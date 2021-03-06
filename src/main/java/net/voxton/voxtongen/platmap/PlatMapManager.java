/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap;

import java.util.HashMap;
import java.util.Random;
import net.voxton.voxtongen.VoxtonGen;
import net.voxton.voxtongen.context.*;
import net.voxton.voxtongen.platmap.etc.PlatMapVanilla;
import net.voxton.voxtongen.platmap.city.PlatMapCentralPark;
import net.voxton.voxtongen.platmap.city.PlatMapMegaScrapers;
import net.voxton.voxtongen.platmap.city.PlatMapSkyscrapers;
import net.voxton.voxtongen.platmap.city.PlatMapTown;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

/**
 *
 * @author simplyianm
 */
public class PlatMapManager {
    private VoxtonGen plugin;

    private PlatMapCache platmaps;

    private MapMatrix matrix;

    private PlatMapFactory factory;

    public PlatMapManager(VoxtonGen plugin) {
        this.plugin = plugin;

        factory = new PlatMapFactory();
        setupMatrix();
    }

    /**
     * Sets up the matrix for plat maps.
     */
    private void setupMatrix() {
        matrix = new MapMatrix(6);

        matrix.set(0, 0, MapType.CENTRAL_PARK);
        matrix.set(0, 1, MapType.ONE_FLOOR);
        matrix.set(0, 2, MapType.ONE_FLOOR);
        matrix.set(0, 3, MapType.ONE_FLOOR);
        matrix.set(0, 4, MapType.ONE_FLOOR);
        matrix.set(0, 5, MapType.CENTRAL_PARK);

        matrix.set(1, 0, MapType.ONE_FLOOR);
        matrix.set(1, 1, MapType.SKYSCRAPERS);
        matrix.set(1, 2, MapType.TOWN);
        matrix.set(1, 3, MapType.TOWN);
        matrix.set(1, 4, MapType.SKYSCRAPERS);
        matrix.set(1, 5, MapType.ONE_FLOOR);

        matrix.set(2, 0, MapType.CENTRAL_PARK);
        matrix.set(2, 1, MapType.TOWN);
        matrix.set(2, 2, MapType.MEGASCRAPERS);
        matrix.set(2, 3, MapType.CENTRAL_PARK);
        matrix.set(2, 4, MapType.TOWN);
        matrix.set(2, 5, MapType.CENTRAL_PARK);

        matrix.set(3, 0, MapType.CENTRAL_PARK);
        matrix.set(3, 1, MapType.TOWN);
        matrix.set(3, 2, MapType.CENTRAL_PARK);
        matrix.set(3, 3, MapType.MEGASCRAPERS);
        matrix.set(3, 4, MapType.TOWN);
        matrix.set(3, 5, MapType.CENTRAL_PARK);

        matrix.set(4, 0, MapType.ONE_FLOOR);
        matrix.set(4, 1, MapType.SKYSCRAPERS);
        matrix.set(4, 2, MapType.TOWN);
        matrix.set(4, 3, MapType.TOWN);
        matrix.set(4, 4, MapType.SKYSCRAPERS);
        matrix.set(4, 5, MapType.ONE_FLOOR);

        matrix.set(5, 0, MapType.CENTRAL_PARK);
        matrix.set(5, 1, MapType.ONE_FLOOR);
        matrix.set(5, 2, MapType.ONE_FLOOR);
        matrix.set(5, 3, MapType.ONE_FLOOR);
        matrix.set(5, 4, MapType.ONE_FLOOR);
        matrix.set(5, 5, MapType.CENTRAL_PARK);
    }

    /**
     * Gets the PlatMap associated with the given chunk.
     *
     * @param chunk
     * @param random
     * @return
     */
    public PlatMap getPlatMap(Chunk chunk, Random random) {
        return getPlatMap(chunk.getWorld(), chunk.getX(), chunk.getZ(), random);
    }

    /**
     * Gets the PlatMap associated with the given coordinates.
     *
     * @param world
     * @param cx
     * @param cz
     * @param random
     * @return
     */
    public PlatMap getPlatMap(World world, int cx, int cz, Random random) {
        //Verify the platmap hashmap has been created
        if (platmaps == null) {
            platmaps = new PlatMapCache();
        }

        // find the origin for the plat
        int mapX = getPlatMapOrigin(cx);
        int mapZ = getPlatMapOrigin(cz);

        // calculate the plat's key
        Long platkey = Long.valueOf(((long) mapX * (long) Integer.MAX_VALUE + (long) mapZ));

        // get the right plat
        PlatMap platmap = platmaps.get(platkey);

        // doesn't exist? then make it!
        if (platmap == null) {
            platmap = findPlatMapFromMatrix(world, random, mapX, mapZ);
            platmaps.put(platkey, platmap);
        }

        // finally return the plat
        return platmap;
    }

    private PlatMap findPlatMapFromMatrix(World world, Random random, int mapX, int mapZ) {
        PlatMapContext context = getContext(random);
        MapType type = matrix.getMapType(mapX, mapZ);
        return factory.makePlatMap(world, context, mapX, mapZ, type);
    }

    private PlatMap findPlatMapFromBiome(World world, Random random, int mapX, int mapZ) {
        PlatMap platmap = null;

        // what is the context for this one?
        PlatMapContext context = getContext(random);

        // figure out the biome for this platmap
        switch (world.getBiome(mapX, mapZ)) {
            /**
             * Boring!
             */
            case HELL:
            case SKY:
                platmap = new PlatMapVanilla(world, context, mapX, mapZ);
                break;

            case DESERT:			// industrial zone
            case EXTREME_HILLS:		// tall city
            case FOREST:			// neighborhood
                platmap = new PlatMapMegaScrapers(world, context, mapX, mapZ);
                break;

            case FROZEN_OCEAN:		// winter ocean/lake side
            case FROZEN_RIVER:		// ???
            case ICE_DESERT:		// stark industrial zone
            case ICE_MOUNTAINS:		// stark tall city
                platmap = new PlatMapSkyscrapers(world, context, mapX, mapZ);
                break;

            case ICE_PLAINS:		// apartments
            case MUSHROOM_ISLAND:	// ???
            case MUSHROOM_SHORE:	// ???
            case OCEAN:				// ocean/lake side
            case PLAINS:			// farm land
            case RAINFOREST:		// ???
            case RIVER:				// ???
            case SAVANNA:			// town
                platmap = new PlatMapTown(world, context, mapX, mapZ);
                break;

            case SEASONAL_FOREST:	// ???
            case SHRUBLAND:			// ???
            case SWAMPLAND:			// government
            case TAIGA:				// ???
            case TUNDRA:			// recreation
            default:
                platmap = new PlatMapCentralPark(world, context, mapX, mapZ);
                break;
        }

        return platmap;
    }

    private PlatMapContext getContext(Random random) {
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

    /**
     * Gets the origin of a PlatMap coordinate. (If a side were 16, then it
     * would get the closest multiple of 16 to the number.)
     *
     * @param platCoord
     * @return
     */
    private int getPlatMapOrigin(int platCoord) {
        if (platCoord >= 0) {
            return platCoord / PlatMap.SIDE * PlatMap.SIDE;
        } else {
            return -((Math.abs(platCoord + 1) / PlatMap.SIDE * PlatMap.SIDE) + PlatMap.SIDE);
        }
    }

}
