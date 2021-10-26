package me.koutachan.buildingwordbattle.Map;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class AreaCreator {

    private final int xMin, yMin, zMin, xMax, yMax, zMax;

    private String author;

    public AreaCreator(String author, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        this.author = author;

        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;

        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    public AreaCreator(String author, Location location1, Location location2) {
        this.author = author;

        if (location1.getBlockX() >= location2.getBlockX()) {
            xMax = location1.getBlockX();
            xMin = location2.getBlockX();
        } else {
            xMax = location2.getBlockX();
            xMin = location1.getBlockX();
        }

        if (location1.getBlockY() >= location2.getBlockY()) {
            yMax = location1.getBlockY();
            yMin = location2.getBlockY();
        } else {
            yMax = location2.getBlockY();
            yMin = location1.getBlockY();
        }

        if (location1.getBlockZ() >= location2.getBlockZ()) {
            zMax = location1.getBlockZ();
            zMin = location2.getBlockZ();
        } else {
            zMax = location2.getBlockZ();
            zMin = location1.getBlockZ();
        }
    }

    /**
     * @param location 自分の場所を必要とします
     * @return isArea (boolean)
     * @credit - https://www.spigotmc.org/threads/how-get-spaces-of-place-to-protect.398587/#post-3570759
     * @credit - https://github.com/nanami-network/BuildCastle/blob/d67f5475b32b6342aea2339d61428a59691f33cf/src/main/java/me/koutachan/battleroyale/util/LocationUtil.java#L20
     */

    public boolean isArea(Location location) {
        return (location.getBlockX() <= xMax && location.getBlockX() >= xMin) && (location.getBlockY() <= yMax && location.getBlockY() >= yMin) && (location.getBlockZ() <= zMax && location.getBlockZ() >= zMin);
    }
}