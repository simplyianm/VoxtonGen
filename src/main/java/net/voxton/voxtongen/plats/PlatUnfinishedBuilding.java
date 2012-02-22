package net.voxton.voxtongen.plats;

import java.util.Random;

import org.bukkit.Material;

import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmaps.PlatMap;
import net.voxton.voxtongen.support.ByteChunk;
import net.voxton.voxtongen.support.Direction.Stair;
import net.voxton.voxtongen.support.Direction.Torch;
import net.voxton.voxtongen.support.RealChunk;
import net.voxton.voxtongen.support.SurroundingFloors;
import net.voxton.voxtongen.support.Direction.StairWell;

public class PlatUnfinishedBuilding extends PlatBuilding {

	protected final static int FloorHeight = PlatMapContext.FloorHeight;
	
	protected final static byte airId = (byte) Material.AIR.getId();
	protected final static byte girderId = (byte) Material.DOUBLE_STEP.getId();
	protected final static Material dirtMaterial = Material.DIRT;
	protected final static Material fenceMaterial = Material.IRON_FENCE;
	protected final static Material stairMaterial = Material.WOOD_STAIRS;
	protected final static Material wallMaterial = Material.SMOOTH_BRICK;
	protected final static Material ceilingMaterial = Material.STONE;
	
	protected final static int fenceHeight = 3;
	protected final static int inset = 2;
	
	// our special bits
	protected boolean unfinishedBasementOnly;
	protected int floorsBuilt;
	protected int lastHorizontalGirder;
	
	//TODO randomly add a construction crane on the top most horizontal girder
	
	public PlatUnfinishedBuilding(Random rand, PlatMapContext context) {
		super(rand, context);
		
		// basement only?
		unfinishedBasementOnly = rand.nextInt(context.oddsOfOnlyUnfinishedBasements) == 0;
		
		// how many floors are finished?
		floorsBuilt = rand.nextInt(height);
	}
	
	@Override
	public void makeConnected(Random rand, PlatLot relative) {
		super.makeConnected(rand, relative);
		// unlike most other plat types, this one doesn't attempt to make its bits similar
		
		// other bits
//		if (relative instanceof PlatUnfinishedBuilding) {
//			PlatUnfinishedBuilding relativebuilding = (PlatUnfinishedBuilding) relative;
//
//			// our special bits
//			basementOnly = relativebuilding.basementOnly;
//			floorsBuilt = relativebuilding.floorsBuilt;
//		}
	}

	@Override
	public void generateChunk(PlatMap platmap, ByteChunk chunk, PlatMapContext context, int platX, int platZ) {
		// check out the neighbors
		SurroundingFloors neighborBasements = getNeighboringBasementCounts(platmap, platX, platZ);
		SurroundingFloors neighborFloors = getNeighboringFloorCounts(platmap, platX, platZ);

		// starting with the bottom
		int lowestY = context.streetLevel - FloorHeight * (depth - 1) - 3;
		generateBedrock(chunk, context, lowestY);
		
		// bottom most floor
		drawCeilings(chunk, context, lowestY, 1, 0, 0, false, ceilingMaterial, neighborBasements);
		
		// below ground
		for (int floor = 0; floor < depth; floor++) {
			int floorAt = context.streetLevel - FloorHeight * floor - 2;
			
			// at the first floor add a fence to prevent folks from falling in
			if (floor == 0) {
				drawWalls(chunk, context, context.streetLevel + 2, fenceHeight, 0, 0, false,
						fenceMaterial, fenceMaterial, neighborBasements);
				holeFence(chunk, context.streetLevel + 2, neighborBasements);
			}
			
			// one floor please
			drawWalls(chunk, context, floorAt, FloorHeight, 0, 0, false,
					dirtMaterial, dirtMaterial, neighborBasements);
			drawWalls(chunk, context, floorAt, FloorHeight, 1, 1, false,
					wallMaterial, wallMaterial, neighborBasements);
			
			// ceilings if needed
			if (!unfinishedBasementOnly) {
				drawCeilings(chunk, context, floorAt + FloorHeight - 1, 1, 1, 1, false,
						ceilingMaterial, neighborBasements);
			} else {
				drawHorizontalGirders(chunk, floorAt + FloorHeight - 1, neighborBasements);
			}
	
			// hold up the bit we just drew
			drawVerticalGirders(chunk, floorAt, FloorHeight);
			
			// one down, more to go
			neighborBasements.decrement();
		}
		
		// do more?
		if (!unfinishedBasementOnly) {
			lastHorizontalGirder = 0;

			// above ground
			for (int floor = 0; floor < height; floor++) {
				int floorAt = context.streetLevel + FloorHeight * floor + 2;
				
				// floor built yet?
				if (floor <= floorsBuilt) {
					
					// the floor of the next floor
					drawCeilings(chunk, context, floorAt + FloorHeight - 1, 1, 1, 1, false,
							ceilingMaterial, neighborFloors);
				} else {
					
					// sometimes the top most girders aren't there quite yet
					if (floor < height - 1 || rand.nextBoolean()) {
						drawHorizontalGirders(chunk, floorAt + FloorHeight - 1, neighborFloors);
						lastHorizontalGirder = floorAt + FloorHeight - 1;
					}
				}
	
				// hold up the bit we just drew
				drawVerticalGirders(chunk, floorAt, FloorHeight);
				
				// one down, more to go
				neighborFloors.decrement();
			}
		}
	}

