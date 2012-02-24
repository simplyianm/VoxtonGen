package net.voxton.voxtongen.plats.building;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmaps.PlatMap;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.support.ByteChunk;
import net.voxton.voxtongen.support.Direction;
import net.voxton.voxtongen.support.Direction.StairWell;
import net.voxton.voxtongen.support.Direction.Torch;
import net.voxton.voxtongen.support.MaterialFactory;
import net.voxton.voxtongen.support.GlassFactoryEW;
import net.voxton.voxtongen.support.GlassFactoryNS;
import net.voxton.voxtongen.support.RealChunk;
import net.voxton.voxtongen.support.SurroundingFloors;
import net.voxton.voxtongen.support.Direction.Door;

import org.bukkit.Material;

public abstract class PlatBuilding extends PlatLot {
    protected boolean neighborsHaveIdenticalHeights;

    protected int neighborsHaveSimilarHeightsOdds;

    protected int neighborsHaveSimilarRoundedOdds;

    protected int height; // floors up

    protected int depth; // floors down

    protected boolean needStairsUp;

    protected boolean needStairsDown;

    protected boolean rounded; // rounded corners if possible? (only if the insets match)

    protected MaterialFactory windowsEW;

    protected MaterialFactory windowsNS;

    protected final static byte airId = (byte) Material.AIR.getId();

    protected final static byte doubleSlabId = (byte) Material.DOUBLE_STEP.getId();

    protected final static byte antennaId = (byte) Material.FENCE.getId();

    protected final static byte lightId = (byte) Material.GLOWSTONE.getId();

    protected final static byte conditionerId = (byte) Material.ENDER_PORTAL_FRAME.getId();

    protected final static Material tileMaterial = Material.STEP;

    public enum RoofStyle {
        FLATTOP, EDGED, PEAK, TENTNS, TENTEW

    };

    public enum RoofFeature {
        ANTENNAS, CONDITIONERS, TILE

    };

    protected RoofStyle roofStyle;

    protected RoofFeature roofFeature;

    protected int roofScale;

    protected int navLightX = 0;

    protected int navLightY = 0;

    protected int navLightZ = 0;

    public PlatBuilding(Random rand, PlatMapContext context) {
        super(rand, context);

        neighborsHaveIdenticalHeights = rand.nextInt(context.oddsOfIdenticalBuildingHeights) == 0;
        neighborsHaveSimilarHeightsOdds = context.oddsOfIdenticalBuildingHeights;
        neighborsHaveSimilarRoundedOdds = context.oddsOfSimilarBuildingRounding;
        height = rand.nextInt(context.maximumFloorsAbove) + 1;
        if (context.doBasement) {
            depth = rand.nextInt(context.maximumFloorsBelow) + 1;
        }
        needStairsDown = true;
        needStairsUp = true;
        rounded = rand.nextInt(context.oddsOfSimilarBuildingRounding) == 0;
        roofStyle = pickRoofStyle(rand);
        roofFeature = pickRoofFeature(rand);
        roofScale = rand.nextInt(2) + 1;
        windowsEW = new GlassFactoryEW(rand);
        windowsNS = new GlassFactoryNS(rand, windowsEW.style);
    }

    static public RoofStyle pickRoofStyle(Random rand) {
        switch (rand.nextInt(5)) {
            case 1:
                return RoofStyle.EDGED;
            case 2:
                return RoofStyle.PEAK;
            case 3:
                return RoofStyle.TENTNS;
            case 4:
                return RoofStyle.TENTEW;
            default:
                return RoofStyle.FLATTOP;
        }
    }

    static public RoofFeature pickRoofFeature(Random rand) {
        switch (rand.nextInt(3)) {
            case 1:
                return RoofFeature.ANTENNAS;
            case 2:
                return RoofFeature.CONDITIONERS;
            default:
                return RoofFeature.TILE;
        }
    }

    static public Material pickGlassMaterial(Random rand) {
        switch (rand.nextInt(2)) {
            case 1:
                return Material.THIN_GLASS;
            default:
                return Material.GLASS;
        }
    }

