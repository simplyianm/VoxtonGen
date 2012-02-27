/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap.city;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.MapType;
import net.voxton.voxtongen.plat.PlatLot;
import net.voxton.voxtongen.plat.building.PlatSkyscraper;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapSkyscrapers extends PlatMapBlocks {
    public PlatMapSkyscrapers(World world, PlatMapContext context, int platX, int platZ) {
        super(world, context, platX, platZ);
    }

    @Override
    protected PlatLot makeBlockPlat(int bx, int bz, int x, int z) {
        return new PlatSkyscraper(platRand, context);
    }

    @Override
    public MapType getType() {
        return MapType.SKYSCRAPERS;
    }

}
