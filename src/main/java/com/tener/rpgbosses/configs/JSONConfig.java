package com.tener.rpgbosses.configs;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.tener.rpgbosses.RpgBosses;

import java.io.*;
import java.util.Map;

public class JSONConfig {
    private static File file;
    private static Writer writer;
    private static JsonReader reader;
    private static Gson gson;
    public static Configs configs;

    public static void Create(){
        gson = new Gson();
        file = new File("config/rpgbosses.json");

        try {
            if (!file.exists()){
                file.createNewFile();
            }
            writer = new FileWriter(file,true);
            reader = new JsonReader(new FileReader(file));

            Load();
        }catch (IOException e) {

        }
    }

    public static void Load(){
        configs = gson.fromJson(reader,Configs.class);

        if (configs != null && configs.getBosses() != null) {
            for (Map.Entry<String, Boss> pair : configs.getBosses().entrySet()){
                RpgBosses.LOGGER.info(pair.getKey());
            }
        }
    }

}