    @Override
    public void makeConnected(Random rand, PlatLot relative) {
        //log.info("PlatBuilding's makeConnected");
        super.makeConnected(rand, relative);

        // other bits
        if (relative instanceof PlatBuilding) {
            PlatBuilding relativebuilding = (PlatBuilding) relative;

            neighborsHaveIdenticalHeights = relativebuilding.neighborsHaveIdenticalHeights;
            if (neighborsHaveIdenticalHeights || rand.nextInt(neighborsHaveSimilarHeightsOdds) != 0) {
                height = relativebuilding.height;
                depth = relativebuilding.depth;
            }

            if (rand.nextInt(neighborsHaveSimilarRoundedOdds) == 0) {
                rounded = relativebuilding.rounded;
            }

            // any other bits
            roofStyle = relativebuilding.roofStyle;
            roofFeature = relativebuilding.roofFeature;
            roofScale = relativebuilding.roofScale;
            windowsEW = relativebuilding.windowsEW;
            windowsNS = relativebuilding.windowsNS;

            // do we need stairs?
            relativebuilding.needStairsDown = relativebuilding.depth > depth;
            relativebuilding.needStairsUp = relativebuilding.height > height;
        }
    }

    protected SurroundingFloors getNeighboringFloorCounts(PlatMap platmap, int platX, int platZ) {
        SurroundingFloors neighborBuildings = new SurroundingFloors();

        // get a list of qualified neighbors
        PlatLot[][] neighborChunks = getNeighborPlatLots(platmap, platX, platZ, true);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (neighborChunks[x][z] == null) {
                    neighborBuildings.floors[x][z] = 0;
                } else {

                    // in order for this building to be connected to our building they would have to be the same type
                    neighborBuildings.floors[x][z] = ((PlatBuilding) neighborChunks[x][z]).height;
                }
            }
        }

        return neighborBuildings;
    }

    protected SurroundingFloors getNeighboringBasementCounts(PlatMap platmap, int platX, int platZ) {
        SurroundingFloors neighborBuildings = new SurroundingFloors();

        // get a list of qualified neighbors
        PlatLot[][] neighborChunks = getNeighborPlatLots(platmap, platX, platZ, true);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (neighborChunks[x][z] == null) {
                    neighborBuildings.floors[x][z] = 0;
                } else {

                    // in order for this building to be connected to our building they would have to be the same type
                    neighborBuildings.floors[x][z] = ((PlatBuilding) neighborChunks[x][z]).depth;
                }
            }
        }

        return neighborBuildings;
    }

    protected void drawCeilings(ByteChunk byteChunk, PlatMapContext context, int y1, int height,
            int insetNS, int insetEW, boolean allowRounded,
            Material material, SurroundingFloors heights) {

        // precalculate
        byte materialId = (byte) material.getId();
        int y2 = y1 + height;
        boolean stillNeedCeiling = true;

        // rounded and square inset and there are exactly two neighbors?
        if (allowRounded && rounded) { // already know that... && insetNS == insetEW && heights.getNeighborCount() == 2
            int innerCorner = (ByteChunk.WIDTH - insetNS * 2) + insetNS;
            if (heights.toNorth()) {
                if (heights.toEast()) {
                    byteChunk.setArcNorthEast(insetNS, y1, y2, materialId, true);
                    if (!heights.toNorthEast()) {
                        byteChunk.setArcNorthEast(innerCorner, y1, y2, airId, true);
                    }
                    stillNeedCeiling = false;
                } else if (heights.toWest()) {
                    byteChunk.setArcNorthWest(insetNS, y1, y2, materialId, true);
                    if (!heights.toNorthWest()) {
                        byteChunk.setArcNorthWest(innerCorner, y1, y2, airId, true);
                    }
                    stillNeedCeiling = false;
                }
            } else if (heights.toSouth()) {
                if (heights.toEast()) {
                    byteChunk.setArcSouthEast(insetNS, y1, y2, materialId, true);
                    if (!heights.toSouthEast()) {
                        byteChunk.setArcSouthEast(innerCorner, y1, y2, airId, true);
                    }
                    stillNeedCeiling = false;
                } else if (heights.toWest()) {
                    byteChunk.setArcSouthWest(insetNS, y1, y2, materialId, true);
                    if (!heights.toSouthWest()) {
                        byteChunk.setArcSouthWest(innerCorner, y1, y2, airId, true);
                    }
                    stillNeedCeiling = false;
                }
            }
        }

        // still need to do something?
        if (stillNeedCeiling) {

            // center part
            byteChunk.setBlocks(insetNS, ByteChunk.WIDTH - insetNS, y1, y2, insetEW, ByteChunk.WIDTH - insetEW, materialId);

        }

        // only if we are inset
        if (insetNS > 0 || insetEW > 0) {

            // cardinal bits
            if (heights.toWest()) {
                byteChunk.setBlocks(0, insetNS, y1, y2, insetEW, ByteChunk.WIDTH - insetEW, materialId);
            }
            if (heights.toEast()) {
                byteChunk.setBlocks(ByteChunk.WIDTH - insetNS, ByteChunk.WIDTH, y1, y2, insetEW, ByteChunk.WIDTH - insetEW, materialId);
            }
            if (heights.toNorth()) {
                byteChunk.setBlocks(insetNS, ByteChunk.WIDTH - insetNS, y1, y2, 0, insetEW, materialId);
            }
            if (heights.toSouth()) {
                byteChunk.setBlocks(insetNS, ByteChunk.WIDTH - insetNS, y1, y2, ByteChunk.WIDTH - insetEW, ByteChunk.WIDTH, materialId);
            }

            // corner bits
            if (heights.toNorthWest()) {
                byteChunk.setBlocks(0, insetNS, y1, y2, 0, insetEW, materialId);
            }
            if (heights.toSouthWest()) {
                byteChunk.setBlocks(0, insetNS, y1, y2, ByteChunk.WIDTH - insetEW, ByteChunk.WIDTH, materialId);
            }
            if (heights.toNorthEast()) {
                byteChunk.setBlocks(ByteChunk.WIDTH - insetNS, ByteChunk.WIDTH, y1, y2, 0, insetEW, materialId);
            }
            if (heights.toSouthEast()) {
                byteChunk.setBlocks(ByteChunk.WIDTH - insetNS, ByteChunk.WIDTH, y1, y2, ByteChunk.WIDTH - insetEW, ByteChunk.WIDTH, materialId);
            }
        }
    }

    protected void drawWalls(ByteChunk byteChunk, PlatMapContext context, int y1, int height,
            int insetNS, int insetEW, boolean allowRounded,
            Material material, Material glass, SurroundingFloors heights) {

        // precalculate
        byte materialId = (byte) material.getId();
        byte glassId = (byte) glass.getId();
        int y2 = y1 + height;
        boolean stillNeedWalls = true;

        // rounded and square inset and there are exactly two neighbors?
        if (allowRounded && rounded) {

            // hack the glass material if needed
            if (glass == Material.THIN_GLASS) {
                glassId = (byte) Material.GLASS.getId();
            }

            // do the sides
            if (heights.toSouth()) {
                if (heights.toWest()) {
                    byteChunk.setArcSouthWest(insetNS, y1, y2, glassId, false);
                    if (!heights.toSouthWest()) {
                        byteChunk.setBlocks(insetNS, y1, y2, ByteChunk.WIDTH - insetEW - 1, materialId);
                    }
                    stillNeedWalls = false;
                } else if (heights.toEast()) {
                    byteChunk.setArcSouthEast(insetNS, y1, y2, glassId, false);
                    if (!heights.toSouthEast()) {
                        byteChunk.setBlocks(ByteChunk.WIDTH - insetNS - 1, y1, y2, ByteChunk.WIDTH - insetEW - 1, materialId);
                    }
                    stillNeedWalls = false;
                }
            } else if (heights.toNorth()) {
                if (heights.toWest()) {
                    byteChunk.setArcNorthWest(insetNS, y1, y2, glassId, false);
                    if (!heights.toNorthWest()) {
                        byteChunk.setBlocks(insetNS, y1, y2, insetEW, materialId);
                    }
                    stillNeedWalls = false;
                } else if (heights.toEast()) {
                    byteChunk.setArcNorthEast(insetNS, y1, y2, glassId, false);
                    if (!heights.toNorthEast()) {
                        byteChunk.setBlocks(ByteChunk.WIDTH - insetNS - 1, y1, y2, insetEW, materialId);
                    }
                    stillNeedWalls = false;
                }
            }
        }

        // still need to do something?
        if (stillNeedWalls) {

            // corner columns
            if (!heights.toNorthWest()) {
                byteChunk.setBlocks(insetNS, y1, y2, insetEW, materialId);
            }
            if (!heights.toSouthWest()) {
                byteChunk.setBlocks(insetNS, y1, y2, ByteChunk.WIDTH - insetEW - 1, materialId);
            }
            if (!heights.toNorthEast()) {
                byteChunk.setBlocks(ByteChunk.WIDTH - insetNS - 1, y1, y2, insetEW, materialId);
            }
            if (!heights.toSouthEast()) {
                byteChunk.setBlocks(ByteChunk.WIDTH - insetNS - 1, y1, y2, ByteChunk.WIDTH - insetEW - 1, materialId);
            }

            // cardinal walls
            if (!heights.toWest()) {
                byteChunk.setBlocks(insetNS, insetNS + 1, y1, y2, insetEW + 1, ByteChunk.WIDTH - insetEW - 1, materialId, glassId, windowsNS);
            }
            if (!heights.toEast()) {
                byteChunk.setBlocks(ByteChunk.WIDTH - insetNS - 1, ByteChunk.WIDTH - insetNS, y1, y2, insetEW + 1, ByteChunk.WIDTH - insetEW - 1, materialId, glassId, windowsNS);
            }
            if (!heights.toNorth()) {
                byteChunk.setBlocks(insetNS + 1, ByteChunk.WIDTH - insetNS - 1, y1, y2, insetEW, insetEW + 1, materialId, glassId, windowsEW);
            }
            if (!heights.toSouth()) {
                byteChunk.setBlocks(insetNS + 1, ByteChunk.WIDTH - insetNS - 1, y1, y2, ByteChunk.WIDTH - insetEW - 1, ByteChunk.WIDTH - insetEW, materialId, glassId, windowsEW);
            }
        }

        // only if there are insets
        if (insetNS > 0) {
            if (heights.toWest()) {
                if (!heights.toNorthWest()) {
                    byteChunk.setBlocks(0, insetNS, y1, y2, insetEW, insetEW + 1, materialId, glassId, windowsNS);
                }
                if (!heights.toSouthWest()) {
                    byteChunk.setBlocks(0, insetNS, y1, y2, ByteChunk.WIDTH - insetEW - 1, ByteChunk.WIDTH - insetEW, materialId, glassId, windowsNS);
                }
            }
            if (heights.toEast()) {
                if (!heights.toNorthEast()) {
                    byteChunk.setBlocks(ByteChunk.WIDTH - insetNS, ByteChunk.WIDTH, y1, y2, insetEW, insetEW + 1, materialId, glassId, windowsNS);
                }
                if (!heights.toSouthEast()) {
                    byteChunk.setBlocks(ByteChunk.WIDTH - insetNS, ByteChunk.WIDTH, y1, y2, ByteChunk.WIDTH - insetEW - 1, ByteChunk.WIDTH - insetEW, materialId, glassId, windowsNS);
                }
            }
        }
        if (insetEW > 0) {
            if (heights.toNorth()) {
                if (!heights.toNorthWest()) {
                    byteChunk.setBlocks(insetNS, insetNS + 1, y1, y2, 0, insetEW, materialId, glassId, windowsEW);
                }
                if (!heights.toNorthEast()) {
                    byteChunk.setBlocks(ByteChunk.WIDTH - insetNS - 1, ByteChunk.WIDTH - insetNS, y1, y2, 0, insetEW, materialId, glassId, windowsEW);
                }
            }
            if (heights.toSouth()) {
                if (!heights.toSouthWest()) {
                    byteChunk.setBlocks(insetNS, insetNS + 1, y1, y2, ByteChunk.WIDTH - insetEW, ByteChunk.WIDTH, materialId, glassId, windowsEW);
                }
                if (!heights.toSouthEast()) {
                    byteChunk.setBlocks(ByteChunk.WIDTH - insetNS - 1, ByteChunk.WIDTH - insetNS, y1, y2, ByteChunk.WIDTH - insetEW, ByteChunk.WIDTH, materialId, glassId, windowsEW);
                }
            }
        }
    }

    //TODO roof fixtures (peak, antenna, helipad, air conditioning, stairwells access, penthouse, castle trim, etc.
    protected void drawRoof(ByteChunk chunk, PlatMapContext context, int y1,
            int insetEW, int insetNS, boolean allowRounded,
            Material material, SurroundingFloors heights) {
        switch (roofStyle) {
            case PEAK:
                if (heights.getNeighborCount() == 0) {
                    for (int i = 0; i < PlatMapContext.floorHeight; i++) {
                        if (i == PlatMapContext.floorHeight - 1) {
                            drawCeilings(chunk, context, y1 + i * roofScale, roofScale, insetEW + i, insetNS + i, allowRounded, material, heights);
                        } else {
                            drawWalls(chunk, context, y1 + i * roofScale, roofScale, insetEW + i, insetNS + i, allowRounded, material, material, heights);
                        }
                    }
                } else {
                    drawEdgedRoof(chunk, context, y1, insetEW, insetNS, allowRounded, material, true, heights);
                }
                break;
            case TENTNS:
                if (heights.getNeighborCount() == 0) {
                    for (int i = 0; i < PlatMapContext.floorHeight; i++) {
                        if (i == PlatMapContext.floorHeight - 1) {
                            drawCeilings(chunk, context, y1 + i * roofScale, roofScale, insetEW + i, insetNS, allowRounded, material, heights);
                        } else {
                            drawWalls(chunk, context, y1 + i * roofScale, roofScale, insetEW + i, insetNS, allowRounded, material, material, heights);
                        }
                    }
                } else {
                    drawEdgedRoof(chunk, context, y1, insetEW, insetNS, allowRounded, material, true, heights);
                }
                break;
            case TENTEW:
                if (heights.getNeighborCount() == 0) {
                    for (int i = 0; i < PlatMapContext.floorHeight; i++) {
                        if (i == PlatMapContext.floorHeight - 1) {
                            drawCeilings(chunk, context, y1 + i * roofScale, roofScale, insetEW, insetNS + i, allowRounded, material, heights);
                        } else {
                            drawWalls(chunk, context, y1 + i * roofScale, roofScale, insetEW, insetNS + i, allowRounded, material, material, heights);
                        }
                    }
                } else {
                    drawEdgedRoof(chunk, context, y1, insetEW, insetNS, allowRounded, material, true, heights);
                }
                break;
            case EDGED:
                drawEdgedRoof(chunk, context, y1, insetEW, insetNS, allowRounded, material, true, heights);
                break;
            case FLATTOP:
                drawEdgedRoof(chunk, context, y1, insetEW, insetNS, allowRounded, material, false, heights);
                break;
        }
    }

    private void drawEdgedRoof(ByteChunk chunk, PlatMapContext context, int y1,
            int insetEW, int insetNS, boolean allowRounded,
            Material material, boolean doEdge, SurroundingFloors heights) {

        // a little bit of edge 
        if (doEdge) {
            drawWalls(chunk, context, y1, 1, insetEW, insetNS, allowRounded, material, material, heights);
        }

        // add the special features
        switch (roofFeature) {
            case ANTENNAS:
                if (heights.getNeighborCount() == 0) {
                    drawAntenna(chunk, 6, y1, 6);
                    drawAntenna(chunk, 6, y1, 9);
                    drawAntenna(chunk, 9, y1, 6);
                    drawAntenna(chunk, 9, y1, 9);
                    break;
                } // else go for the conditioners
            case CONDITIONERS:
                drawConditioner(chunk, 6, y1, 6);
                drawConditioner(chunk, 6, y1, 9);
                drawConditioner(chunk, 9, y1, 6);
                drawConditioner(chunk, 9, y1, 9);
                break;
            case TILE:
                drawCeilings(chunk, context, y1, 1, insetEW + 1, insetNS + 1, allowRounded, tileMaterial, heights);
                break;
        }
    }

    private void drawAntenna(ByteChunk chunk, int x, int y, int z) {
        if (rand.nextBoolean()) {
            int y2 = y + rand.nextInt(8) + 8;
            chunk.setBlocks(x, y, y + 3, z, doubleSlabId);
            chunk.setBlocks(x, y + 2, y2, z, antennaId);
            if (y2 >= navLightY) {
                navLightX = x;
                navLightY = y2 - 1;
                navLightZ = z;
            }
        }
    }

    protected void drawNavLight(RealChunk chunk) {
        if (navLightY > 0) {
            chunk.setTorch(navLightX, navLightY, navLightZ, Material.TORCH, Torch.FLOOR);
        }
    }

    private void drawConditioner(ByteChunk chunk, int x, int y, int z) {
        if (rand.nextBoolean()) {
            chunk.setBlocks(x, y, y + 1, z, conditionerId);
        }
    }

    private void drawDoor(RealChunk chunk, int x1, int x2, int x3, int y1, int y2, int z1, int z2, int z3,
            Direction.Door direction, Material wallMaterial) {

        // frame the door
        chunk.setBlocks(x1, y1, y2, z1, wallMaterial);
        chunk.setBlocks(x2, y1 + 2, y2, z2, wallMaterial);
        chunk.setBlocks(x3, y1, y2, z3, wallMaterial);

        // place the door
        chunk.setWoodenDoor(x2, y1, z2, direction);
    }

    protected void drawDoors(RealChunk chunk, int y1, int floorHeight,
            int insetNS, int insetEW, StairWell where,
            SurroundingFloors heights, Material wallMaterial) {
        int w1 = RealChunk.Width - 1;
        int w2 = RealChunk.Width - 2;
        int w3 = RealChunk.Width - 3;
        int x1 = insetNS;
        int x2 = w1 - insetNS;
        int z1 = insetEW;
        int z2 = w1 - insetEW;
        int y2 = y1 + floorHeight - 1;

        switch (where) {
            case CENTER:
                int center = RealChunk.Width / 2;

                if (!heights.toWest() && rand.nextBoolean()) {
                    drawDoor(chunk, x1, x1, x1,
                            y1, y2,
                            center - 1, center, center + 1,
                            Door.WESTBYNORTHWEST, wallMaterial);
                }
                if (!heights.toEast() && rand.nextBoolean()) {
                    drawDoor(chunk, x2, x2, x2,
                            y1, y2,
                            center - 1, center, center + 1,
                            Door.EASTBYSOUTHEAST, wallMaterial);
                }
                if (!heights.toNorth() && rand.nextBoolean()) {
                    drawDoor(chunk, center - 1, center, center + 1,
                            y1, y2,
                            z1, z1, z1,
                            Door.NORTHBYNORTHEAST, wallMaterial);
                }
                if (!heights.toSouth() && rand.nextBoolean()) {
                    drawDoor(chunk, center - 1, center, center + 1,
                            y1, y2,
                            z2, z2, z2,
                            Door.SOUTHBYSOUTHWEST, wallMaterial);
                }
                break;
            case SOUTHWEST:
                if (rand.nextBoolean()) {
                    drawDoor(chunk, x2, x2, x2, y1, y2, 0, 1, 2, Door.EASTBYNORTHEAST, wallMaterial);
                }
                if (rand.nextBoolean()) {
                    drawDoor(chunk, 0, 1, 2, y1, y2, z2, z2, z2, Door.SOUTHBYSOUTHWEST, wallMaterial);
                }
                break;
            case SOUTHEAST:
                if (rand.nextBoolean()) {
                    drawDoor(chunk, 0, 1, 2, y1, y2, z1, z1, z1, Door.NORTHBYNORTHWEST, wallMaterial);
                }
                if (rand.nextBoolean()) {
                    drawDoor(chunk, x2, x2, x2, y1, y2, w1, w2, w3, Door.EASTBYSOUTHEAST, wallMaterial);
                }
                break;
            case NORTHWEST:
                if (rand.nextBoolean()) {
                    drawDoor(chunk, x1, x1, x1, y1, y2, 0, 1, 2, Door.WESTBYNORTHWEST, wallMaterial);
                }
                if (rand.nextBoolean()) {
                    drawDoor(chunk, w1, w2, w3, y1, y2, z2, z2, z2, Door.SOUTHBYSOUTHEAST, wallMaterial);
                }
                break;
            case NORTHEAST:
                if (rand.nextBoolean()) {
                    drawDoor(chunk, w1, w2, w3, y1, y2, z1, z1, z1, Door.NORTHBYNORTHEAST, wallMaterial);
                }
                if (rand.nextBoolean()) {
                    drawDoor(chunk, x1, x1, x1, y1, y2, w1, w2, w3, Door.WESTBYSOUTHWEST, wallMaterial);
                }
                break;
        }
    }

    protected boolean stairsHere(SurroundingFloors neighbors, double throwOfDice) {
        return (throwOfDice < 1.0 - ((double) neighbors.getNeighborCount() / 4.0));
    }

    //TODO These might go too far by one
    static class StairAt {
        public int X = 0;

        public int Z = 0;

        public StairAt(int floorHeight, int insetNS, int insetEW, StairWell where) {
            switch (where) {
                case CENTER:
                    X = (RealChunk.Width - floorHeight) / 2;
                    Z = (RealChunk.Width - 4) / 2;
                    break;
                case SOUTHWEST:
                    X = insetNS;
                    Z = insetEW;
                    break;
                case SOUTHEAST:
                    X = insetNS;
                    Z = RealChunk.Width - 4 - insetEW;
                    break;
                case NORTHWEST:
                    X = RealChunk.Width - floorHeight - insetNS;
                    Z = insetEW;
                    break;
                case NORTHEAST:
                    X = RealChunk.Width - floorHeight - insetNS;
                    Z = RealChunk.Width - 4 - insetEW;
                    break;
            }
        }

    }

    public StairWell getStairWellLocation(boolean allowRounded, SurroundingFloors heights) {
        if (allowRounded && rounded) {
            if (heights.toWest()) {
                if (heights.toNorth()) {
                    return StairWell.SOUTHWEST;
                } else if (heights.toSouth()) {
                    return StairWell.SOUTHEAST;
                }
            } else if (heights.toEast()) {
                if (heights.toNorth()) {
                    return StairWell.NORTHWEST;
                } else if (heights.toSouth()) {
                    return StairWell.NORTHEAST;
                }
            }
        }
        return StairWell.CENTER;
    }

    protected void drawStairs(RealChunk chunk, int y, int floorHeight,
            int insetNS, int insetEW, StairWell where, Material stairMaterial) {
        StairAt at = new StairAt(floorHeight, insetNS, insetEW, where);
        for (int i = 0; i < floorHeight; i++) {
            chunk.setBlock(at.X + i, y + floorHeight - 1, at.Z + 1, Material.AIR);
            chunk.setBlock(at.X + i, y + floorHeight - 1, at.Z + 2, Material.AIR);
            chunk.setBlock(at.X + i, y + i, at.Z + 1, stairMaterial);
            chunk.setBlock(at.X + i, y + i, at.Z + 2, stairMaterial);
        }
    }

    protected void drawStairsWalls(RealChunk chunk, int y, int floorHeight,
            int insetNS, int insetEW, StairWell where,
            Material wallMaterial, boolean drawStartcap, boolean drawEndcap) {
        StairAt at = new StairAt(floorHeight, insetNS, insetEW, where);
        chunk.setBlocks(at.X, at.X + floorHeight, y, y + floorHeight - 1, at.Z, at.Z + 1, wallMaterial);
        chunk.setBlocks(at.X, at.X + floorHeight, y, y + floorHeight - 1, at.Z + 3, at.Z + 4, wallMaterial);
        if (drawStartcap) {
            chunk.setBlocks(at.X - 1, at.X, y, y + floorHeight - 1, at.Z, at.Z + 4, wallMaterial);
        }
        if (drawEndcap) {
            chunk.setBlocks(at.X + floorHeight, at.X + floorHeight + 1, y, y + floorHeight - 1, at.Z, at.Z + 4, wallMaterial);
        }
    }

    ;

	protected void drawOtherPillars(RealChunk chunk, int y1, int floorHeight,
            StairWell where, Material wallMaterial) {
        int y2 = y1 + floorHeight - 1;
        if (where != StairWell.SOUTHWEST) {
            chunk.setBlocks(3, 5, y1, y2, 3, 5, wallMaterial);
        }
        if (where != StairWell.SOUTHEAST) {
            chunk.setBlocks(3, 5, y1, y2, 11, 13, wallMaterial);
        }
        if (where != StairWell.NORTHWEST) {
            chunk.setBlocks(11, 13, y1, y2, 3, 5, wallMaterial);
        }
        if (where != StairWell.NORTHEAST) {
            chunk.setBlocks(11, 13, y1, y2, 11, 13, wallMaterial);
        }
    }

}
