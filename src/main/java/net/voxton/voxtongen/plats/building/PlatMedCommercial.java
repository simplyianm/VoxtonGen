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
public class PlatMedCommercial extends PlatHeightedBuilding {
    public PlatMedCommercial(Random rand, PlatMapContext context) {
        super(rand, context, 5, 10);
    }

}
