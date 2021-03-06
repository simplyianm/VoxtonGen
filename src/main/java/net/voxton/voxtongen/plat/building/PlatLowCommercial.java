/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.plat.building;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.plat.PlatType;

/**
 *
 * @author simplyianm
 */
public class PlatLowCommercial extends PlatHeightedBuilding {
    public PlatLowCommercial(Random rand, PlatMapContext context) {
        super(rand, context, 2, 5);
        depth = 0; //Bars don't need basements.
    }

    @Override
    public PlatType getType() {
        return PlatType.LOW_COMMERCIAL;
    }

}
