package net.voxton.voxtongen.surrounding;

import java.util.Arrays;
import net.voxton.voxtongen.platmap.PlatMap;
import net.voxton.voxtongen.plat.PlatLot;
import net.voxton.voxtongen.plat.road.RoadOrientation;

public class SurroundingRoads {
    private boolean[][] roads;

    public SurroundingRoads() {
        super();
        roads = new boolean[3][3];
    }

    public SurroundingRoads(PlatMap platmap, int platX, int platZ) {
        super();
        roads = new boolean[3][3];

        // calculate neighbors
        updateNeighbors(platmap, platX, platZ);
    }

    protected void updateNeighbors(PlatMap platmap, int platX, int platZ) {
        PlatLot platlot = platmap.platLots[platX][platZ];

        // get a list of qualified neighbors
        PlatLot[][] neighborChunks = platlot.getNeighborPlatLots(platmap, platX, platZ, false);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                PlatLot neighbor = neighborChunks[x][z];

                // beyond the edge
                if (neighbor == null) {
                    roads[x][z] = true; //All roads on edges are... edges.
                } else {
                    roads[x][z] = platlot.isConnected(neighbor);
                }
            }
        }
    }

    /**
     * Returns true if the road section is straight.
     *
     * @return
     */
    public boolean isStraight() {
        boolean ns = (toWest() && toEast());
        boolean we = (toNorth() && toSouth());

        return (ns && !we) || (!ns && we);
    }

    /**
     * Returns true if there are roads adjacent to this one.
     *
     * @return
     */
    public boolean adjacentRoads() {
        return toEast() || toWest() || toNorth() || toSouth();
    }

    /**
     * Returns true if there is a road in the center.
     *
     * @return
     */
    public boolean toCenter() {
        return roads[1][1];
    }

    /**
     * Returns true if there is a road to the north.
     *
     * @return
     */
    public boolean toNorth() {
        return roads[1][0];
    }

    /**
     * Returns true if there is a road to the south.
     *
     * @return
     */
    public boolean toSouth() {
        return roads[1][2];
    }

    /**
     * Returns true if there is a road to the west.
     *
     * @return
     */
    public boolean toWest() {
        return roads[0][1];
    }

    /**
     * Returns true if there is a road to the east.
     *
     * @return
     */
    public boolean toEast() {
        return roads[2][1];
    }

    /**
     * Returns true if there is a road to the northwest.
     *
     * @return
     */
    public boolean toNorthWest() {
        return roads[0][0];
    }

    /**
     * Returns true if there is a road to the northeast.
     *
     * @return
     */
    public boolean toNorthEast() {
        return roads[2][0];
    }

    /**
     * Returns true if there is a road to the southwest.
     *
     * @return
     */
    public boolean toSouthWest() {
        return roads[0][2];
    }

    /**
     * Returns true if there is a road to the southeast.
     *
     * @return
     */
    public boolean toSouthEast() {
        return roads[2][2];
    }

    /**
     * Gets the orientation of the road piece.
     *
     * @return
     */
    public RoadOrientation getOrientation() {
        if (toWest() && toEast()) {
            if (toNorth() && !toSouth()) {
                return RoadOrientation.SOUTH_AVE;
            } else if (!toNorth() && toSouth()) {
                return RoadOrientation.NORTH_AVE;
            } else if (!toNorth() && !toSouth()) {
                return RoadOrientation.WE;
            }
        }

        if (toNorth() && toSouth()) {
            if (toWest() && !toEast()) {
                return RoadOrientation.EAST_AVE;
            } else if (!toWest() && toEast()) {
                return RoadOrientation.WEST_AVE;
            } else if (!toWest() && !toEast()) {
                return RoadOrientation.NS;
            }
        }

        if (toNorthWest() && toNorthEast()) {
            if (!toSouthWest() && !toSouthEast()) {
                return RoadOrientation.SOUTH_CONJ;
            } else if (!toSouthWest() && toSouthEast()) {
                return RoadOrientation.SW_AVE;
            } else if (toSouthWest() && !toSouthEast()) {
                return RoadOrientation.SE_AVE;
            }
        }

        if (toSouthWest() && toSouthEast()) {
            if (!toNorthWest() && !toNorthEast()) {
                return RoadOrientation.NORTH_CONJ;
            } else if (!toNorthWest() && toNorthEast()) {
                return RoadOrientation.NW_AVE;
            } else if (toNorthWest() && !toNorthEast()) {
                return RoadOrientation.NE_AVE;
            }
        }

        if (toNorthWest() && toSouthWest()) {
            if (!toNorthEast() && !toSouthEast()) {
                return RoadOrientation.EAST_CONJ;
            } else if (!toNorthEast() && toSouthEast()) {
                return RoadOrientation.NE_AVE;
            } else if (toNorthEast() && !toSouthEast()) {
                return RoadOrientation.SE_AVE;
            }
        }

        if (toNorthEast() && toSouthEast()) {
            if (!toNorthWest() && !toSouthWest()) {
                return RoadOrientation.WEST_CONJ;
            } else if (!toNorthWest() && toSouthWest()) {
                return RoadOrientation.NW_AVE;
            } else if (toNorthWest() && !toSouthWest()) {
                return RoadOrientation.SW_AVE;
            }
        }

        return RoadOrientation.CENTER;
    }

}
