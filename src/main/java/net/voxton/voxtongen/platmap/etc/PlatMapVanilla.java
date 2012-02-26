package net.voxton.voxtongen.platmap.etc;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.plats.PlatBiome;
import net.voxton.voxtongen.plats.PlatLot;

import org.bukkit.World;

public class PlatMapVanilla extends PlatMap {
    public PlatMapVanilla(World world, PlatMapContext context, int platX, int platZ) {
        super(world, context, platX, platZ);

        for (int x = 0; x < SIDE; x++) {
            for (int z = 0; z < SIDE; z++) {
                PlatLot current = platLots[x][z];
                if (current == null) {
                    platLots[x][z] = new PlatBiome(context);
                }
            }
        }
    }

}
