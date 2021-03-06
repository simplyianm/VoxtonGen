/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap.city;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.MapType;
import net.voxton.voxtongen.plat.PlatLot;
import net.voxton.voxtongen.plat.building.PlatLowCommercial;
import net.voxton.voxtongen.plat.building.PlatMedCommercial;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapTown extends PlatMapBlocks {
    public PlatMapTown(World world, PlatMapContext context, int platX, int platZ) {
        super(world, context, platX, platZ);
    }

    @Override
    protected PlatLot makeBlockPlat(int bx, int bz, int x, int z) {
        int rand = platRand.nextInt(3);
        return (rand != 0) ? new PlatMedCommercial(platRand, context) : new PlatLowCommercial(platRand, context);
    }

    @Override
    public MapType getType() {
        return MapType.TOWN;
    }

}
