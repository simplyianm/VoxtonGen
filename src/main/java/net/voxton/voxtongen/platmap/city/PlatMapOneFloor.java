/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap.city;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.MapType;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.plats.building.PlatOneFloorBuilding;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapOneFloor extends PlatMapBlocks {
    public PlatMapOneFloor(World world, PlatMapContext context, int platX, int platZ) {
        super(world, context, platX, platZ);
    }

    @Override
    protected PlatLot makeBlockPlat(int bx, int bz, int x, int z) {
        return new PlatOneFloorBuilding(platRand, context);
    }

    @Override
    public MapType getType() {
        return MapType.ONE_FLOOR;
    }

}
