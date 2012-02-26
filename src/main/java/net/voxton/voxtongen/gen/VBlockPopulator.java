package net.voxton.voxtongen.gen;

import java.util.Random;
import net.voxton.voxtongen.VoxtonGen;

import net.voxton.voxtongen.platmap.PlatMap;
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
        //Workable chunk
        RealChunk chunk = new RealChunk(source);

        //Get the platmap
        PlatMap platmap = plugin.getPlatMap(source, random);
        if (platmap != null) {
            platmap.generateBlocks(chunk);
        }
    }

}
