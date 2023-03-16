package com.tener.rpgbosses.objects;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import com.bottomtextdanny.dannys_expansion.core.capabilities.player.DannyEntityCap;
import com.tener.rpgbosses.RpgBosses;
import com.tener.rpgbosses.configs.Boss;
import com.tener.rpgbosses.configs.Minion;
import com.tener.rpgbosses.interfaces.BossInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.AffixesConfig;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.ChampionBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossObject implements BossInterface {
    private Boss boss;
    private Entity bossEntity;
    private LivingEntity bossLivingEntity;
    private ServerWorld serverWorld;
    private boolean targeting = false;
    private HashMap<String,Entity> minionsSpawned = new HashMap<>();
    private HashMap<String, ResourceLocation> minionsResources = new HashMap<>();
    private HashMap<Entity, List<Entity>> targetsList = new HashMap<>();
    private long lastSpawned;
    private long lastCheckedTarget;
    private int minionsAmount = 0;

    public boolean isTargeting() {
        return targeting;
    }

    public void setTargeting(boolean targeting) {
        this.targeting = targeting;
    }

    public BossObject(Boss boss, Entity bossEntity, ServerWorld serverWorld){
        this.boss = boss;
        this.bossEntity  = bossEntity;
        this.serverWorld = serverWorld;

        this.bossEntity.addTag(RpgBosses.modId + ":boss");
        this.bossLivingEntity = (LivingEntity) this.bossEntity;
        lastSpawned = System.currentTimeMillis() - (long) boss.getCooldownToSpawn();
        lastCheckedTarget = System.currentTimeMillis() - (long) boss.getCooldownCheckTarget();

        this.SetChampionsMobsModifier();
        this.SetUndefeatablesMobsTier();
        this.SetInfernalMobsModifier();
    }

    public LivingEntity getBossLivingEntity() {
        return bossLivingEntity;
    }

    public void setBossLivingEntity(LivingEntity bossLivingEntity) {
        this.bossLivingEntity = bossLivingEntity;
    }

    public List<Entity> GetTargetsByDistance(){
       List<Entity> targets = new ArrayList<>();

        for (int i = 0; i < minionsAmount; i++) {
            float farDistance = 0;
            Entity target = null;

            for (Map.Entry<Entity, List<Entity>> pair : targetsList.entrySet()){
                Entity targetChecking = pair.getKey();

                if (targetChecking == null || !targetChecking.isAlive() || !targetChecking.isAddedToWorld()) {
                    continue;
                }

                float distance = bossEntity.distanceTo(targetChecking);

                if (distance > farDistance && !targets.contains(targetChecking)) {
                    farDistance = distance;
                    target = targetChecking;
                }
            }

            if (target != null) {
                targets.add(target);
            }
        }

        return targets;
    }

    @Override
    public String GetRandomMinion() {
        double totalWeight = 0.0;
        String minionName = "";

        for (Map.Entry<String, Minion> i : boss.getMinions().entrySet()) {
            totalWeight += i.getValue().getSpawnChance();
        }

        double r = Math.random() * totalWeight;
        for (Map.Entry<String, Minion> i : boss.getMinions().entrySet()) {
            r -= i.getValue().getSpawnChance();
            if (r <= 0.0) {
                minionName = i.getKey();
                break;
            }
        }

        return minionName;
    }

    @Override
    public void AddTarget(Entity target) {
        targetsList.put(target,new ArrayList<>());
    }

    @Override
    public void RemoveMinion(String UUID) {
        if (minionsSpawned.containsKey(UUID)) {
            minionsSpawned.remove(UUID);
            --minionsAmount;
        }
    }

    @Override
    public void CheckIsTargeting() {

        if (bossLivingEntity.getLastHurtByMob() == null) {
            targeting = false;
            targetsList.clear();

            for (Map.Entry<String,Entity> pairs : minionsSpawned.entrySet()){
                LivingEntity minionLiving = (LivingEntity) pairs.getValue();

                minionLiving.setLastHurtByMob(null);
            }
        }
    }

    @Override
    public boolean CanMinionsSetTarget() {
        return (targeting && System.currentTimeMillis() - lastCheckedTarget >= boss.getCooldownCheckTarget());
    }

    @Override
    public void MinionsSetTarget() {
        if (CanMinionsSetTarget()) {
            List<Entity> targets = GetTargetsByDistance();
            int targeted = 0;
            int targetsSize = targets.size();

            if (targets != null && targets.size() > 0) {
                for (Map.Entry<String,Entity> pairs : minionsSpawned.entrySet()){
                    if ((targeted + 1) > targetsSize) {
                        targeted = 0;
                    }

                    if (targets.get(targeted) != null) {
                        LivingEntity targetedLivingEntity = (LivingEntity) targets.get(targeted);
                        LivingEntity minionLiving = (LivingEntity) pairs.getValue();

                        if ((minionLiving == null || !minionLiving.isAlive() || !minionLiving.isAddedToWorld())
                                || (targetedLivingEntity == null || !targetedLivingEntity.isAlive() || !targetedLivingEntity.isAddedToWorld())
                        ) {
                            continue;
                        }

                        targeted++;
                    }
                }
            }
        }
    }

    @Override
    public void SetInfernalMobsModifier() {
        if (ModList.get().isLoaded("infernalmobs")) {
            String mods = boss.getInfernalmobs_abilites();

            if (mods != null && !mods.equals("")) {
                String savedMods = bossEntity.getPersistentData().getString(InfernalMobsCore.instance().getNBTTag());

                if (savedMods.isEmpty() || !savedMods.equals(mods)) {
                    bossEntity.getPersistentData().putString(InfernalMobsCore.instance().getNBTTag(), mods);
                }

                RpgBosses.LOGGER.info(mods);
            }
        }
    }

    @Override
    public void SetChampionsMobsModifier() {
        if (ModList.get().isLoaded("champions")) {
            boolean have_abilities;

            if (boss.getChampions_abilites() != null && boss.getChampions_abilites().size() > 0) {
                have_abilities = true;
            } else {
                have_abilities = false;
            }

            ChampionCapability.getCapability(bossEntity).ifPresent(capability -> {
                IChampion.Server server = capability.getServer();
                List<IAffix> affixes = new ArrayList<>();

                if (have_abilities) {
                    Rank rank = boss.getChampions_rank() == -1 ? RankManager.getHighestRank() : RankManager.getRanks().containsKey(boss.getChampions_rank()) ? RankManager.getRank(boss.getChampions_rank()) : null;

                    for (String e : boss.getChampions_abilites()) {
                        if (Champions.API.getAffix(e.toLowerCase()).isPresent()) {
                            affixes.add(Champions.API.getAffix(e.toLowerCase()).get());
                        }
                    }

                    ChampionBuilder.spawnPreset(capability,rank != null ? rank.getTier() : null,affixes);
                }
            });
        }
    }

    @Override
    public void SetUndefeatablesMobsTier() {
        if (ModList.get().isLoaded("undefeatables")){

        }
    }

    @Override
    public boolean CanSpawnMinions() {
        return (targeting && System.currentTimeMillis() - lastSpawned >= boss.getCooldownToSpawn()
                && minionsAmount < boss.getMaxMinions()
        );
    }

    @Override
    public void SpawnMinions() {
        if (CanSpawnMinions()) {

            RpgBosses.LOGGER.info("Trying spawn minion");
            RpgBosses.LOGGER.info(bossLivingEntity.getName().getString());

            lastSpawned = System.currentTimeMillis();

            StringTextComponent minionTagName = new StringTextComponent(bossLivingEntity.getName().getString() + " minion");
            Vector3d bossPosition = bossEntity.position();

            for (int i = 0; i < boss.getAmountToSpawn();i++){
                String minionName = GetRandomMinion();

                RpgBosses.LOGGER.info(minionName);

                if (!minionsResources.containsKey(minionName)) {
                    minionsResources.put(minionName,new ResourceLocation(minionName));
                }

                Entity minion = ForgeRegistries.ENTITIES.getValue(minionsResources.get(minionName))
                        .spawn(serverWorld,null,minionTagName,null,new BlockPos(bossPosition.x,bossPosition.y,bossPosition.z),SpawnReason.MOB_SUMMONED,false,false);

                minion.addTag(RpgBosses.modId + ":boss_minion");
                minion.addTag(("bossid:" + bossEntity.getStringUUID()));

                ++minionsAmount;
                minionsSpawned.put(minion.getStringUUID(),minion);

                if (minionsAmount == boss.getMaxMinions()) {
                    break;
                }
            }
        }
    }
}
