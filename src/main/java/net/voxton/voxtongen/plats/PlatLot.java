package net.voxton.voxtongen.plats;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.chunk.ByteChunk;
import net.voxton.voxtongen.chunk.RealChunk;

import org.bukkit.Material;

public abstract class PlatLot {
    protected Random rand;

    protected long connectedkey;

    protected static byte bedrockId = (byte) Material.BEDROCK.getId();

    protected static byte stoneId = (byte) Material.STONE.getId();

    protected static byte lavaId = (byte) Material.LAVA.getId();

    public PlatLot(Random rand, PlatMapContext context) {
        super();
        this.rand = rand;

        //TODO while this is relatively safe, I would feel better to have something airtight
        connectedkey = rand.nextLong();
    }

    /**
     * Gets the type of plat this plat is.
     *
     * @return
     */
    public abstract PlatType getType();

    public abstract void generateChunk(PlatMap platmap, ByteChunk chunk, PlatMapContext context, int platX, int platZ);

    public void generateBlocks(PlatMap platmap, RealChunk chunk, PlatMapContext context, int platX, int platZ) {
        // default one does nothing!
    }

    public void makeConnected(Random rand, PlatLot relative) {
        connectedkey = relative.connectedkey;
    }

    public boolean isConnectable(PlatLot relative) {
        return getClass().isInstance(relative);
    }

    public boolean isIsolatedLot(int oddsOfIsolation) {
        return rand.nextInt(oddsOfIsolation) == 0;
    }

    public boolean isConnected(PlatLot relative) {
        return connectedkey == relative.connectedkey;
    }

    //TODO move this logic to SurroundingLots, add to it the ability to produce SurroundingHeights and SurroundingDepths
    public PlatLot[][] getNeighborPlatLots(PlatMap platmap, int platX, int platZ, boolean onlyConnectedNeighbors) {
        PlatLot[][] miniPlatMap = new PlatLot[3][3];

        // populate the results
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {

                // which platchunk are we looking at?
                int atX = platX + x - 1;
                int atZ = platZ + z - 1;

                // is it in bounds?
                if (!(atX < 0 || atX > PlatMap.SIDE - 1 || atZ < 0 || atZ > PlatMap.SIDE - 1)) {
                    PlatLot relative = platmap.platLots[atX][atZ];

                    if (!onlyConnectedNeighbors || isConnected(relative)) {
                        miniPlatMap[x][z] = relative;
                    }
                }
            }
        }

        return miniPlatMap;
    }

    protected void generateBedrock(ByteChunk byteChunk, PlatMapContext context, int uptoY) {

        // bottom of the bottom
        byteChunk.setLayer(0, bedrockId);

        // draw the underworld
        if (context.doUnderworld) {

            // the pillars of the world
            for (int x = 0; x < ByteChunk.WIDTH; x++) {
                for (int z = 0; z < ByteChunk.WIDTH; z++) {
                    int x4 = x % 4;
                    int z4 = z % 4;
                    if ((x4 == 0 || x4 == 3) && (z4 == 0 || z4 == 3)) {
                        byteChunk.setBlocks(x, 1, uptoY - 1, z, context.isolationId);
                    } else if (rand.nextBoolean()) {
                        if (rand.nextInt(context.oddsOfLavaDownBelow) == 0) {
                            byteChunk.setBlock(x, 1, z, lavaId);
                        } else {
                            byteChunk.setBlock(x, 1, z, context.isolationId);
                        }
                    }
                }
            }

            // top of the bottom
            byteChunk.setLayer(uptoY - 1, context.isolationId);
        } else {

            // back fill with stone
            byteChunk.setLayer(1, uptoY - 1, stoneId);
        }
    }

}
