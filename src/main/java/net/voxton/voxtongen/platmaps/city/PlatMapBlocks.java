package net.voxton.voxtongen.platmaps.city;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmaps.PlatMapRoadBorder;
import net.voxton.voxtongen.plats.*;
import net.voxton.voxtongen.plats.road.PlatRoadPaved;
import net.voxton.voxtongen.plats.road.PlatRoad;

import org.bukkit.World;

/**
 * Represents an urban plat map, aka a plat map with a road grid.
 */
public abstract class PlatMapBlocks extends PlatMapRoadBorder {
    public PlatMapBlocks(World world, Random random, PlatMapContext context, int platX, int platZ) {
        super(world, random, context, platX, platZ);

        //TODO rivers and railroads?
        //Nah

        //Calculate plat map borders

        fillPlats();

//        //TODO I need to come up with a more elegant way of doing this!
//        // what to build?
//        if (platRand.nextInt(context.oddsOfParks) == 0) {
//            current = new PlatPark(platRand, context);
//        } else if (platRand.nextInt(context.oddsOfUnfinishedBuildings) == 0) {
//            current = new PlatUnfinishedBuilding(platRand, context);
//        } else {
//            current = new PlatOfficeBuilding(platRand, context);
//        }
    }

    /**
     * Fills all plats.
     */
    private void fillPlats() {
        for (int x = HALF_BORDER; x < SIDE - HALF_BORDER; x++) {
            for (int z = HALF_BORDER; z < SIDE - HALF_BORDER; z++) {
                if ((x % (BLOCK_WIDTH + 1) == 0) || (z % (BLOCK_WIDTH + 1) == 0)) {
                    platLots[x][z] = new PlatRoadPaved(platRand, context);
                }
            }
        }

        for (int bx = 0; bx < BLOCKS_ACROSS; bx++) {
            for (int bz = 0; bz < BLOCKS_ACROSS; bz++) {

                for (int x = 0; x < BLOCK_WIDTH; x++) {
                    for (int z = 0; z < BLOCK_WIDTH; z++) {

                        platLots[HALF_BORDER + (bx * BLOCK_GRP_WIDTH) + x][HALF_BORDER + (bz * BLOCK_GRP_WIDTH) + z] = makeBlockPlat(bx, bz, x, z);

                    }
                }

            }
        }
    }

    /**
     * Makes a plat within a block.
     *
     * @param bx The x of the block. (Starts at 0)
     * @param bz The z of the block. (Starts at 0)
     * @param x The internal x of the plat. (Starts at 0)
     * @param z The internal z of the plat. (Starts at 0)
     * @return The plat lot.
     */
    protected abstract PlatLot makeBlockPlat(int bx, int bz, int x, int z);

    protected PlatRoad getRoadPlat(int x, int z) {
        //Inside tic-tac-toe


        return null;
    }

    private void placeRoundabout(int x, int z) {
        platLots[x - 1][z - 1] = new PlatRoadPaved(platRand, context);
        platLots[x - 1][z] = new PlatRoadPaved(platRand, context);
        platLots[x - 1][z + 1] = new PlatRoadPaved(platRand, context);
        platLots[x][z - 1] = new PlatRoadPaved(platRand, context);

        platLots[x][z] = new PlatStatue(platRand, context);

        platLots[x][z + 1] = new PlatRoadPaved(platRand, context);
        platLots[x + 1][z - 1] = new PlatRoadPaved(platRand, context);
        platLots[x + 1][z] = new PlatRoadPaved(platRand, context);
        platLots[x + 1][z + 1] = new PlatRoadPaved(platRand, context);
    }

    /**
     * for each plot randomly pick a plattype see if the "previous chunk" is the
     * same type if so make the new plattype connected to the previous type if
     * the new plot is shorter than the previous plot or if the new plot is
     * shallower than the previous slot mark the previous plot to have stairs if
     * plot does not have neighbors yet mark the plot to have stairs
     */
    private void makeConnections(PlatLot current, int x, int z) {

        // see if the previous chunk is the same type
        PlatLot previous = null;
        if (x > 0 && current.isConnectable(platLots[x - 1][z])) {
            previous = platLots[x - 1][z];
        } else if (z > 0 && current.isConnectable(platLots[x][z - 1])) {
            previous = platLots[x][z - 1];
        }

        // if there was a similar previous one then copy it... maybe
        if (previous != null && !previous.isIsolatedLot(context.oddsOfIsolatedLots)) {
            current.makeConnected(platRand, previous);
        }
    }

}
