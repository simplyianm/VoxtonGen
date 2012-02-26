package net.voxton.voxtongen.plats.road;

import java.util.Random;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.support.Direction;
import net.voxton.voxtongen.support.Direction.Chest;
import net.voxton.voxtongen.support.Direction.Ladder;
import net.voxton.voxtongen.support.Direction.TrapDoor;
import net.voxton.voxtongen.chunk.RealChunk;
import net.voxton.voxtongen.chunk.ByteChunk;
import net.voxton.voxtongen.plats.PlatType;
import net.voxton.voxtongen.surrounding.SurroundingRoads;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.inventory.ItemStack;

/**
 * Paved road.
 */
public class PlatRoadPaved extends PlatRoad {
    //TODO Lines on the road
    protected static long connectedkeyForPavedRoads = 0;

    protected final static int sidewalkWidth = 3;

    protected final static int lightpostHeight = 3;

    protected final static int crossDitchEdge = 7;

    protected final static int vaultWidth = 5;

    protected final static int vaultDoorOffset = 2;

    protected final static int waterOffset = 3;

    protected final static Material airMaterial = Material.AIR;

    protected final static Material lightpostbaseMaterial = Material.DOUBLE_STEP;

    protected final static Material lightpostMaterial = Material.FENCE;

    protected final static Material lightMaterial = Material.GLOWSTONE;

    protected final static Material manpipeMaterial = Material.OBSIDIAN;

    protected final static Material sewerWallMaterial = Material.MOSSY_COBBLESTONE;

    protected final static Material vineMaterial = Material.VINE;

    protected final static byte airId = (byte) airMaterial.getId();

    protected final static byte sewerFloorId = (byte) Material.COBBLESTONE.getId();

    protected final static byte sewerWallId = (byte) sewerWallMaterial.getId();

    protected final static byte plumbingId = (byte) Material.OBSIDIAN.getId();

    protected final static byte doorBrickId = (byte) Material.BRICK.getId();

    protected final static byte doorIronId = (byte) Material.IRON_FENCE.getId();

    protected final static byte waterId = (byte) Material.WATER.getId();

    protected final static byte pavementId = (byte) Material.STONE.getId();

    protected final static byte sidewalkId = (byte) Material.STEP.getId();

    public PlatRoadPaved(Random rand, PlatMapContext context) {
        super(rand, context);

        // if the master key for paved roads isn't calculated then do it
        if (connectedkeyForPavedRoads == 0) {
            connectedkeyForPavedRoads = rand.nextLong();
        }

        // all paved roads are interconnected
        connectedkey = connectedkeyForPavedRoads;
    }

    @Override
    public void generateChunk(PlatMap platmap, ByteChunk chunk, PlatMapContext context, int platX, int platZ) {
        // where do we start
        int base1Y = context.streetLevel - PlatMapContext.floorHeight * 3 + 1;
        int sewerY = base1Y + 1;
        int base2Y = base1Y + PlatMapContext.floorHeight + 1;
        int plumbingY = base2Y + 1;
        int base3Y = context.streetLevel - 1;
        int sidewalkLevel = context.streetLevel + 1;

        // starting with the bottom
        generateBedrock(chunk, context, base1Y);

        // look around
        SurroundingRoads roads = new SurroundingRoads(platmap, platX, platZ);

        doSewer(context, chunk, sewerY, roads, base1Y, base2Y);
        doPlumbing(context, chunk, base2Y, base3Y, plumbingY);
        doPavement(context, roads, chunk);
        doSidewalk(roads, context, chunk, sidewalkLevel);

    }

    protected void doPavement(PlatMapContext context, SurroundingRoads roads, ByteChunk chunk) {
        chunk.setLayer(context.streetLevel, pavementId);
    }

