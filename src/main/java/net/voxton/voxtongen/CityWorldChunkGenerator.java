package net.voxton.voxtongen;

import java.util.*;

import net.voxton.voxtongen.context.ContextAllPark;
import net.voxton.voxtongen.context.ContextCityCenter;
import net.voxton.voxtongen.context.ContextHighrise;
import net.voxton.voxtongen.context.ContextLowrise;
import net.voxton.voxtongen.context.ContextMall;
import net.voxton.voxtongen.context.ContextMidrise;
import net.voxton.voxtongen.context.ContextUnfinished;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmaps.PlatMap;
import net.voxton.voxtongen.platmaps.PlatMapVanilla;
import net.voxton.voxtongen.platmaps.city.PlatMapCentralPark;
import net.voxton.voxtongen.platmaps.city.PlatMapSkyscrapers;
import net.voxton.voxtongen.platmaps.city.PlatMapTown;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.support.ByteChunk;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class CityWorldChunkGenerator extends ChunkGenerator {
    private CityWorld plugin;

    public String worldname;

    public String worldstyle;

    public CityWorldChunkGenerator(CityWorld instance, String name, String style) {
        this.plugin = instance;
        this.worldname = name;
        this.worldstyle = style;
    }

    public CityWorld getWorld() {
        return plugin;
    }

    public String getWorldname() {
        return worldname;
    }

    public String getWorldstyle() {
        return worldstyle;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator) new CityWorldBlockPopulator(this));
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        // see if this works any better (loosely based on ExpansiveTerrain)
        int x = random.nextInt(100) - 50;
        int z = random.nextInt(100) - 50;
        //int y = Math.max(world.getHighestBlockYAt(x, z), PlatMap.StreetLevel + 1);
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }

    @Override
    public byte[] generate(World world, Random random, int chunkX, int chunkZ) {

        // place to work
        ByteChunk byteChunk = new ByteChunk(chunkX, chunkZ);

        // figure out what everything looks like
        PlatMap platmap = getPlatMap(world, random, chunkX, chunkZ);
        if (platmap != null) {
            platmap.generateChunk(byteChunk);
        }

        return byteChunk.blocks;
    }

    // this function is designed for BlockPopulators...
    //    calling it else where will likely result in nulls being returned
    public PlatLot getPlatLot(World world, Random random, int chunkX, int chunkZ) {
        PlatLot platlot = null;

        // try and find the lot handler for this chunk
        PlatMap platmap = getPlatMap(world, random, chunkX, chunkZ);
        if (platmap != null) {

            // calculate the right index
            int platX = chunkX - platmap.X;
            int platZ = chunkZ - platmap.Z;

            // see if there is something there yet
            platlot = platmap.platLots[platX][platZ];
        }

        // return what we got
        return platlot;
    }

    // ***********
    // manager for handling the city plat maps collection
    private double xFactor = 25.0;

    private double zFactor = 25.0;

    private SimplexNoiseGenerator generatorUrban;

    private SimplexNoiseGenerator generatorWater;

    private SimplexNoiseGenerator generatorUnfinished;

    private HashMap<Long, PlatMap> platmaps;

    public PlatMap getPlatMap(World world, Random random, int chunkX, int chunkZ) {

        // get the plat map collection
        if (platmaps == null) {
            platmaps = new HashMap<Long, PlatMap>();
        }

        // find the origin for the plat
        int platX = calcOrigin(chunkX);
        int platZ = calcOrigin(chunkZ);

        // calculate the plat's key
        Long platkey = Long.valueOf(((long) platX * (long) Integer.MAX_VALUE + (long) platZ));

        // get the right plat
        PlatMap platmap = platmaps.get(platkey);

        // doesn't exist? then make it!
        if (platmap == null) {
            platmap = getMap(world, random, chunkX, chunkZ, platX, platZ);
            platmaps.put(platkey, platmap);
        }

        // finally return the plat
        return platmap;
    }

    private PlatMap getMap(World world, Random random, int chunkX, int chunkZ, int platX, int platZ) {
        PlatMap platmap = null;

        // generator generated?
        if (generatorUrban == null) {
            long seed = world.getSeed();
            generatorUrban = new SimplexNoiseGenerator(seed);
            generatorWater = new SimplexNoiseGenerator(seed + 1);
            generatorUnfinished = new SimplexNoiseGenerator(seed + 2);
        }

//			int platX
//			CityWorld.log.info("PlatMapAt: " + platX / PlatMap.Width + ", " + platZ / PlatMap.Width + " OR " + chunkX + ", " + chunkZ);

        // what is the context for this one?
        PlatMapContext context = getContext(world, plugin, random, chunkX, chunkZ);

        // figure out the biome for this platmap
        switch (world.getBiome(platX, platZ)) {
            /**
             * Boring!
             */
            case HELL:
            case SKY:
                platmap = new PlatMapVanilla(world, random, context, platX, platZ);
                break;

            case DESERT:			// industrial zone
            case EXTREME_HILLS:		// tall city
            case FOREST:			// neighborhood
            case FROZEN_OCEAN:		// winter ocean/lake side
            case FROZEN_RIVER:		// ???
            case ICE_DESERT:		// stark industrial zone
            case ICE_MOUNTAINS:		// stark tall city
                platmap = new PlatMapSkyscrapers(world, random, context, platX, platZ);
                break;

            case ICE_PLAINS:		// apartments
            case MUSHROOM_ISLAND:	// ???
            case MUSHROOM_SHORE:	// ???
            case OCEAN:				// ocean/lake side
            case PLAINS:			// farm land
            case RAINFOREST:		// ???
            case RIVER:				// ???
            case SAVANNA:			// town
                platmap = new PlatMapTown(world, random, context, platX, platZ);
                break;

            case SEASONAL_FOREST:	// ???
            case SHRUBLAND:			// ???
            case SWAMPLAND:			// government
            case TAIGA:				// ???
            case TUNDRA:			// recreation
            default:
                platmap = new PlatMapCentralPark(world, random, context, platX, platZ);
                break;
        }

        return platmap;
    }

    private PlatMapContext getContext(World world, CityWorld plugin, Random random, int chunkX, int chunkZ) {
        switch (random.nextInt(20)) {
            case 0:
            case 1:
            case 2:
            case 3:
                return new ContextLowrise(plugin, random);
            case 4:
            case 5:
            case 6:
            case 7:
                return new ContextMidrise(plugin, random);
            case 8:
            case 9:
            case 10:
                return new ContextHighrise(plugin, random);
            case 11:
            case 12:
                return new ContextAllPark(plugin, random);
            case 13:
            case 14:
                return new ContextMall(plugin, random);
            case 15:
            case 16:
            case 17:
                return new ContextCityCenter(plugin, random);
            case 18:
            case 19:
            default:
                return new ContextUnfinished(plugin, random);
        }
    }

    // Supporting code used by getPlatMap
    private int calcOrigin(int i) {
        if (i >= 0) {
            return i / PlatMap.SIDE * PlatMap.SIDE;
        } else {
            return -((Math.abs(i + 1) / PlatMap.SIDE * PlatMap.SIDE) + PlatMap.SIDE);
        }
    }

}
