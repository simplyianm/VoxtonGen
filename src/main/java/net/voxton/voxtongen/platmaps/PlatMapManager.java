/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmaps;

import java.util.HashMap;
import java.util.Random;
import net.voxton.voxtongen.VoxtonGen;
import net.voxton.voxtongen.VoxtonGen;
import net.voxton.voxtongen.context.*;
import net.voxton.voxtongen.platmaps.PlatMap;
import net.voxton.voxtongen.platmaps.PlatMapVanilla;
import net.voxton.voxtongen.platmaps.city.PlatMapCentralPark;
import net.voxton.voxtongen.platmaps.city.PlatMapMegaScrapers;
import net.voxton.voxtongen.platmaps.city.PlatMapSkyscrapers;
import net.voxton.voxtongen.platmaps.city.PlatMapTown;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

/**
 *
 * @author simplyianm
 */
public class PlatMapManager {
    private VoxtonGen plugin;
    
    private SimplexNoiseGenerator generatorUrban;

    private SimplexNoiseGenerator generatorWater;

    private SimplexNoiseGenerator generatorUnfinished;

    private HashMap<Long, PlatMap> platmaps;

    public PlatMapManager(VoxtonGen plugin) {
        this.plugin = plugin;
    }

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
            platmap = makePlatMap(world, random, chunkX, chunkZ, platX, platZ);
            platmaps.put(platkey, platmap);
        }

        // finally return the plat
        return platmap;
    }

    private PlatMap makePlatMap(World world, Random random, int chunkX, int chunkZ, int platX, int platZ) {
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
                platmap = new PlatMapMegaScrapers(world, random, context, platX, platZ);
                break;

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

    private PlatMapContext getContext(World world, VoxtonGen plugin, Random random, int chunkX, int chunkZ) {
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
