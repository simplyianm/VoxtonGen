package net.voxton.voxtongen.gen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.voxton.voxtongen.VoxtonGen;

import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.plat.PlatLot;
import net.voxton.voxtongen.chunk.ByteChunk;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

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
