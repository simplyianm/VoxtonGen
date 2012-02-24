package net.voxton.voxtongen.context;

import java.util.Random;

import net.voxton.voxtongen.VoxtonGen;

public class ContextLowrise extends PlatMapContext {
    public ContextLowrise(VoxtonGen plugin, Random rand) {
        super(plugin, rand);

        setFloorRange(rand, 3, 1);

        oddsOfParks = oddsLikely;
        oddsOfIsolatedLots = oddsVeryLikely;
        oddsOfIdenticalBuildingHeights = oddsExtremelyLikely;
        oddsOfSimilarBuildingHeights = oddsExtremelyLikely;
        oddsOfSimilarBuildingRounding = oddsExtremelyLikely;
        oddsOfUnfinishedBuildings = oddsVeryUnlikely;
        oddsOfOnlyUnfinishedBasements = oddsNeverGoingToHappen;
        oddsOfMissingRoad = oddsLikely;
        oddsOfRoundAbouts = oddsLikely;

        oddsOfStairWallMaterialIsWallMaterial = oddsExtremelyLikely;
        oddsOfBuildingWallInset = oddsExtremelyLikely;
        oddsOfFlatWalledBuildings = oddsExtremelyLikely;
        oddsOfSimilarInsetBuildings = oddsExtremelyLikely;
        rangeOfWallInset = 2;
    }

}
