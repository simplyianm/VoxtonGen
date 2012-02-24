/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmaps.city;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmaps.PlatMapRoadBorder;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.plats.PlatPark;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapCentralPark extends PlatMapRoadBorder {
    private PlatLot lastPlat = null;

    public PlatMapCentralPark(World world, Random random, PlatMapContext context, int platX, int platZ) {
        super(world, random, context, platX, platZ);

        for (int x = HALF_BORDER; x < INT_SIDE; x++) {
            for (int z = HALF_BORDER; z < INT_SIDE; z++) {
                PlatLot last = lastPlat;
                lastPlat = new PlatPark(platRand, context);
                if (last != null) {
                    lastPlat.makeConnected(platRand, last);
                }

                platLots[x][z] = lastPlat;
            }
        }

        lastPlat = null;
    }

}
