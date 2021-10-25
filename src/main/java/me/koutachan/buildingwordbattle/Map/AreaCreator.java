package me.koutachan.buildingwordbattle.Map;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public class AreaCreator {

    private final int xMin, yMin, zMin, xMax, yMax, zMax;

    public AreaCreator(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;

        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    /**
     * @param location は自分の場所を必要とします
     * @return isArea (boolean)
     * @credit - https://www.spigotmc.org/threads/how-get-spaces-of-place-to-protect.398587/#post-3570759
     * @credit - https://github.com/nanami-network/BuildCastle/blob/d67f5475b32b6342aea2339d61428a59691f33cf/src/main/java/me/koutachan/battleroyale/util/LocationUtil.java#L20
     */

    public boolean isArea(Location location) {
        return (location.getBlockX() <= xMax && location.getBlockX() >= xMin) && (location.getBlockY() <= yMax && location.getBlockY() >= yMin) && (location.getBlockZ() <= zMax && location.getBlockZ() >= zMin);
    }
}