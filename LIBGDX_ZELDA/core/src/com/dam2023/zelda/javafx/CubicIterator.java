package com.dam2023.zelda.javafx;


import java.util.NoSuchElementException;

/**
 * A utility class to iterate over the path segments of a cubic curve
 * segment through the PathIterator interface.
 *
 * @version 10 Feb 1997
 */
class CubicIterator implements PathIterator {
    final CubicCurve2D cubic;
    final BaseTransform transform;
    int index;

    CubicIterator(CubicCurve2D q, BaseTransform tx) {
        this.cubic = q;
        this.transform = tx;
    }

    /**
     * Return the winding rule for determining the insideness of the
     * path.
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     */
    @Override
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    /**
     * Tests if there are more points to read.
     * @return true if there are more points to read
     */
    @Override
    public boolean isDone() {
        return (index > 1);
    }

    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    @Override
    public void next() {
        ++index;
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A float array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of float x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    @Override
    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("cubic iterator iterator out of bounds");
        }
        int type;
        if (index == 0) {
            coords[0] = cubic.x1;
            coords[1] = cubic.y1;
            type = SEG_MOVETO;
        } else {
            coords[0] = cubic.ctrlx1;
            coords[1] = cubic.ctrly1;
            coords[2] = cubic.ctrlx2;
            coords[3] = cubic.ctrly2;
            coords[4] = cubic.x2;
            coords[5] = cubic.y2;
            type = SEG_CUBICTO;
        }
        if (transform != null) {
            transform.transform(coords, 0, coords, 0, index == 0 ? 1 : 3);
        }
        return type;
    }
}
