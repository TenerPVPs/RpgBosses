package com.tener.rpgbosses.objects;

import com.tener.rpgbosses.RpgBosses;
import com.tener.rpgbosses.configs.Boss;
import com.tener.rpgbosses.configs.Minion;
import com.tener.rpgbosses.interfaces.BossInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossObject implements BossInterface {
    private Boss boss;
    private Entity bossEntity;
    private ServerWorld serverWorld;
    private boolean targeting = false;
    private HashMap<String,Entity> minionsSpawned = new HashMap<>();
    private HashMap<String, ResourceLocation> minionsResources = new HashMap<>();
    private List<Entity> targetsList = new ArrayList<>();
    private long lastSpawned;

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
        lastSpawned = System.currentTimeMillis() - (long) boss.getCooldownToSpawn();
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
        targetsList.add(target);
    }

    @Override
    public boolean CanSpawnMinions() {
        return (targeting && System.currentTimeMillis() - lastSpawned >= boss.getCooldownToSpawn());
    }

    @Override
    public void SpawnMinions() {
        if (CanSpawnMinions()) {
            lastSpawned = System.currentTimeMillis();
            String minionName = GetRandomMinion();
            Vector3d bossPosition = bossEntity.position();

            RpgBosses.LOGGER.info(minionName);

            if (!minionsResources.containsKey(minionName)) {
                minionsResources.put(minionName,new ResourceLocation(minionName));
            }

            Entity minion = ForgeRegistries.ENTITIES.getValue(minionsResources.get(minionName))
                    .spawn(serverWorld,null,null,new BlockPos(bossPosition.x,bossPosition.y,bossPosition.z), SpawnReason.MOB_SUMMONED,false,false);

            minionsSpawned.put(minion.getStringUUID(),minion);
        }
    }
}
