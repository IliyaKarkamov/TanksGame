package com.karkamov.tanks.map;

import com.karkamov.tanks.Main;
import com.karkamov.tanks.map.enums.EntityType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LevelReader {
    private ArrayList<Chunk> _chunks = new ArrayList<>();
    private int _chunkPageSize = -1;

    public void parseLevel(String level) {
        InputStream inputStream = Main.class.getResourceAsStream(level);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = br.readLine();

            while (line != null) {
                if (_chunkPageSize < 0) {
                    _chunkPageSize = Integer.parseInt(line);
                } else {
                    String[] entitiesTypes = line.split("\\s+");

                    for (String entityType : entitiesTypes) {
                        _chunks.add(new Chunk(EntityType.values()[Integer.parseInt(entityType)]));
                    }
                }

                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getChunkPageSize() {
        return _chunkPageSize;
    }

    public ArrayList<Chunk> getChunks() {
        return _chunks;
    }
}
