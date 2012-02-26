package net.voxton.voxtongen.gen;

import java.util.*;
import net.voxton.voxtongen.VoxtonGen;

import net.voxton.voxtongen.context.ContextAllPark;
import net.voxton.voxtongen.context.ContextCityCenter;
import net.voxton.voxtongen.context.ContextHighrise;
import net.voxton.voxtongen.context.ContextLowrise;
import net.voxton.voxtongen.context.ContextMall;
import net.voxton.voxtongen.context.ContextMidrise;
import net.voxton.voxtongen.context.ContextUnfinished;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.platmap.etc.PlatMapVanilla;
import net.voxton.voxtongen.platmap.city.PlatMapCentralPark;
import net.voxton.voxtongen.platmap.city.PlatMapMegaScrapers;
import net.voxton.voxtongen.platmap.city.PlatMapSkyscrapers;
import net.voxton.voxtongen.platmap.city.PlatMapTown;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.chunk.ByteChunk;
import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class VChunkGenerator extends ChunkGenerator {
    private VoxtonGen plugin;

    public String worldname;

    public String worldstyle;

    public VChunkGenerator(VoxtonGen instance, String name, String style) {
        this.plugin = instance;
        this.worldname = name;
        this.worldstyle = style;
    }

    public String getWorldname() {
        return worldname;
    }

    public String getWorldstyle() {
        return worldstyle;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator) new VBlockPopulator(plugin));
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
        PlatMap platmap = plugin.getPlatMap(world, chunkX, chunkZ, random);
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
        PlatMap platmap = plugin.getPlatMap(world, chunkX, chunkZ, random);
        if (platmap != null) {

            // calculate the right index
            int platX = chunkX - platmap.x;
            int platZ = chunkZ - platmap.z;

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

}