    protected void doSewer(PlatMapContext context, ByteChunk chunk, int sewerY, SurroundingRoads roads, int base1Y, int base2Y) {
        // sewer or not?
        if (context.doSewer) {

            // draw the floor of the sewer
            chunk.setLayer(sewerY - 1, sewerFloorId);
            chunk.setBlocks(crossDitchEdge, ByteChunk.WIDTH - crossDitchEdge,
                    sewerY - 1, sewerY,
                    crossDitchEdge, ByteChunk.WIDTH - crossDitchEdge, airId);

            // draw/fill vaults and ceiling inset
            generateVault(chunk, context, 0, vaultWidth,
                    sewerY,
                    0, vaultWidth, true, true);
            generateVault(chunk, context, 0, vaultWidth,
                    sewerY,
                    ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH, true, true);
            generateVault(chunk, context, ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH,
                    sewerY,
                    0, vaultWidth, true, true);
            generateVault(chunk, context, ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH,
                    sewerY,
                    ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH, true, true);
            generateCeilingInset(chunk, context, vaultWidth, ByteChunk.WIDTH - vaultWidth,
                    sewerY,
                    vaultWidth, ByteChunk.WIDTH - vaultWidth,
                    !roads.toWest(), !roads.toEast(), !roads.toNorth(), !roads.toSouth());

            // now cardinal water, vaults and insets
            if (roads.toWest()) {
                chunk.setBlocks(0, crossDitchEdge,
                        sewerY - 1, sewerY,
                        crossDitchEdge, ByteChunk.WIDTH - crossDitchEdge, airId);
                generateCeilingInset(chunk, context, 0, vaultWidth,
                        sewerY,
                        vaultWidth, ByteChunk.WIDTH - vaultWidth, false, false, true, true);
                chunk.setBlock(waterOffset, sewerY - 1, crossDitchEdge, waterId);
            } else {
                generateVault(chunk, context, 0, vaultWidth,
                        sewerY,
                        vaultWidth, ByteChunk.WIDTH - vaultWidth, false, true);
            }
            if (roads.toEast()) {
                chunk.setBlocks(ByteChunk.WIDTH - crossDitchEdge, ByteChunk.WIDTH,
                        sewerY - 1, sewerY,
                        crossDitchEdge, ByteChunk.WIDTH - crossDitchEdge, airId);
                generateCeilingInset(chunk, context, ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH,
                        sewerY,
                        vaultWidth, ByteChunk.WIDTH - vaultWidth, false, false, true, true);
                chunk.setBlock(ByteChunk.WIDTH - waterOffset - 1, sewerY - 1, ByteChunk.WIDTH - crossDitchEdge - 1, waterId);
            } else {
                generateVault(chunk, context, ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH,
                        sewerY,
                        vaultWidth, ByteChunk.WIDTH - vaultWidth, false, true);
            }
            if (roads.toNorth()) {
                chunk.setBlocks(crossDitchEdge, ByteChunk.WIDTH - crossDitchEdge,
                        sewerY - 1, sewerY,
                        0, crossDitchEdge, airId);
                generateCeilingInset(chunk, context, vaultWidth, ByteChunk.WIDTH - vaultWidth,
                        sewerY,
                        0, vaultWidth, true, true, false, false);
                chunk.setBlock(crossDitchEdge, sewerY - 1, waterOffset, waterId);
            } else {
                generateVault(chunk, context, vaultWidth, ByteChunk.WIDTH - vaultWidth,
                        sewerY,
                        0, vaultWidth, true, false);
            }
            if (roads.toSouth()) {
                chunk.setBlocks(crossDitchEdge, ByteChunk.WIDTH - crossDitchEdge,
                        sewerY - 1, sewerY,
                        ByteChunk.WIDTH - crossDitchEdge, ByteChunk.WIDTH, airId);
                generateCeilingInset(chunk, context, vaultWidth, ByteChunk.WIDTH - vaultWidth,
                        sewerY,
                        ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH, true, true, false, false);
                chunk.setBlock(ByteChunk.WIDTH - crossDitchEdge - 1, sewerY - 1, ByteChunk.WIDTH - waterOffset - 1, waterId);
            } else {
                generateVault(chunk, context, vaultWidth, ByteChunk.WIDTH - vaultWidth,
                        sewerY,
                        ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH, true, false);
            }
        } else {

            // backfill the sewer
            chunk.setLayer(base1Y, base2Y - base1Y, stoneId);
        }
    }

