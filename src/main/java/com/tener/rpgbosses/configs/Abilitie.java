package com.tener.rpgbosses.configs;

public class Abilitie {
    private float distanceFromSpawn;
    private boolean instaRegen;
    private float cooldownRegen;
    private float regenPerCooldown;
    private boolean useGeneralConfigs;

    private float soulHealAmount;

    public float getDistanceFromSpawn() {
        return distanceFromSpawn;
    }

    public void setDistanceFromSpawn(float distanceFromSpawn) {
        this.distanceFromSpawn = distanceFromSpawn;
    }

    public boolean isInstaRegen() {
        return instaRegen;
    }

    public void setInstaRegen(boolean instaRegen) {
        this.instaRegen = instaRegen;
    }

    public float getCooldownRegen() {
        return cooldownRegen;
    }

    public void setCooldownRegen(float cooldownRegen) {
        this.cooldownRegen = cooldownRegen;
    }

    public float getRegenPerCooldown() {
        return regenPerCooldown;
    }

    public void setRegenPerCooldown(float regenPerCooldown) {
        this.regenPerCooldown = regenPerCooldown;
    }

    public boolean isUseGeneralConfigs() {
        return useGeneralConfigs;
    }

    public void setUseGeneralConfigs(boolean useGeneralConfigs) {
        this.useGeneralConfigs = useGeneralConfigs;
    }

    public float getSoulHealAmount() {
        return soulHealAmount;
    }

    public void setSoulHealAmount(float soulHealAmount) {
        this.soulHealAmount = soulHealAmount;
    }


    public void UnregistryAllEvents(){

    }
}
