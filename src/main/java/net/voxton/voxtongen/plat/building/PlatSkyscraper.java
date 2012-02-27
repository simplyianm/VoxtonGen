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
public class PlatSkyscraper extends PlatHeightedBuilding {
    public PlatSkyscraper(Random rand, PlatMapContext context) {
        super(rand, context, 20, 30);
    }

    @Override
    public PlatType getType() {
        return PlatType.SKYSCRAPER;
    }

}