    protected void doPlumbing(PlatMapContext context, ByteChunk chunk, int base2Y, int base3Y, int plumbingY) {
        // plumbing?
        if (context.doPlumbing) {

            // draw plumbing floor
            chunk.setLayer(base2Y, context.isolationId);

            // draw plumbing
            for (int x = 0; x < ByteChunk.WIDTH - 1; x = x + 2) {
                for (int z = 0; z < ByteChunk.WIDTH - 1; z = z + 2) {
                    chunk.setBlocks(x + 1, plumbingY, plumbingY + 4, z + 1, plumbingId);
                    if (rand.nextInt(context.oddsOfPlumbingConnection) == 0) {
                        chunk.setBlocks(x + 1, plumbingY, plumbingY + 4, z, plumbingId);
                    }
                    if (rand.nextInt(context.oddsOfPlumbingConnection) == 0) {
                        chunk.setBlocks(x, plumbingY, plumbingY + 4, z + 1, plumbingId);
                    }
                    if (rand.nextInt(context.oddsOfPlumbingTreasure) == 0) {
                        if (context.doTreasureInPlumbing) {
                            byte treasureId = (byte) pickPlumbingTreasure().getId();
                            chunk.setBlocks(x, plumbingY, plumbingY + 1, z, treasureId);
                            if (context.doSewer) {
                                if (treasureId == waterId
                                        && x >= crossDitchEdge && x < ByteChunk.WIDTH - crossDitchEdge
                                        && z >= crossDitchEdge && z < ByteChunk.WIDTH - crossDitchEdge) {
                                    chunk.setBlock(x, plumbingY - 1, z, airId);
                                }
                            }
                        }
                    } else if (rand.nextInt(context.oddsOfPlumbingConnection) == 0) {
                        chunk.setBlocks(x, plumbingY + 2, plumbingY + 4, z, plumbingId);
                    }
                }
            }

            // draw plumbing ceiling
            chunk.setLayer(base3Y, context.isolationId);
        } else {

            // backfill the plumbing
            chunk.setLayer(base2Y, base3Y - base2Y + 1, stoneId);
        }
    }

    /**
     * Creates the sidewalk.
     *
     * @param roads
     * @param chunk
     * @param sidewalkLevel
     */
    protected void doSidewalk(SurroundingRoads roads, PlatMapContext context, ByteChunk chunk, int sidewalkLevel) {
        // sidewalk edges
        if (!roads.toWest()) {
            chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, 0, ByteChunk.WIDTH, sidewalkId);
        }
        if (!roads.toEast()) {
            chunk.setBlocks(ByteChunk.WIDTH - sidewalkWidth, ByteChunk.WIDTH, sidewalkLevel, sidewalkLevel + 1, 0, ByteChunk.WIDTH, sidewalkId);
        }
        if (!roads.toNorth()) {
            chunk.setBlocks(0, ByteChunk.WIDTH, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, sidewalkId);
        }
        if (!roads.toSouth()) {
            chunk.setBlocks(0, ByteChunk.WIDTH, sidewalkLevel, sidewalkLevel + 1, ByteChunk.WIDTH - sidewalkWidth, ByteChunk.WIDTH, sidewalkId);
        }

