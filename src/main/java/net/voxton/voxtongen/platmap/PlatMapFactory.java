/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap;

import java.util.Random;
import net.voxton.voxtongen.context.PlatMapContext;
import net.voxton.voxtongen.platmap.city.PlatMapCentralPark;
import net.voxton.voxtongen.platmap.city.PlatMapMegaScrapers;
import net.voxton.voxtongen.platmap.city.PlatMapSkyscrapers;
import net.voxton.voxtongen.platmap.city.PlatMapTown;
import net.voxton.voxtongen.platmap.etc.PlatMapVanilla;
import org.bukkit.World;

/**
 *
 * @author simplyianm
 */
public class PlatMapFactory {
    public PlatMap makePlatMap(World world, Random random, PlatMapContext context, int platX, int platZ, MapType type) {
        PlatMap map = null;
        
        switch(type) {
            case CENTRAL_PARK:
                map = new PlatMapCentralPark(world, context, platX, platZ);
                break;
                
            case MEGASCRAPERS:
                map = new PlatMapMegaScrapers(world, context, platX, platZ);
                break;
                
            case SKYSCRAPERS:
                map = new PlatMapSkyscrapers(world, context, platX, platZ);
                break;
                
            case TOWN:
                map = new PlatMapTown(world, context, platX, platZ);
                break;
                
            case VANILLA:
            default:
                map = new PlatMapVanilla(world, context, platX, platZ);
                break;
        }
        
        return map;
    }
}
