package net.voxton.voxtongen.context;

import java.util.Random;
import net.voxton.voxtongen.GenerationSettings;

import net.voxton.voxtongen.VoxtonGen;
import net.voxton.voxtongen.chunk.RealChunk;

public class PlatMapContext {
    public static int oddsNeverGoingToHappen = Integer.MAX_VALUE;

    public static int oddsExtremelyUnlikely = 80;

    public static int oddsVeryUnlikely = 40;

    public static int oddsUnlikely = 20;

    public static int oddsLikely = 10;

    public static int oddsVeryLikely = 5;

    public static int oddsExtremelyLikely = 3;

    public static int oddsAlwaysGoingToHappen = 1;

    public int oddsOfIsolatedLots = oddsExtremelyLikely; // isolated buildings 1/n of the time

    public int oddsOfParks = oddsVeryLikely; // parks show up 1/n of the time

    public int maximumFloorsAbove;

    public int maximumFloorsBelow;

    public int oddsOfIdenticalBuildingHeights = oddsExtremelyLikely; // similar height 1/n of the time

    public int oddsOfSimilarBuildingHeights = oddsExtremelyLikely; // identical height 1/n of the time

    public int oddsOfSimilarBuildingRounding = oddsExtremelyLikely; // like rounding 1/n of the time

    public int oddsOfStairWallMaterialIsWallMaterial = oddsExtremelyLikely; // stair walls are the same as walls 1/n of the time

    public int buildingWallInsettedMinLowPoint; // minimum building height before insetting is allowed

    public int buildingWallInsettedMinMidPoint; // lowest point of inset

    public int buildingWallInsettedMinHighPoint; // lowest highest point of inset

    public int oddsOfUnfinishedBuildings = oddsLikely; // buildings are unfinished 1/n of the time

    public int oddsOfOnlyUnfinishedBasements = oddsVeryLikely; // unfinished buildings only have basements 1/n of the time

    public int oddsOfCranes = oddsVeryLikely; // plop a crane on top of the last horizontal girder 1/n of the time

    public int oddsOfBuildingWallInset = oddsExtremelyLikely; // building walls inset as they go up 1/n of the time

    public int oddsOfSimilarInsetBuildings = oddsExtremelyLikely; // the east/west inset is used for north/south inset 1/n of the time

    public int rangeOfWallInset = 2; // 1 or 2 in... but not zero

    public int oddsOfFlatWalledBuildings = oddsExtremelyLikely; // the ceilings are inset like the walls 1/n of the time

    public int oddsOfPlumbingConnection = oddsExtremelyLikely;

    public int oddsOfPlumbingTreasure = oddsVeryLikely;

    public int oddsOfSewerVines = oddsUnlikely;

    public int oddsOfSewerTreasure = oddsExtremelyLikely;

    public int oddsOfSewerTrick = oddsExtremelyLikely;

    public int maxTreasureCount = 5;

    public int oddsOfMissingRoad = oddsLikely; // roads are missing 1/n of the time

    public int oddsOfRoundAbouts = oddsLikely; // roundabouts are created 1/n of the time

    public int oddsOfMoneyInFountains = oddsLikely; // gold is in the fountain 1/n of the time

    public int oddsOfMissingArt = oddsUnlikely; // art is missing 1/n of the time

    public int oddsOfLavaDownBelow = oddsUnlikely; // how often does lava show up in the underworld 1/n of the time

    public int oddsOfManholeToDownBelow = oddsExtremelyLikely; // manhole/ladder down to the lowest levels 1/n of the time

    public int oddsOfNaturalArt = oddsExtremelyLikely; // sometimes nature is art 1/n of the time 

    public static final int floorHeight = 4;

    public static final int FudgeFloorsBelow = 2;

    public static final int FudgeFloorsAbove = 3;

    public static final int absoluteMinimumFloorsAbove = 5; // shortest tallest building

    public static final int absoluteAbsoluteMaximumFloorsBelow = 3; // that is as many basements as I can tolerate

    public int absoluteMaximumFloorsBelow;

    public int absoluteMaximumFloorsAbove;

    public int streetLevel;

    public byte isolationId;

    public boolean doPlumbing;

    public boolean doSewer;

    public boolean doCistern;

    public boolean doBasement;

    public boolean doUnderworld;

    public boolean doTreasureInSewer;

    public boolean doTreasureInPlumbing;

    public boolean doTreasureInFountain;

    public boolean doSpawnerInSewer;

    private final Random random;

    public PlatMapContext(VoxtonGen plugin, Random rand) {
        this.random = rand;

        GenerationSettings settings = plugin.getSettings();
        isolationId = (byte) settings.getIsolationMaterial().getId();
        doPlumbing = settings.isDoPlumbing();
        doSewer = settings.isDoSewer();
        doCistern = settings.isDoCistern();
        doBasement = settings.isDoBasement();
        doUnderworld = settings.isDoUnderworld();
        doTreasureInSewer = settings.isDoTreasureInSewer();
        doTreasureInPlumbing = settings.isDoTreasureInPlumbing();
        doTreasureInFountain = settings.isDoTreasureInFountain();
        doSpawnerInSewer = settings.isDoSpawnerInSewer();

        // where is the ground
        streetLevel = Math.min(Math.max(settings.getStreetLevel(),
                floorHeight * FudgeFloorsBelow),
                RealChunk.Height - floorHeight * (FudgeFloorsAbove + absoluteMinimumFloorsAbove));

        // worst case?
        absoluteMaximumFloorsBelow = Math.max(Math.min(streetLevel / floorHeight - FudgeFloorsBelow, absoluteAbsoluteMaximumFloorsBelow), 0);
        absoluteMaximumFloorsAbove = Math.max(Math.min((RealChunk.Height - streetLevel) / floorHeight - FudgeFloorsAbove, settings.getMaximumFloors()), absoluteMinimumFloorsAbove);

        // turn off a few things if there isn't room
        if (absoluteMaximumFloorsBelow == 0) {
            doPlumbing = false;
            doSewer = false;
            doCistern = false;
            doBasement = false;
        }

        // default floor range
        setFloorRange(rand, 2, 2);
    }

    /**
     * Random objects
     * 
     * @return 
     */
    public Random getRandom() {
        return random;
    }

    protected void setFloorRange(Random rand, int aboveRange, int belowRange) {
        // calculate the extremes for this plat
        maximumFloorsAbove = Math.min((rand.nextInt(aboveRange) + 1) * 2, absoluteMaximumFloorsAbove);
        maximumFloorsBelow = Math.min(rand.nextInt(belowRange) + 1, absoluteMaximumFloorsBelow);

        int floorsFourth = Math.max((maximumFloorsAbove) / 4, 1);
        buildingWallInsettedMinLowPoint = floorsFourth;
        buildingWallInsettedMinMidPoint = floorsFourth * 2;
        buildingWallInsettedMinHighPoint = floorsFourth * 3;
    }

}
