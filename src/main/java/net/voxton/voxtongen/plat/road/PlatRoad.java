package net.voxton.voxtongen.plat.road;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.plat.PlatLot;

public abstract class PlatRoad extends PlatLot {
    public static final int PlatMapRoadInset = 3;

    public PlatRoad(Random rand, PlatMapContext context) {
        super(rand, context);

        // is there more to life?
    }

}
