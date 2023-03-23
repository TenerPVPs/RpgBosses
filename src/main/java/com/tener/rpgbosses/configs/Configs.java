package com.tener.rpgbosses.configs;

import java.util.HashMap;

public class Configs {
    private HashMap<String,Boss> bosses;

    private General general;

    public HashMap<String, Boss> getBosses() {
        return bosses;
    }

    public void setBosses(HashMap<String, Boss> bosses) {
        this.bosses = bosses;
    }

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }
}
