package com.tener.rpgbosses.configs;

import java.util.HashMap;
import java.util.List;

public class Boss {
    private int maxMinions;
    private int amountToSpawn;
    private double cooldownToSpawn;
    private double cooldownCheckTarget;
    private String infernalmobs_abilites;
    private Integer champions_rank;
    private Integer undefeatableRank;
    private List<String> champions_abilites;
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

    public String getInfernalmobs_abilites() {
        return infernalmobs_abilites;
    }

    public void setInfernalmobs_abilites(String infernalmobs_abilites) {
        this.infernalmobs_abilites = infernalmobs_abilites;
    }

    public Integer getChampions_rank() {
        return champions_rank;
    }

    public void setChampions_rank(Integer champions_rank) {
        this.champions_rank = champions_rank;
    }

    public List<String> getChampions_abilites() {
        return champions_abilites;
    }

    public void setChampions_abilites(List<String> champions_abilites) {
        this.champions_abilites = champions_abilites;
    }

    public Integer getUndefeatableRank() {
        return undefeatableRank;
    }

    public void setUndefeatableRank(Integer undefeatableRank) {
        this.undefeatableRank = undefeatableRank;
    }
}
