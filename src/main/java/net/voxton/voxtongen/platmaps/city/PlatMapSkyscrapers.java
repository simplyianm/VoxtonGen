/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmaps.city;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.plats.building.PlatSkyscraper;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapSkyscrapers extends PlatMapBlocks {
    public PlatMapSkyscrapers(World world, Random random, PlatMapContext context, int platX, int platZ) {
        super(world, random, context, platX, platZ);
    }

    @Override
    protected PlatLot makeBlockPlat(int bx, int bz, int x, int z) {
        return new PlatSkyscraper(platRand, context);
    }

}
