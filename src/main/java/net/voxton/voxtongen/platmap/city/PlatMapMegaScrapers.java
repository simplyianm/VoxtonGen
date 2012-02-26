/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap.city;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.plats.PlatLot;
import net.voxton.voxtongen.plats.building.PlatSkyscraper;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapMegaScrapers extends PlatMapBlocks {
    private PlatLot last;

    private int lbx = 0;

    private int lbz = 0;

    public PlatMapMegaScrapers(World world, PlatMapContext context, int platX, int platZ) {
        super(world, context, platX, platZ);
        last = null;
    }

    @Override
    protected PlatLot makeBlockPlat(int bx, int bz, int x, int z) {
        if (bx == lbx && bz == lbz && last != null) {
            PlatLot next = new PlatSkyscraper(platRand, context);
            next.makeConnected(platRand, last);
            last = next;
            return next;
        }

        last = new PlatSkyscraper(platRand, context);
        lbx = bx;
        lbz = bz;
        return last;
    }

}
