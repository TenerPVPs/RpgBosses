package com.tener.rpgbosses.abilities;

import com.tener.rpgbosses.RpgBosses;
import com.tener.rpgbosses.abilitiesinterfarces.GuardianInterface;
import com.tener.rpgbosses.configs.Abilitie;
import com.tener.rpgbosses.configs.General;
import com.tener.rpgbosses.configs.JSONConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Guardian extends Abilitie implements GuardianInterface {
    private Abilitie configs;
    private Entity bossEntity;
    private LivingEntity bossLivingEntity;
    private Vector3d spawnPosition;
    private float lastCooldown;

    public Guardian(Abilitie configs, Entity bossEntity){
        this.configs = configs;
        this.ValidateConfigs();
        this.bossEntity = bossEntity;
        this.bossLivingEntity = (LivingEntity) this.bossEntity;
        this.spawnPosition = bossEntity.position();
        this.lastCooldown = System.currentTimeMillis() - configs.getCooldownRegen();
        RpgBosses.LOGGER.info("registered");
        RegistryEvents();
    }

    @Override
    public boolean IsFarFromSpawn() {
        return bossEntity.distanceToSqr(spawnPosition) > configs.getDistanceFromSpawn();
    }

    @Override
    public void RegistryEvents() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void ValidateConfigs() {
        if (configs.isUseGeneralConfigs()) {
            General general = JSONConfig.configs.getGeneral();
            Abilitie abilitie = general.getAbilities().getOrDefault("guardian", null);

            if (abilitie != null) {
                RpgBosses.LOGGER.info("using general configs");
                this.configs = abilitie;
                RpgBosses.LOGGER.info(configs.getCooldownRegen());
            }
        }
    }

    @SubscribeEvent
    public void LivingUpdateEventCheck(LivingEvent.LivingUpdateEvent event){
        if (event.getEntity() == bossEntity) {
            if (IsFarFromSpawn()) {
                if (bossLivingEntity.getLastHurtByMob() != null) {
                    bossLivingEntity.setLastHurtByMob(null);
                    bossLivingEntity.teleportTo(spawnPosition.x,spawnPosition.y,spawnPosition.z);
                }else if(configs.isInstaRegen()){
                    bossLivingEntity.heal(bossLivingEntity.getMaxHealth());
                }else if (System.currentTimeMillis() - lastCooldown >= configs.getCooldownRegen()){
                    float hpToHeal = bossLivingEntity.getMaxHealth() * configs.getRegenPerCooldown();
                    bossLivingEntity.heal(hpToHeal);
                    lastCooldown = System.currentTimeMillis();
                }
            }
        }
    }
    @Override
    public void UnregistryAllEvents(){
        RpgBosses.LOGGER.info("all removed ez");
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
