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
public class PlatMedCommercial extends PlatHeightedBuilding {
    public PlatMedCommercial(Random rand, PlatMapContext context) {
        super(rand, context, 5, 10);
    }

    @Override
    public PlatType getType() {
        return PlatType.MED_COMMERCIAL;
    }

}
