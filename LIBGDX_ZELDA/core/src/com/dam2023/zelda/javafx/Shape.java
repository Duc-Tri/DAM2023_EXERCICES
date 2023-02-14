package com.dam2023.zelda.javafx;

/**
 * The <code>Shape</code> class provides definitions for objects
 * that represent some form of geometric shape. The <code>Shape</code>
 * is described by a object, which can express the
 * outline of the <code>Shape</code> as well as a rule for determining
 * how the outline divides the 2D plane into interior and exterior
 * points. Each <code>Shape</code> object provides callbacks to get the
 * bounding box of the geometry, determine whether points or
 * rectangles lie partly or entirely within the interior
 * of the <code>Shape</code>, and retrieve a <code>PathIterator</code>
 * object that describes the trajectory path of the <code>Shape</code>
 * outline.
 * <p>
 * <b>Definition of insideness:</b>
 * A point is considered to lie inside a
 * <code>Shape</code> if and only if:
 * <ul>
 * <li> it lies completely
 * inside the<code>Shape</code> boundary <i>or</i>
 * <li>
 * it lies exactly on the <code>Shape</code> boundary <i>and</i> the
 * space immediately adjacent to the
 * point in the increasing <code>X</code> direction is
 * entirely inside the boundary <i>or</i>
 * <li>
 * it lies exactly on a horizontal boundary segment <b>and</b> the
 * space immediately adjacent to the point in the
 * increasing <code>Y</code> direction is inside the boundary.
 * </ul>
 * <p>The <code>contains</code> and <code>intersects</code> methods
 * consider the interior of a <code>Shape</code> to be the area it
 * encloses as if it were filled.  This means that these methods
 * consider
 * unclosed shapes to be implicitly closed for the purpose of
 * determining if a shape contains or intersects a rectangle or if a
 * shape contains a point.
 *
 *
 */
public abstract class Shape {
    /**
     * Note that there is no guarantee that the returned
     * {@link RectBounds} is the smallest bounding box that encloses
     * the <code>Shape</code>, only that the <code>Shape</code> lies
     * entirely within the indicated <code>RectBounds</code>.
     * @return an instance of <code>RectBounds</code>
     */
    public abstract RectBounds getBounds();

    /**
     * Tests if the specified coordinates are inside the boundary of the
     * <code>Shape</code>.
     * @param x the specified X coordinate to be tested
     * @param y the specified Y coordinate to be tested
     * @return <code>true</code> if the specified coordinates are inside
     *         the <code>Shape</code> boundary; <code>false</code>
     *         otherwise.
     */
    public abstract boolean contains(float x, float y);

    /**
     * Tests if the interior of the <code>Shape</code> intersects the
     * interior of a specified rectangular area.
     * The rectangular area is considered to intersect the <code>Shape</code>
     * if any point is contained in both the interior of the
     * <code>Shape</code> and the specified rectangular area.
     * <p>
     * The {@code Shape.intersects()} method allows a {@code Shape}
     * implementation to conservatively return {@code true} when:
     * <ul>
     * <li>
     * there is a high probability that the rectangular area and the
     * <code>Shape</code> intersect, but
     * <li>
     * the calculations to accurately determine this intersection
     * are prohibitively expensive.
     * </ul>
     * This means that for some {@code Shapes} this method might
     * return {@code true} even though the rectangular area does not
     * intersect the {@code Shape}.
     * more accurate computations of geometric intersection than most
     * {@code Shape} objects and therefore can be used if a more precise
     * answer is required.
     *
     * @param x the X coordinate of the upper-left corner
     *          of the specified rectangular area
     * @param y the Y coordinate of the upper-left corner
     *          of the specified rectangular area
     * @param w the width of the specified rectangular area
     * @param h the height of the specified rectangular area
     * @return <code>true</code> if the interior of the <code>Shape</code> and
     *      the interior of the rectangular area intersect, or are
     *      both highly likely to intersect and intersection calculations
     *      would be too expensive to perform; <code>false</code> otherwise.
     */
    public abstract boolean intersects(float x, float y, float w, float h);

