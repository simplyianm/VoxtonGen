/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.plat.road;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.chunk.ByteChunk;
import net.voxton.voxtongen.plat.PlatType;
import net.voxton.voxtongen.surrounding.SurroundingRoads;
import org.bukkit.Material;

/**
 *
 * @author simplyianm
 */
public class PlatRoadArtery extends PlatRoadPaved {
    private byte medianId = (byte) Material.GLOWSTONE.getId();

    public PlatRoadArtery(Random rand, PlatMapContext context) {
        super(rand, context);
    }

    @Override
    protected void doPavement(PlatMapContext context, SurroundingRoads roads, ByteChunk chunk) {
        super.doPavement(context, roads, chunk);

        RoadOrientation orientation = roads.getOrientation();

        int sl = context.streetLevel;
        int slp = sl + 1;

        switch (orientation) {
            case NORTH_AVE:
                chunk.setBlocks(0, ByteChunk.WIDTH, sl, slp, 14, 15, medianId);
                break;

            case SOUTH_AVE:
                chunk.setBlocks(0, ByteChunk.WIDTH, sl, slp, 1, 2, medianId);
                break;

            case WEST_AVE:
                chunk.setBlocks(14, 15, sl, slp, 0, ByteChunk.WIDTH, medianId);
                break;

            case EAST_AVE:
                chunk.setBlocks(1, 2, sl, slp, 0, ByteChunk.WIDTH, medianId);
                break;
        }
    }

    @Override
    public PlatType getType() {
        return PlatType.ROAD_ARTERY;
    }

}
