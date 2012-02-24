package net.voxton.voxtongen.gen;

import java.util.Random;
import net.voxton.voxtongen.VoxtonGen;

import net.voxton.voxtongen.platmaps.PlatMap;
import net.voxton.voxtongen.chunk.RealChunk;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class VBlockPopulator extends BlockPopulator {
    private VoxtonGen plugin;

    public VBlockPopulator(VoxtonGen instance) {
        this.plugin = instance;
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        int chunkX = source.getX();
        int chunkZ = source.getZ();

        // place to work
        RealChunk chunk = new RealChunk(source);

        // figure out what everything looks like
        PlatMap platmap = plugin.getPlatMap(world, random, chunkX, chunkZ);
        if (platmap != null) {
            platmap.generateBlocks(chunk);
        }
    }

}
