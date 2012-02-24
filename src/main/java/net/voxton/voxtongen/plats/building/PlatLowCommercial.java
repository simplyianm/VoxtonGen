/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.plats.building;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;

/**
 *
 * @author simplyianm
 */
public class PlatLowCommercial extends PlatHeightedBuilding {
    public PlatLowCommercial(Random rand, PlatMapContext context) {
        super(rand, context, 2, 5);
        depth = 0; //Bars don't need basements.
    }

}
