package com.tener.rpgbosses.interfaces;

import net.minecraft.entity.Entity;

public interface BossInterface {
    public abstract void SpawnMinions();
    public abstract boolean CanSpawnMinions();
    public abstract String GetRandomMinion();
    public abstract void AddTarget(Entity target);
}
