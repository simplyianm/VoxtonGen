/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.plat.road;

/**
 *
 * @author simplyianm
 */
public enum RoadOrientation {
    NORTH_AVE(false, false),
    SOUTH_AVE(false, false),
    WEST_AVE(false, false),
    EAST_AVE(false, false),
    NS(false, false),
    WE(false, false),
    NORTH_CONJ(true, true),
    SOUTH_CONJ(true, true),
    WEST_CONJ(true, true),
    EAST_CONJ(true, true),
    NW_AVE(true, false),
    NE_AVE(true, false),
    SW_AVE(true, false),
    SE_AVE(true, false),
    CENTER(true, false);

    private final boolean conjunction;

    private final boolean avenueStreet;

    private RoadOrientation(boolean conjunction, boolean avenueStreet) {
        this.conjunction = conjunction;
        this.avenueStreet = avenueStreet;
    }

    public boolean isConjunction() {
        return conjunction;
    }

    public boolean isAvenueStreet() {
        return avenueStreet;
    }

}
