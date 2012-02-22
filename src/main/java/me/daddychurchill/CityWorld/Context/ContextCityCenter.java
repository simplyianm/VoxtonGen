package me.daddychurchill.CityWorld.Context;

import java.util.Random;

import me.daddychurchill.CityWorld.CityWorld;

public class ContextCityCenter extends PlatMapContext {

	public ContextCityCenter(CityWorld plugin, Random rand) {
		super(plugin, rand);

		setFloorRange(rand, 5, 2);
		
		oddsOfParks = oddsLikely;
		oddsOfIsolatedLots = oddsVeryLikely;
		oddsOfIdenticalBuildingHeights = oddsAlwaysGoingToHappen;
		oddsOfSimilarBuildingHeights = oddsAlwaysGoingToHappen;
		oddsOfSimilarBuildingRounding = oddsAlwaysGoingToHappen;
		oddsOfUnfinishedBuildings = oddsNeverGoingToHappen;
		oddsOfOnlyUnfinishedBasements = oddsLikely;
		oddsOfMissingRoad = oddsNeverGoingToHappen;
		oddsOfRoundAbouts = oddsAlwaysGoingToHappen;
		 
		oddsOfStairWallMaterialIsWallMaterial = oddsAlwaysGoingToHappen;
		oddsOfFlatWalledBuildings = oddsVeryLikely;
		oddsOfSimilarInsetBuildings = oddsVeryLikely;
		oddsOfBuildingWallInset = oddsVeryLikely;
		rangeOfWallInset = 1;
	}

}
