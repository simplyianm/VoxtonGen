package net.voxton.voxtongen.plats;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmaps.PlatMap;
import net.voxton.voxtongen.support.ByteChunk;

import org.bukkit.Material;
import org.bukkit.block.Biome;

public class PlatBiome extends PlatLot {

	protected final static byte stoneId = (byte) Material.STONE.getId();
	
	public PlatBiome(Random rand, PlatMapContext context) {
		super(rand, context);
	}

	@Override
	public void generateChunk(PlatMap platmap, ByteChunk byteChunk, PlatMapContext context, int platX, int platZ) {
		
		byteChunk.setLayer(0, context.streetLevel + 1, stoneId);
		
		Biome biome = platmap.theWorld.getBiome(byteChunk.X, byteChunk.Z);
		int tens = biome.ordinal() / 10;
		int ones = biome.ordinal() % 10;
		byteChunk.drawCoordinate(tens, ones, context.streetLevel + 1, (platX == 0 && platZ == 0));
	}

}