        // sidewalk corners
        if (roads.getOrientation().equals(RoadOrientation.CENTER)) {
            if (!roads.toNorthWest()) {
                chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, sidewalkId);
            }
            if (!roads.toNorthEast()) {
                chunk.setBlocks(ByteChunk.WIDTH - sidewalkWidth, ByteChunk.WIDTH, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, sidewalkId);
            }
            if (!roads.toSouthWest()) {
                chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, ByteChunk.WIDTH - sidewalkWidth, ByteChunk.WIDTH, sidewalkId);
            }
            if (!roads.toSouthEast()) {
                chunk.setBlocks(ByteChunk.WIDTH - sidewalkWidth, ByteChunk.WIDTH, sidewalkLevel, sidewalkLevel + 1, ByteChunk.WIDTH - sidewalkWidth, ByteChunk.WIDTH, sidewalkId);
            }
        }

        // round things out
        if (!roads.toWest() && roads.toEast() && !roads.toNorth() && roads.toSouth()) {
            generateRoundedOut(chunk, context, sidewalkWidth, sidewalkWidth,
                    false, false);
        }
        if (!roads.toWest() && roads.toEast() && roads.toNorth() && !roads.toSouth()) {
            generateRoundedOut(chunk, context, sidewalkWidth, ByteChunk.WIDTH - sidewalkWidth - 4,
                    false, true);
        }
        if (roads.toWest() && !roads.toEast() && !roads.toNorth() && roads.toSouth()) {
            generateRoundedOut(chunk, context, ByteChunk.WIDTH - sidewalkWidth - 4, sidewalkWidth,
                    true, false);
        }
        if (roads.toWest() && !roads.toEast() && roads.toNorth() && !roads.toSouth()) {
            generateRoundedOut(chunk, context, ByteChunk.WIDTH - sidewalkWidth - 4, ByteChunk.WIDTH - sidewalkWidth - 4,
                    true, true);
        }
    }

    @Override
    public void generateBlocks(PlatMap platmap, RealChunk chunk, PlatMapContext context, int platX, int platZ) {
        // look around
        SurroundingRoads roads = new SurroundingRoads(platmap, platX, platZ);

        // light posts
        RoadOrientation ro = roads.getOrientation();
        if (!ro.equals(RoadOrientation.CENTER)) {
            if (!ro.equals(RoadOrientation.EAST) && !ro.equals(RoadOrientation.SOUTH)) {
                generateLightPost(chunk, context, sidewalkWidth - 1, sidewalkWidth - 1);
            }
            if (!ro.equals(RoadOrientation.WEST) && !ro.equals(RoadOrientation.NORTH)) {
                generateLightPost(chunk, context, ByteChunk.WIDTH - sidewalkWidth, ByteChunk.WIDTH - sidewalkWidth);
            }
        }

        // where do we start
        int base1Y = context.streetLevel - PlatMapContext.floorHeight * 3 + 1;
        int sewerY = base1Y + 1;
        int base2Y = base1Y + PlatMapContext.floorHeight + 1;
        int sidewalkLevel = context.streetLevel + 1;

        // sewer?
        if (context.doSewer) {

            // drill down
            if (!ro.equals(RoadOrientation.CENTER)
                    && roads.toEast() && roads.toNorth()) {
                generateManhole(chunk, ByteChunk.WIDTH - sidewalkWidth,
                        base2Y,
                        sidewalkLevel,
                        ByteChunk.WIDTH - sidewalkWidth - 1);
            }

            // draw/fill vaults and ceiling inset
            populateVault(chunk, context, 0, vaultWidth,
                    sewerY,
                    0, vaultWidth);
            populateVault(chunk, context, 0, vaultWidth,
                    sewerY,
                    ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH);
            populateVault(chunk, context, ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH,
                    sewerY,
                    0, vaultWidth);
            populateVault(chunk, context, ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH,
                    sewerY,
                    ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH);

            // now cardinal water, vaults and insets
            if (!roads.toWest()) {
                populateVault(chunk, context, 0, vaultWidth,
                        sewerY,
                        vaultWidth, ByteChunk.WIDTH - vaultWidth);
            }
            if (!roads.toEast()) {
                populateVault(chunk, context, ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH,
                        sewerY,
                        vaultWidth, ByteChunk.WIDTH - vaultWidth);
            }
            if (!roads.toNorth()) {
                populateVault(chunk, context, vaultWidth, ByteChunk.WIDTH - vaultWidth,
                        sewerY,
                        0, vaultWidth);
            }
            if (!roads.toSouth()) {
                populateVault(chunk, context, vaultWidth, ByteChunk.WIDTH - vaultWidth,
                        sewerY,
                        ByteChunk.WIDTH - vaultWidth, ByteChunk.WIDTH);
            }

//			// now cardinal water, vaults and insets
//			if (roads.toSouth()) {
//				generateCeilingVines(chunk, 0, vaultWidth,
//						sewerY, 
//						vaultWidth, RealChunk.Width - vaultWidth, false, false, true, true);
//			}
//			if (roads.toNorth()) {
//				generateCeilingVines(chunk, RealChunk.Width - vaultWidth, RealChunk.Width,
//						sewerY, 
//						vaultWidth, RealChunk.Width - vaultWidth, false, false, true, true);
//			}
//			if (roads.toWest()) {
//				generateCeilingVines(chunk, vaultWidth, RealChunk.Width - vaultWidth,
//						sewerY,
//						0, vaultWidth, true, true, false, false);
//			}
//			if (roads.toEast()) {
//				generateCeilingVines(chunk, vaultWidth, RealChunk.Width - vaultWidth,
//						sewerY, 
//						RealChunk.Width - vaultWidth, RealChunk.Width, true, true, false, false);
//			}
        }
    }

