package me.koutachan.buildingwordbattle;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

public class BoxCreator {

    private final Location center;

    public BoxCreator(Location center) {
        this.center = center;
    }

    public void CreatFlat(Material material, int xMin, int xMax, int zMin, int zMax, boolean setAir) {
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                if (x == xMin || z == zMin || x == xMax || z == zMax) {
                    center.clone().add(x, 0, z).getBlock().setType(material);
                } else {
                    if (setAir) center.clone().add(x, 0, z).getBlock().setType(Material.AIR);
                }
            }
        }
    }

    public void CreateCube(Material material, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, boolean setAir) {
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    if (x == xMin || y == yMin || z == zMin || x == xMax || z == zMax) {
                        center.clone().add(x, y, z).getBlock().setType(material);
                    } else {
                        if (setAir) center.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }

    public void CreateCubeFM(Material material, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, boolean setAir) {
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    if (y == yMin || z == zMin || x == xMax) {
                        center.clone().add(x, y, z).getBlock().setType(material);
                    } else {
                        if (setAir) center.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }

    public void ChunkCube(Material material, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, boolean setAir) {
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    if (x == xMin || y == yMin || z == zMin || x == xMax || z == zMax) {
                        center.clone().add(x, y, z).getBlock().setType(material);
                    } else {
                        if (setAir) center.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }

    public void CreateCubeFillTop(Material material, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, boolean setAir) {
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    if (x == xMin || y == yMin || z == zMin || x == xMax || y == yMax || z == zMax) {
                        center.clone().add(x, y, z).getBlock().setType(material);
                    } else {
                        if (setAir) center.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }
}