	@Override
	public void generateBlocks(PlatMap platmap, RealChunk chunk, PlatMapContext context, int platX, int platZ) {
		
		// work on the basement stairs first
		if (!unfinishedBasementOnly) {
			
			if (needStairsDown) {
				for (int floor = 0; floor < depth; floor++) {
					int y = context.streetLevel - FloorHeight * floor - 2;
					
					// place the stairs and such
					drawStairs(chunk, y, FloorHeight, inset, inset, StairWell.CENTER, stairMaterial);

// unfinished buildings don't need walls on the stairs
//					drawStairsWalls(chunk, y, FloorHeight, inset, inset, StairWell.CENTER, 
//							wallMaterial, false, false);
				}
			}
			
			if (needStairsUp) {
				for (int floor = 0; floor < height; floor++) {
					int y = context.streetLevel + FloorHeight * floor + 2;
					
					// floor built yet?
					if (floor <= floorsBuilt) {
						
						// more stairs and such
						if (floor < height - 1)
							drawStairs(chunk, y, FloorHeight, inset, inset, StairWell.CENTER, stairMaterial);
						
// unfinished buildings don't need walls on the stairs
//						if (floor > 0 || (floor == 0 && (depth > 0 || height > 1)))
//							drawStairsWalls(chunk, y, FloorHeight, inset, inset, StairWell.CENTER, 
//									stairWallMaterial, false, false);
					}
				}
			}
			
			// plop a crane on top?
			if (lastHorizontalGirder > 0 && rand.nextInt(context.oddsOfCranes) == 0) {
				if (rand.nextBoolean())
					drawCrane(chunk, inset + 2, lastHorizontalGirder + 1, inset);
				else
					drawCrane(chunk, inset + 2, lastHorizontalGirder + 1, RealChunk.Width - inset - 1);
			}
		}
	}
	
	private void drawCrane(RealChunk chunk, int x, int y, int z) {
		// vertical bit
		chunk.setBlocks(x, y, y + 8, z, Material.IRON_FENCE);
		chunk.setBlocks(x, y + 8, y + 10, z, Material.DOUBLE_STEP);
		chunk.setBlocks(x - 1, y + 8, y + 10, z, Material.STEP);
		chunk.setTorch(x, y + 10, z, Material.TORCH, Torch.FLOOR);
		
		// horizontal bit
		chunk.setBlock(x + 1, y + 8, z, Material.GLASS);
		chunk.setBlocks(x + 2, x + 11, y + 8, y + 9, z, z + 1, Material.IRON_FENCE);
		chunk.setBlocks(x + 1, x + 10, y + 9, y + 10, z, z + 1, Material.STEP);
		chunk.setStair(x + 10, y + 9, z, Material.SMOOTH_STAIRS, Stair.WEST);
		
		// counter weight
		chunk.setBlock(x - 2, y + 9, z, Material.STEP);
		chunk.setStair(x - 3, y + 9, z, Material.SMOOTH_STAIRS, Stair.EAST);
		chunk.setBlocks(x - 3, x - 1, y + 7, y + 9, z, z + 1, Material.WOOL.getId(), (byte) rand.nextInt(16));
	}
	
	private void drawVerticalGirders(ByteChunk chunk, int y1, int floorHeight) {
		int y2 = y1 + floorHeight;
		chunk.setBlocks(inset, y1, y2, inset, girderId);
		chunk.setBlocks(inset, y1, y2, ByteChunk.Width - inset - 1, girderId);
		chunk.setBlocks(ByteChunk.Width - inset - 1, y1, y2, inset, girderId);
		chunk.setBlocks(ByteChunk.Width - inset - 1, y1, y2, ByteChunk.Width - inset - 1, girderId);
	}

	private void drawHorizontalGirders(ByteChunk chunk, int y1, SurroundingFloors neighbors) {
		int x1 = neighbors.toWest() ? 0 : inset;
		int x2 = neighbors.toEast() ? ByteChunk.Width - 1 : ByteChunk.Width - inset - 1;
		int z1 = neighbors.toNorth() ? 0 : inset;
		int z2 = neighbors.toSouth() ? ByteChunk.Width - 1 : ByteChunk.Width - inset - 1;
		int i1 = inset;
		int i2 = ByteChunk.Width - inset - 1;
		
		chunk.setBlocks(x1, x2 + 1, y1, y1 + 1, i1, i1 + 1, girderId);
		chunk.setBlocks(x1, x2 + 1, y1, y1 + 1, i2, i2 + 1, girderId);
		chunk.setBlocks(i1, i1 + 1, y1, y1 + 1, z1, z2 + 1, girderId);
		chunk.setBlocks(i2, i2 + 1, y1, y1 + 1, z1, z2 + 1, girderId);
	}
	
	private void holeFence(ByteChunk chunk, int y1, SurroundingFloors neighbors) {
		int i = rand.nextInt(ByteChunk.Width / 2) + 4;
		int y2 = y1 + 2;
		if (rand.nextBoolean() && !neighbors.toWest())
			chunk.setBlocks(0, y1, y2, i, airId);
		if (rand.nextBoolean() && !neighbors.toEast())
			chunk.setBlocks(ByteChunk.Width - 1, y1, y2, i, airId);
		if (rand.nextBoolean() && !neighbors.toNorth())
			chunk.setBlocks(i, y1, y2, 0, airId);
		if (rand.nextBoolean() && !neighbors.toSouth())
			chunk.setBlocks(i, y1, y2, ByteChunk.Width - 1, airId);
	}

}