//	protected void generateCeilingVines(RealChunk chunk, ContextUrban context, int x1, int x2, int y1, int z1, int z2, 
//			boolean insetS, boolean insetN, boolean insetE, boolean insetW) {
//		int y = y1 + PlatMap.FloorHeight - 1;
//		
//		if (insetS || insetN)
//			for (int z = z1; z < z2; z++) {
//				if (insetS) {
//					if (rand.nextInt(context.oddsOfSewerVines) == 0)
//						chunk.setBlock(x1 + 1, y, z, vineMaterial);
//				}
//				if (insetN) {
//					if (rand.nextInt(context.oddsOfSewerVines) == 0)
//						chunk.setBlock(x2 - 2, y, z, vineMaterial);
//				}
//			}
//		if (insetE || insetW)
//			for (int x = x1; x < x2; x++) {
//				if (insetE) {
//					if (rand.nextInt(context.oddsOfSewerVines) == 0)
//						chunk.setBlock(x, y, z1 + 1, vineMaterial);
//				}
//				if (insetW) {
//					if (rand.nextInt(context.oddsOfSewerVines) == 0)
//						chunk.setBlock(x, y, z2 - 2, vineMaterial);
//				}
//			}
//	}
    protected void generateLightPost(RealChunk chunk, PlatMapContext context, int x, int z) {
        int sidewalkLevel = context.streetLevel + 1;
        chunk.setBlock(x, sidewalkLevel, z, lightpostbaseMaterial);
        chunk.setBlocks(x, sidewalkLevel + 1, sidewalkLevel + lightpostHeight + 1, z, lightpostMaterial);
        chunk.setBlock(x, sidewalkLevel + lightpostHeight + 1, z, lightMaterial, true);
    }

    protected void generateManhole(RealChunk chunk, int x, int y1, int y2, int z) {
        // place the manhole
        chunk.setTrapDoor(x, y2, z, TrapDoor.SOUTH);

        // make a tube of material going down
        chunk.setBlocks(x - 1, x + 2, y1 + 1, y2 - 1, z - 1, z + 2, manpipeMaterial);

        // empty the vault and fix up the doors
        chunk.setBlocks(x - 1, x + vaultWidth - 3, y1 - PlatMapContext.floorHeight, y1, z, z + vaultWidth - 2, airMaterial);
        chunk.setBlocks(x, y1 - PlatMapContext.floorHeight, y1 - PlatMapContext.floorHeight + 2, z - 1, sewerWallMaterial);
        chunk.setWoodenDoor(x + 1, y1 - PlatMapContext.floorHeight, z - 1, Direction.Door.NORTHBYNORTHEAST);

        // ladder
        chunk.setLadder(x, y1 - PlatMapContext.floorHeight, y2, z, Ladder.SOUTH);
    }

    /**
     * Rounds out the roads.
     *
     * @param chunk
     * @param context
     * @param x
     * @param z
     * @param toNorth
     * @param toEast
     */
    protected void generateRoundedOut(ByteChunk chunk, PlatMapContext context, int x, int z, boolean toNorth, boolean toEast) {
        int sidewalkLevel = context.streetLevel + 1;

        // long bits
        for (int i = 0; i < 4; i++) {
            chunk.setBlock(toNorth ? x + 3 : x, sidewalkLevel, z + i, sidewalkId);
            chunk.setBlock(x + i, sidewalkLevel, toEast ? z + 3 : z, sidewalkId);
        }

        // little notch
        chunk.setBlock(toNorth ? x + 2 : x + 1,
                sidewalkLevel,
                toEast ? z + 2 : z + 1,
                sidewalkId);
    }

    protected void generateCeilingInset(ByteChunk chunk, PlatMapContext context, int x1, int x2, int y1, int z1, int z2,
            boolean insetS, boolean insetN, boolean insetE, boolean insetW) {
        int y = y1 + PlatMapContext.floorHeight - 1;

        if (insetS || insetN) {
            for (int z = z1; z < z2; z++) {
                if (insetS) {
                    chunk.setBlock(x1, y, z, sewerWallId);
                }
                if (insetN) {
                    chunk.setBlock(x2 - 1, y, z, sewerWallId);
                }
            }
        }
        if (insetE || insetW) {
            for (int x = x1; x < x2; x++) {
                if (insetE) {
                    chunk.setBlock(x, y, z1, sewerWallId);
                }
                if (insetW) {
                    chunk.setBlock(x, y, z2 - 1, sewerWallId);
                }
            }
        }
    }

    protected void generateVault(ByteChunk chunk, PlatMapContext context, int x1, int x2, int y1, int z1, int z2, boolean doorNS, boolean doorEW) {
        int y2 = y1 + PlatMapContext.floorHeight;

        // place the walls
        for (int x = x1; x < x2; x++) {
            chunk.setBlocks(x, y1, y2, z1, sewerWallId);
            chunk.setBlocks(x, y1, y2, z2 - 1, sewerWallId);
        }
        for (int z = z1; z < z2; z++) {
            chunk.setBlocks(x1, y1, y2, z, sewerWallId);
            chunk.setBlocks(x2 - 1, y1, y2, z, sewerWallId);
        }

        // is the vault empty?
        byte doorId = rand.nextBoolean() ? doorIronId : doorBrickId;

        // place the doors, if the vault is empty then just "leave the door" open
        if (doorNS) {
            chunk.setBlocks(x1, y1, y2 - 2, z1 + vaultDoorOffset, doorId);
            chunk.setBlocks(x2 - 1, y1, y2 - 2, z1 + vaultDoorOffset, doorId);
        }
        if (doorEW) {
            chunk.setBlocks(x1 + vaultDoorOffset, y1, y2 - 2, z1, doorId);
            chunk.setBlocks(x1 + vaultDoorOffset, y1, y2 - 2, z2 - 1, doorId);
        }
    }

    private int minTreasureId = Material.IRON_SPADE.getId();

    private int maxTreasureId = Material.ROTTEN_FLESH.getId();

    private int countTreasureIds = maxTreasureId - minTreasureId;

    protected void populateVault(RealChunk chunk, PlatMapContext context, int x1, int x2, int y1, int z1, int z2) {
        //int y2 = y1 + PlatMap.FloorHeight;

        // fill the vault
        if (context.doTreasureInSewer) {

            // trick or treat?
            if (rand.nextInt(context.oddsOfSewerTreasure) == 0) {

                // where is it?
                int xC = (x2 - x1) / 2 + x1;
                int zC = (z2 - z1) / 2 + z1;

                if (context.doSpawnerInSewer && rand.nextInt(context.oddsOfSewerTrick) == 0) {
                    chunk.setSpawner(xC, y1, zC, pickTrick());
                } else {

                    // fabricate the treasures
                    int treasureCount = rand.nextInt(context.maxTreasureCount) + 1;
                    ItemStack[] items = new ItemStack[treasureCount];
                    for (int i = 0; i < treasureCount; i++) {
                        items[i] = new ItemStack(rand.nextInt(countTreasureIds) + minTreasureId, rand.nextInt(2) + 1);
                    }

                    // make a chest and stuff the stuff into it
                    chunk.setChest(xC, y1, zC, Chest.NORTH, items);
                }
            }
        }
    }

    protected CreatureType pickTrick() {
        switch (rand.nextInt(8)) {
            case 1:
                return CreatureType.CREEPER;
            case 2:
                return CreatureType.PIG_ZOMBIE;
            case 3:
                return CreatureType.SKELETON;
            case 4:
                return CreatureType.SPIDER;
            case 5:
                return CreatureType.ZOMBIE;
            case 6:
                return CreatureType.CAVE_SPIDER;
            case 7:
                return CreatureType.SILVERFISH;
            default:
                return CreatureType.ENDERMAN;
        }
    }

    protected Material pickPlumbingTreasure() {
        switch (rand.nextInt(20)) {

            // random junk
            case 0:
            case 1:
            case 2:
            case 3:
                return Material.IRON_BLOCK;
            case 4:
            case 5:
            case 6:
                return Material.GOLD_BLOCK;
            case 7:
            case 8:
                return Material.LAPIS_BLOCK;
            case 9:
                return Material.DIAMOND_BLOCK;

            // the rest of the time it is just flooded
            default:
                return Material.WATER;
        }
    }

    @Override
    public PlatType getType() {
        return PlatType.ROAD_PAVED;
    }

}
