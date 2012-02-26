/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap.generic;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.plats.road.PlatRoadArtery;
import org.bukkit.World;

/**
 * A plat map with a road border.
 */
public abstract class PlatMapRoadBorder extends PlatMap {
    public PlatMapRoadBorder(World world, PlatMapContext context, int platX, int platZ) {
        super(world, context, platX, platZ);

        for (int x = 0; x < SIDE; x++) {
            for (int z = 0; z < SIDE; z++) {
                if (x < HALF_BORDER || z < HALF_BORDER || x > INT_SIDE_END - 1 || z > INT_SIDE_END - 1) {
                    platLots[x][z] = new PlatRoadArtery(platRand, context);
                }
            }
        }
    }

}
