package net.voxton.voxtongen.context;

import java.util.Random;

import net.voxton.voxtongen.VoxtonGen;

public class ContextHighrise extends PlatMapContext {
    public ContextHighrise(VoxtonGen plugin, Random rand) {
        super(plugin, rand);

        setFloorRange(rand, 11, 4);

        oddsOfParks = oddsNeverGoingToHappen;
        oddsOfIsolatedLots = oddsLikely;
        oddsOfIdenticalBuildingHeights = oddsExtremelyLikely;
        oddsOfSimilarBuildingHeights = oddsExtremelyLikely;
        oddsOfSimilarBuildingRounding = oddsExtremelyLikely;
        oddsOfUnfinishedBuildings = oddsVeryUnlikely;
        oddsOfOnlyUnfinishedBasements = oddsNeverGoingToHappen;
        oddsOfMissingRoad = oddsNeverGoingToHappen;
        oddsOfRoundAbouts = oddsNeverGoingToHappen;

        oddsOfStairWallMaterialIsWallMaterial = oddsExtremelyLikely;
        oddsOfBuildingWallInset = oddsExtremelyLikely;
        oddsOfFlatWalledBuildings = oddsExtremelyLikely;
        oddsOfSimilarInsetBuildings = oddsExtremelyLikely;
        rangeOfWallInset = 1;
    }

}
