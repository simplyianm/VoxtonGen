package net.voxton.voxtongen.context;

import java.util.Random;

import net.voxton.voxtongen.CityWorld;

public class ContextAllPark extends PlatMapContext {

	public ContextAllPark(CityWorld plugin, Random rand) {
		super(plugin, rand);

		setFloorRange(rand, 2, 4);
		
		oddsOfParks = oddsAlwaysGoingToHappen;
		oddsOfIsolatedLots = oddsExtremelyLikely;
		oddsOfIdenticalBuildingHeights = oddsExtremelyLikely;
		oddsOfSimilarBuildingHeights = oddsExtremelyLikely;
		oddsOfSimilarBuildingRounding = oddsExtremelyLikely;
		oddsOfUnfinishedBuildings = oddsExtremelyLikely;
		oddsOfOnlyUnfinishedBasements = oddsLikely;
		oddsOfMissingRoad = oddsLikely;
		oddsOfRoundAbouts = oddsVeryLikely;
		 
		oddsOfStairWallMaterialIsWallMaterial = oddsExtremelyLikely;
		oddsOfBuildingWallInset = oddsExtremelyLikely;
		oddsOfFlatWalledBuildings = oddsExtremelyLikely;
		oddsOfSimilarInsetBuildings = oddsExtremelyLikely;
		rangeOfWallInset = 2;
	}

}