    /**
     * Tests if the interior of the <code>Shape</code> intersects the
     * interior of a specified rectangular area.
     * The rectangular area is considered to intersect the <code>Shape</code>
     * if any point is contained in both the interior of the
     * <code>Shape</code> and the specified rectangular area.
     * <p>
     * The {@code Shape.intersects()} method allows a {@code Shape}
     * implementation to conservatively return {@code true} when:
     * <ul>
     * <li>
     * there is a high probability that the rectangular area and the
     * <code>Shape</code> intersect, but
     * <li>
     * the calculations to accurately determine this intersection
     * are prohibitively expensive.
     * </ul>
     * This means that for some {@code Shapes} this method might
     * return {@code true} even though the rectangular area does not
     * intersect the {@code Shape}.
     * more accurate computations of geometric intersection than most
     * {@code Shape} objects and therefore can be used if a more precise
     * answer is required.
     *
     *          of the specified rectangular area
     *          of the specified rectangular area
     * @return <code>true</code> if the interior of the <code>Shape</code> and
     *      the interior of the rectangular area intersect, or are
     *      both highly likely to intersect and intersection calculations
     *      would be too expensive to perform; <code>false</code> otherwise.
     */
    public boolean intersects(RectBounds r) {
        float x = r.getMinX();
        float y = r.getMinY();
        float w = r.getMaxX() - x;
        float h = r.getMaxY() - y;
        return intersects(x, y, w, h);
    }

    /**
     * Tests if the specified line segment intersects the interior of the
     * rectangle denoted by rx1, ry1, rx2, ry2.
     */
    static boolean intersectsLine(float rx1, float ry1, float rwidth,
                                  float rheight, float x1, float y1, float x2, float y2)
    {
        int out1, out2;
        if ((out2 = outcode(rx1, ry1, rwidth, rheight, x2, y2)) == 0) {
            return true;
        }
        while ((out1 = outcode(rx1, ry1, rwidth, rheight, x1, y1)) != 0) {
            if ((out1 & out2) != 0) {
                return false;
            }
            if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
                if ((out1 & OUT_RIGHT) != 0) {
                    rx1 += rwidth;
                }
                y1 = y1 + (rx1 - x1) * (y2 - y1) / (x2 - x1);
                x1 = rx1;
            } else {
                if ((out1 & OUT_BOTTOM) != 0) {
                    ry1 += rheight;
                }
                x1 = x1 + (ry1 - y1) * (x2 - x1) / (y2 - y1);
                y1 = ry1;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    static int outcode(float rx, float ry, float rwidth, float rheight, float x, float y) {
        /*
         * Note on casts to double below.  If the arithmetic of
         * x+w or y+h is done in int, then we may get integer
         * overflow. By converting to double before the addition
         * we force the addition to be carried out in double to
         * avoid overflow in the comparison.
         *
         * See bug 4320890 for problems that this can cause.
         */
        int out = 0;
        if (rwidth <= 0) {
            out |= OUT_LEFT | OUT_RIGHT;
        } else if (x < rx) {
            out |= OUT_LEFT;
        } else if (x > rx + (double) rwidth) {
            out |= OUT_RIGHT;
        }
        if (rheight <= 0) {
            out |= OUT_TOP | OUT_BOTTOM;
        } else if (y < ry) {
            out |= OUT_TOP;
        } else if (y > ry + (double) rheight) {
            out |= OUT_BOTTOM;
        }
        return out;
    }

    /**
     * The bitmask that indicates that a point lies to the left of
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_LEFT = 1;

    /**
     * The bitmask that indicates that a point lies above
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_TOP = 2;

    /**
     * The bitmask that indicates that a point lies to the right of
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_RIGHT = 4;

    /**
     * The bitmask that indicates that a point lies below
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_BOTTOM = 8;

}
