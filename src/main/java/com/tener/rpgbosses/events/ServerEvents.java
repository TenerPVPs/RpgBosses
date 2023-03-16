package com.tener.rpgbosses.events;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import com.tener.rpgbosses.RpgBosses;
import com.tener.rpgbosses.configs.Configs;
import com.tener.rpgbosses.configs.JSONConfig;
import com.tener.rpgbosses.managers.BossManager;
import com.tener.rpgbosses.objects.BossObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Set;

@Mod.EventBusSubscriber(modid = RpgBosses.modId,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide && event.getEntity() instanceof LivingEntity) {
            Entity spawned = event.getEntity();
            String entityRegistryName = spawned.getType().getRegistryName().toString();

            Configs configs = JSONConfig.configs;

            if (configs.getBosses().containsKey(entityRegistryName)) {
                RpgBosses.LOGGER.info("Boss spawned " + entityRegistryName);
                RpgBosses.bossManager.InsertBoss(spawned.getStringUUID(),new BossObject(configs.getBosses().get(entityRegistryName),spawned, event.getWorld().getServer().overworld()));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void EntityLeaveWorldEvent(EntityLeaveWorldEvent event){
        if (!event.getWorld().isClientSide && event.getEntity() instanceof LivingEntity) {
            Entity leaved = event.getEntity();
            Set<String> tags = leaved.getTags();
            String entityRegistryName = leaved.getType().getRegistryName().toString();
            BossManager bossManager = RpgBosses.bossManager;
            String UUID = leaved.getStringUUID();

            if(tags.contains(RpgBosses.modId + ":boss")) {
                RpgBosses.LOGGER.info("Boss leaved " + entityRegistryName);
                RpgBosses.bossManager.RemoveBoss(UUID);
            }else if(tags.contains(RpgBosses.modId + ":boss_minion")) {
                for (String tag : tags) {
                    if (tag.contains("bossid")) {
                        RpgBosses.LOGGER.info("Minion leaved " + entityRegistryName);
                        HashMap<String, BossObject> bossObjectHashMap = bossManager.getBossObjectHashMap();
                        String bossUUID = tag.substring(tag.lastIndexOf(":") + 1);

                        if (bossObjectHashMap.containsKey(bossUUID)){
                            bossObjectHashMap.get(tag.substring(tag.lastIndexOf(":") + 1)).RemoveMinion(UUID);
                        }

                        leaved.removeTag(RpgBosses.modId + ":boss_minion");
                        leaved.removeTag("bossid:" + bossUUID);

                        leaved.remove();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void ServerTickEvent(TickEvent.ServerTickEvent event){
        RpgBosses.bossManager.Check();
    }

    @SubscribeEvent
    public static void AttackEntityEvent(AttackEntityEvent event){
        Entity attacked = event.getEntity();
        Entity agressor = event.getTarget();
        String victimUUID = attacked.getStringUUID();
        Set<String> tags = attacked.getTags();
        String UUID = attacked.getStringUUID();
        BossManager bossManager = RpgBosses.bossManager;

        if (agressor != null) {
            Set<String> tagsAgressor = agressor.getTags();
            String agressorUUID = agressor.getStringUUID();

            if((tags.contains(RpgBosses.modId + ":boss") && tagsAgressor.contains("bossid:" + victimUUID)) ||
                    (tags.contains("bossid:" + agressorUUID) && tagsAgressor.contains(RpgBosses.modId + ":boss")))
            {
                event.setCanceled(true);
                return;
            }
        }

        if(tags.contains(RpgBosses.modId + ":boss") && bossManager.getBossObjectHashMap().containsKey(UUID)) {
            BossObject bossObject = bossManager.getBossObjectHashMap().get(UUID);
            bossObject.getBossLivingEntity().setLastHurtByMob((LivingEntity) agressor);
            bossObject.setTargeting(true);
            bossObject.AddTarget(event.getTarget());
        }
    }

    @SubscribeEvent
    public static void LivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        Entity entity = event.getEntity();
        Set<String> tags = entity.getTags();
        String UUID = entity.getStringUUID();
        BossManager bossManager = RpgBosses.bossManager;

        if(tags.contains(RpgBosses.modId + ":boss") && bossManager.getBossObjectHashMap().containsKey(UUID)) {
            BossObject bossObject = bossManager.getBossObjectHashMap().get(UUID);
            bossObject.getBossLivingEntity().setLastHurtByMob(event.getTarget());
            bossObject.setTargeting(true);
            bossObject.AddTarget(event.getTarget());
        }
    }

    @SubscribeEvent
    public static void LivingHurtEvent(LivingHurtEvent event){
       Entity victim = event.getEntity();
       Entity agressor = event.getSource().getEntity();

       if (agressor != null) {
           String victimUUID = victim.getStringUUID();
           String agressorUUID = agressor.getStringUUID();

           Set<String> tagsVictim = victim.getTags();
           Set<String> tagsAgressor = agressor.getTags();

           if((tagsVictim.contains(RpgBosses.modId + ":boss") && tagsAgressor.contains("bossid:" + victimUUID)) ||
                   (tagsVictim.contains("bossid:" + agressorUUID) && tagsAgressor.contains(RpgBosses.modId + ":boss")))
           {
               event.setCanceled(true);
           }
       }
    }

    @SubscribeEvent
    public static void LivingAttackEvent(LivingAttackEvent event){
        Entity victim = event.getEntity();
        Entity agressor = event.getSource().getEntity();

        if (agressor != null) {
            String victimUUID = victim.getStringUUID();
            String agressorUUID = agressor.getStringUUID();

            Set<String> tagsVictim = victim.getTags();
            Set<String> tagsAgressor = agressor.getTags();

            if((tagsVictim.contains(RpgBosses.modId + ":boss") && tagsAgressor.contains("bossid:" + victimUUID)) ||
                    (tagsVictim.contains("bossid:" + agressorUUID) && tagsAgressor.contains(RpgBosses.modId + ":boss")))
            {
                event.setCanceled(true);
            }
        }
    }

}
