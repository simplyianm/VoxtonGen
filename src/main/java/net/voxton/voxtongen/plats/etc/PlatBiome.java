package net.voxton.voxtongen.plats.etc;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.chunk.ByteChunk;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.plats.PlatType;

import org.bukkit.Material;
import org.bukkit.block.Biome;

public class PlatBiome extends PlatLot {
    protected final static byte stoneId = (byte) Material.STONE.getId();

    public PlatBiome(PlatMapContext context) {
        super(context.getRandom(), context);
    }

    @Override
    public void generateChunk(PlatMap platmap, ByteChunk byteChunk, PlatMapContext context, int platX, int platZ) {

        byteChunk.setLayer(0, context.streetLevel + 1, stoneId);

        Biome biome = platmap.theWorld.getBiome(byteChunk.x, byteChunk.z);
        int tens = biome.ordinal() / 10;
        int ones = biome.ordinal() % 10;
        byteChunk.drawCoordinate(tens, ones, context.streetLevel + 1, (platX == 0 && platZ == 0));
    }

    @Override
    public PlatType getType() {
        return PlatType.BIOME;
    }

}
