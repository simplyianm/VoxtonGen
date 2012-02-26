/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 *
 * @author simplyianm
 */
public class PlatMapCache extends LinkedHashMap<Long, PlatMap> {
    private static final int MAX_ENTRIES = 16;

    @Override
    protected boolean removeEldestEntry(Entry<Long, PlatMap> eldest) {
        return size() > MAX_ENTRIES;
    }

}
