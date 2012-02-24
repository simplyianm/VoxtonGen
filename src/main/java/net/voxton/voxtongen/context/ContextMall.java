package net.voxton.voxtongen.context;

import java.util.Random;

import net.voxton.voxtongen.VoxtonGen;

public class ContextMall extends PlatMapContext {
    public ContextMall(VoxtonGen plugin, Random rand) {
        super(plugin, rand);

        setFloorRange(rand, 2, 1);

        oddsOfParks = oddsUnlikely;
        oddsOfIsolatedLots = oddsNeverGoingToHappen;
        oddsOfIdenticalBuildingHeights = oddsAlwaysGoingToHappen;
        oddsOfSimilarBuildingHeights = oddsExtremelyLikely;
        oddsOfSimilarBuildingRounding = oddsExtremelyLikely;
        oddsOfUnfinishedBuildings = oddsNeverGoingToHappen;
        oddsOfOnlyUnfinishedBasements = oddsNeverGoingToHappen;
        oddsOfMissingRoad = oddsNeverGoingToHappen;
        oddsOfRoundAbouts = oddsUnlikely;

        oddsOfStairWallMaterialIsWallMaterial = oddsExtremelyLikely;
        oddsOfBuildingWallInset = oddsExtremelyLikely;
        oddsOfFlatWalledBuildings = oddsExtremelyLikely;
        oddsOfSimilarInsetBuildings = oddsExtremelyLikely;
        rangeOfWallInset = 2;
    }

}
