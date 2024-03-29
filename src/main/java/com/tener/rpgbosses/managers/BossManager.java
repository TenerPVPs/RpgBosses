package com.tener.rpgbosses.managers;

import com.tener.rpgbosses.RpgBosses;
import com.tener.rpgbosses.objects.BossObject;

import java.util.HashMap;
import java.util.Map;

public class BossManager {
    private HashMap<String, BossObject> bossObjectHashMap = new HashMap<>();

    public HashMap<String, BossObject> getBossObjectHashMap() {
        return bossObjectHashMap;
    }

    public void setBossObjectHashMap(HashMap<String, BossObject> bossObjectHashMap) {
        this.bossObjectHashMap = bossObjectHashMap;
    }

    public void InsertBoss(String bossUUID, BossObject bossObject){
        this.bossObjectHashMap.put(bossUUID,bossObject);
    }
    public void RemoveBoss(String bossUUID){
        if (bossObjectHashMap.containsKey(bossUUID)) {
            bossObjectHashMap.get(bossUUID).UnregistryAbilities();
            bossObjectHashMap.remove(bossUUID);
        }
    }
    public void Check() {
        if (RpgBosses.ServerWorldLoaded) {
            for (Map.Entry<String,BossObject> pairs : bossObjectHashMap.entrySet()) {
                BossObject bossObject = pairs.getValue();

                bossObject.CheckIsTargeting();
                bossObject.SpawnMinions();
                bossObject.MinionsSetTarget();
            }
        }
    }
}
