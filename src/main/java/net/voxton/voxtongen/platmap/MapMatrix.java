/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen.platmap;

/**
 * A square matrix that holds map types.
 */
public class MapMatrix {
    private final int side;

    private MapType[] data;

    public MapMatrix(int side) {
        this.side = side;
        data = new MapType[side * side];
    }

    /**
     * Gets a MapType from the matrix.
     *
     * @param row
     * @param col
     * @return
     */
    public MapType get(int row, int col) {
        int index = getIndexFromCoords(row, col);

        return data[index];
    }

    /**
     * Gets a MapType from the matrix based on map coords.
     *
     * @param mapX
     * @param mapZ
     * @return
     */
    public MapType getMapType(int mapX, int mapZ) {
        int relX = getMapMatrixRelative(mapX);
        int relZ = getMapMatrixRelative(mapZ);

        return get(relX, relZ);
    }

    /**
     * Sets a MapType in the matrix.
     *
     * @param row
     * @param col
     * @param type
     * @return
     */
    public MapMatrix set(int row, int col, MapType type) {
        int index = getIndexFromCoords(row, col);

        data[index] = type;
        return this;
    }

    private int getIndexFromCoords(int relX, int relZ) {
        int index = (relX * side) + relZ;

        if (index > (side * side) - 1) {
            throw new IllegalArgumentException("The specified coordinates of (" + relX + ", " + relZ + ") do not apply to this matrix.");
        }

        return index;
    }

    private int getMapMatrixRelative(int mapCoord) {
        return (mapCoord >= 0)
                ? mapCoord % side
                : (mapCoord % side) + side - 1;

    }
}
