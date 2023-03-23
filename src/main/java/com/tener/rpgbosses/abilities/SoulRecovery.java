package com.tener.rpgbosses.abilities;

import com.tener.rpgbosses.RpgBosses;
import com.tener.rpgbosses.abilitiesinterfarces.SoulRecoveryInterface;
import com.tener.rpgbosses.configs.Abilitie;
import com.tener.rpgbosses.configs.General;
import com.tener.rpgbosses.configs.JSONConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoulRecovery extends Abilitie implements SoulRecoveryInterface {

    private Abilitie configs;
    private Entity bossEntity;
    private LivingEntity bossLivingEntity;

    public SoulRecovery(Abilitie configs, Entity bossEntity){
        this.configs = configs;
        this.ValidateConfigs();
        this.bossEntity = bossEntity;
        this.bossLivingEntity = (LivingEntity) this.bossEntity;
        RpgBosses.LOGGER.info("registered");
        RegistryEvents();
    }

    @Override
    public void RegistryEvents() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void ValidateConfigs() {
        if (configs.isUseGeneralConfigs()) {
            General general = JSONConfig.configs.getGeneral();
            Abilitie abilitie = general.getAbilities().getOrDefault("soulrecovery", null);

            if (abilitie != null) {
                this.configs = abilitie;
            }
        }
    }

    @SubscribeEvent
    public void KilledEntity(LivingDeathEvent event){
        Entity killer = event.getSource().getEntity();

        if (killer == bossEntity) {
            float hpToHeal = bossLivingEntity.getMaxHealth() * configs.getSoulHealAmount();
            bossLivingEntity.heal(hpToHeal);
        }
    }

    @Override
    public void UnregistryAllEvents(){
        RpgBosses.LOGGER.info("all removed ez");
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
