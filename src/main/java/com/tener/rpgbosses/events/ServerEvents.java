package com.tener.rpgbosses.events;

import com.tener.rpgbosses.RpgBosses;
import com.tener.rpgbosses.configs.Configs;
import com.tener.rpgbosses.configs.JSONConfig;
import com.tener.rpgbosses.managers.BossManager;
import com.tener.rpgbosses.objects.BossObject;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = RpgBosses.modId,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {
    @SubscribeEvent
    public static void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide) {
            Entity spawned = event.getEntity();
            String entityRegistryName = spawned.getType().getRegistryName().toString();

            Configs configs = JSONConfig.configs;

            if (configs.getBosses().containsKey(entityRegistryName)) {
                RpgBosses.LOGGER.info("Boss spawned " + entityRegistryName);
                RpgBosses.bossManager.InsertBoss(spawned.getStringUUID(),new BossObject(configs.getBosses().get(entityRegistryName),spawned, event.getWorld().getServer().overworld()));
            }
        }
    }

    @SubscribeEvent
    public static void EntityLeaveWorldEvent(EntityLeaveWorldEvent event){
        if (!event.getWorld().isClientSide) {
            Entity leaved = event.getEntity();
            Set<String> tags = leaved.getTags();
            String entityRegistryName = leaved.getType().getRegistryName().toString();

            if(tags.contains(RpgBosses.modId + ":boss")) {
                RpgBosses.LOGGER.info("Boss leaved " + entityRegistryName);
                RpgBosses.bossManager.RemoveBoss(leaved.getStringUUID());
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
        Set<String> tags = attacked.getTags();
        String UUID = attacked.getStringUUID();
        BossManager bossManager = RpgBosses.bossManager;

        if(tags.contains(RpgBosses.modId + ":boss") && bossManager.getBossObjectHashMap().containsKey(UUID)) {
            BossObject bossObject = bossManager.getBossObjectHashMap().get(UUID);
            bossObject.setTargeting(true);
            bossObject.AddTarget(event.getTarget());
        }
    }
}
