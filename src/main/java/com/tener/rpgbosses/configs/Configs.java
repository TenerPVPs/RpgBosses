package com.tener.rpgbosses.configs;

import java.util.HashMap;

public class Configs {
    private HashMap<String,Boss> bosses;

    private HashMap<String,Object> general;

    public HashMap<String, Boss> getBosses() {
        return bosses;
    }

    public void setBosses(HashMap<String, Boss> bosses) {
        this.bosses = bosses;
    }

    public HashMap<String, Object> getGeneral() {
        return general;
    }

    public void setGeneral(HashMap<String, Object> general) {
        this.general = general;
    }
}
