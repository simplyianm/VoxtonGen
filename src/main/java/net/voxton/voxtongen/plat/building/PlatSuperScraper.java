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
public class PlatSuperScraper extends PlatHeightedBuilding {
    public PlatSuperScraper(Random rand, PlatMapContext context) {
        super(rand, context, 50, 60);
    }

    @Override
    public PlatType getType() {
        return PlatType.SKYSCRAPER;
    }

}
