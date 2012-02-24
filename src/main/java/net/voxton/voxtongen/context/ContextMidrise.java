package net.voxton.voxtongen.context;

import java.util.Random;

import net.voxton.voxtongen.VoxtonGen;

public class ContextMidrise extends PlatMapContext {
    public ContextMidrise(VoxtonGen plugin, Random rand) {
        super(plugin, rand);

        setFloorRange(rand, 7, 3);

        oddsOfParks = oddsUnlikely;
        oddsOfIsolatedLots = oddsLikely;
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
