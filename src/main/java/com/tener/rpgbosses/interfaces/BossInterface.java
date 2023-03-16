package com.tener.rpgbosses.interfaces;

import net.minecraft.entity.Entity;

public interface BossInterface {
    public abstract void SpawnMinions();
    public abstract boolean CanSpawnMinions();
    public abstract String GetRandomMinion();
    public abstract void AddTarget(Entity target);
    public abstract void RemoveMinion(String UUID);
    public abstract void CheckIsTargeting();
    public abstract boolean CanMinionsSetTarget();
    public abstract void MinionsSetTarget();
    public abstract void SetInfernalMobsModifier();
    public abstract void SetChampionsMobsModifier();
    public abstract void SetUndefeatablesMobsTier();
}
