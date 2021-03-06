/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap.city;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.MapType;
import net.voxton.voxtongen.platmap.generic.PlatMapRoadBorder;
import net.voxton.voxtongen.plat.PlatLot;
import net.voxton.voxtongen.plat.park.PlatPark;
import net.voxton.voxtongen.plat.park.PlatStatue;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapCentralPark extends PlatMapRoadBorder {
    private PlatLot lastPlat = null;

    public PlatMapCentralPark(World world, PlatMapContext context, int platX, int platZ) {
        super(world, context, platX, platZ);

        for (int x = HALF_BORDER; x < SIDE - HALF_BORDER; x++) {
            for (int z = HALF_BORDER; z < SIDE - HALF_BORDER; z++) {
                PlatLot last = lastPlat;

                int rand = platRand.nextInt(14);

                lastPlat = (rand == 0) ? new PlatStatue(platRand, context) : new PlatPark(platRand, context);

                if (last != null && last.isConnectable(lastPlat)) {
                    lastPlat.makeConnected(platRand, last);
                }

                platLots[x][z] = lastPlat;
            }
        }

        lastPlat = null;
    }

    @Override
    public MapType getType() {
        return MapType.CENTRAL_PARK;
    }

}
