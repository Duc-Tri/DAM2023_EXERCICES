package com.dam2023.zelda.javafx;


import java.util.NoSuchElementException;

/**
 * A utility class to iterate over the path segments of a line segment
 * through the PathIterator interface.
 *
 * @version     1.17, 05/05/07
 */
class LineIterator implements PathIterator {
    final Line2D line;
    final BaseTransform transform;
    int index;

    LineIterator(Line2D l, BaseTransform tx) {
        this.line = l;
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
            throw new NoSuchElementException("line iterator out of bounds");
        }
        int type;
        if (index == 0) {
            coords[0] = line.x1;
            coords[1] = line.y1;
            type = SEG_MOVETO;
        } else {
            coords[0] = line.x2;
            coords[1] = line.y2;
            type = SEG_LINETO;
        }
        if (transform != null) {
            transform.transform(coords, 0, coords, 0, 1);
        }
        return type;
    }
}
