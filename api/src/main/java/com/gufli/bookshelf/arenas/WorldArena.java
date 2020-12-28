package com.gufli.bookshelf.arenas;

import com.gufli.bookshelf.entity.PlatformLocation;

public class WorldArena implements Arena {

    private final String worldName;

    public WorldArena(String worldName) {
        this.worldName = worldName;
    }

    @Override
    public boolean contains(PlatformLocation loc) {
        return loc.getWorldName().equals(worldName);
    }

    public String getWorldName() {
        return worldName;
    }
}
