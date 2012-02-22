package net.voxton.voxtongen.PlatMaps;

import java.util.Random;

import net.voxton.voxtongen.Context.PlatMapContext;
import net.voxton.voxtongen.Plats.PlatBiome;
import net.voxton.voxtongen.Plats.PlatLot;

import org.bukkit.World;

public class PlatMapVanilla extends PlatMap {

	public PlatMapVanilla(World world, Random random, PlatMapContext context, int platX, int platZ) {
		super(world, random, context, platX, platZ);

		for (int x = 0; x < Width; x++) {
			for (int z = 0; z < Width; z++) {
				PlatLot current = platLots[x][z];
				if (current == null) {
					platLots[x][z] = new PlatBiome(random, context);
				}
			}
		}
	}

}
