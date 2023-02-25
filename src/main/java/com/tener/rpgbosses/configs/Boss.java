package com.tener.rpgbosses.configs;

import java.util.HashMap;

public class Boss {
    private int maxMinions;
    private int amountToSpawn;
    private double cooldownToSpawn;
    private double cooldownCheckTarget;
    private HashMap<String, Minion> minions;
    private HashMap<String, Abilitie> abilities;

    public int getMaxMinions() {
        return maxMinions;
    }

    public void setMaxMinions(int maxMinions) {
        this.maxMinions = maxMinions;
    }

    public int getAmountToSpawn() {
        return amountToSpawn;
    }

    public void setAmountToSpawn(int amountToSpawn) {
        this.amountToSpawn = amountToSpawn;
    }

    public double getCooldownToSpawn() {
        return cooldownToSpawn;
    }

    public void setCooldownToSpawn(double cooldownToSpawn) {
        this.cooldownToSpawn = cooldownToSpawn;
    }

    public double getCooldownCheckTarget() {
        return cooldownCheckTarget;
    }

    public void setCooldownCheckTarget(double cooldownCheckTarget) {
        this.cooldownCheckTarget = cooldownCheckTarget;
    }

    public HashMap<String, Minion> getMinions() {
        return minions;
    }

    public void setMinions(HashMap<String, Minion> minions) {
        this.minions = minions;
    }

    public HashMap<String, Abilitie> getAbilities() {
        return abilities;
    }

    public void setAbilities(HashMap<String, Abilitie> abilities) {
        this.abilities = abilities;
    }
}
