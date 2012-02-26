/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.plats.building;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.plats.PlatType;

/**
 *
 * @author simplyianm
 */
public class PlatOneFloorBuilding extends PlatHeightedBuilding {
    public PlatOneFloorBuilding(Random rand, PlatMapContext context) {
        super(rand, context, 1, 1);
    }

    @Override
    public PlatType getType() {
        return PlatType.ONE_FLOOR;
    }

}
