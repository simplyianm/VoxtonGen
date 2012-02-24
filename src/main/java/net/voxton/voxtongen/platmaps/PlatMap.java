package net.voxton.voxtongen.platmaps;

import java.util.Random;
import java.util.logging.Logger;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.chunk.ByteChunk;
import net.voxton.voxtongen.chunk.RealChunk;

import org.bukkit.World;

public abstract class PlatMap {
    // debugging
    protected static Logger log = Logger.getLogger("Minecraft");

    /**
     * Amount of city blocks in the plat map
     */
    public static final int BLOCKS_ACROSS = 3;

    /**
     * Width of the city blocks
     */
    public static final int BLOCK_WIDTH = 4;
    
    /**
     * Width of a city block plus a road.
     */
    public static final int BLOCK_GRP_WIDTH = BLOCK_WIDTH + 1;

    /**
     * Half of the border around a plat map.
     */
    public static final int HALF_BORDER = 1;

    /**
     * Interior side
     */
    public static final int INT_SIDE = BLOCKS_ACROSS * BLOCK_WIDTH + BLOCKS_ACROSS - 1;

    /**
     * Length of an entire side of the plat map.
     */
    public static final int SIDE = INT_SIDE + 2 * HALF_BORDER;

    /**
     * End of the interior side.
     */
    public static final int INT_SIDE_END = SIDE - HALF_BORDER;

//	static final public int FloorHeight = 4;
//	static final public int FudgeFloorsBelow = 2;
//	static final public int FudgeFloorsAbove = 4;
//	static final public int AbsoluteMaximumFloorsBelow = 4;
//	static final public int StreetLevel = FloorHeight * (AbsoluteMaximumFloorsBelow + FudgeFloorsBelow);
//	static final public int AbsoluteMaximumFloorsAbove = (RealChunk.Height - StreetLevel) / FloorHeight - FudgeFloorsAbove; 
    // Instance data
    public World theWorld;

    public int X;

    public int Z;

    public Random platRand;

    public PlatMapContext context;

    public PlatLot[][] platLots;

    public PlatMap(World world, Random random, PlatMapContext context, int platX, int platZ) {
        super();
        // log.info(String.format("PM: %d x %d create", platX, platZ));

        // populate the instance data
        this.theWorld = world;
        this.context = context;
        this.X = platX;
        this.Z = platZ;
        this.platRand = new Random(world.getSeed() + (long) X * (long) SIDE + (long) Z);

        // make room for plat data
        platLots = new PlatLot[SIDE][SIDE];
    }

    public void generateChunk(ByteChunk chunk) {

        // depending on the platchunk's type render a layer
        int platX = chunk.X - X;
        int platZ = chunk.Z - Z;
        PlatLot platlot = platLots[platX][platZ];
        if (platlot != null) {

            // do what we came here for
            platlot.generateChunk(this, chunk, context, platX, platZ);
        }
    }

    public void generateBlocks(RealChunk chunk) {

        // depending on the platchunk's type render a layer
        int platX = chunk.X - X;
        int platZ = chunk.Z - Z;
        PlatLot platlot = platLots[platX][platZ];
        if (platlot != null) {

            // do what we came here for
            platlot.generateBlocks(this, chunk, context, platX, platZ);
        }
    }

}
