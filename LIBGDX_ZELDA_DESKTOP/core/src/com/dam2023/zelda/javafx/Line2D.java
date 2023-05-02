package com.dam2023.zelda.javafx;


/**
 * This <code>Line2D</code> represents a line segment in {@code (x,y)}
 * coordinate space.  This class, like all of the Java 2D API, uses a
 * default coordinate system called <i>user space</i> in which the y-axis
 * values increase downward and x-axis values increase to the right.  For
 * more information on the user space coordinate system, see the
 * <a href="http://java.sun.com/j2se/1.3/docs/guide/2d/spec/j2d-intro.fm2.html#61857">
 * Coordinate Systems</a> section of the Java 2D Programmer's Guide.
 *
 * @version     1.37, 05/05/07
 */
public class Line2D extends Shape {
    /**
     * The X coordinate of the start point of the line segment.
     */
    public float x1;

    /**
     * The Y coordinate of the start point of the line segment.
     */
    public float y1;

    /**
     * The X coordinate of the end point of the line segment.
     */
    public float x2;

    /**
     * The Y coordinate of the end point of the line segment.
     */
    public float y2;

    /**
     * Constructs and initializes a Line from the specified coordinates.
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     */
    public Line2D(float x1, float y1, float x2, float y2) {
        setLine(x1, y1, x2, y2);
    }

    /**
     * Sets the location of the end points of this <code>Line2D</code>
     * to the specified float coordinates.
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     */
    public void setLine(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RectBounds getBounds() {
        RectBounds b = new RectBounds();
        b.setBoundsAndSort(x1, y1, x2, y2);
        return b;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean contains(float x, float y) { return false; }

    /**
     * @inheritDoc
     */
    @Override
    public boolean intersects(float x, float y, float w, float h) {
        int out1, out2;
        if ((out2 = outcode(x, y, w, h, x2, y2)) == 0) {
            return true;
        }
        float px = x1;
        float py = y1;
        while ((out1 = outcode(x, y, w, h, px, py)) != 0) {
            if ((out1 & out2) != 0) {
                return false;
            }
            if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
                px = x;
                if ((out1 & OUT_RIGHT) != 0) {
                    px += w;
                }
                py = y1 + (px - x1) * (y2 - y1) / (x2 - x1);
            } else {
                py = y;
                if ((out1 & OUT_BOTTOM) != 0) {
                    py += h;
                }
                px = x1 + (py - y1) * (x2 - x1) / (y2 - y1);
            }
        }
        return true;
    }

    /**
     * Returns an indicator of where the specified point
     * {@code (px,py)} lies with respect to the line segment from
     * {@code (x1,y1)} to {@code (x2,y2)}.
     * The return value can be either 1, -1, or 0 and indicates
     * in which direction the specified line must pivot around its
     * first end point, {@code (x1,y1)}, in order to point at the
     * specified point {@code (px,py)}.
     * <p>A return value of 1 indicates that the line segment must
     * turn in the direction that takes the positive X axis towards
     * the negative Y axis.  In the default coordinate system used by
     * Java 2D, this direction is counterclockwise.
     * <p>A return value of -1 indicates that the line segment must
     * turn in the direction that takes the positive X axis towards
     * the positive Y axis.  In the default coordinate system, this
     * direction is clockwise.
     * <p>A return value of 0 indicates that the point lies
     * exactly on the line segment.  Note that an indicator value
     * of 0 is rare and not useful for determining colinearity
     * because of floating point rounding issues.
     * <p>If the point is colinear with the line segment, but
     * not between the end points, then the value will be -1 if the point
     * lies "beyond {@code (x1,y1)}" or 1 if the point lies
     * "beyond {@code (x2,y2)}".
     *
     * @param x1 the X coordinate of the start point of the
     *           specified line segment
     * @param y1 the Y coordinate of the start point of the
     *           specified line segment
     * @param x2 the X coordinate of the end point of the
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the
     *           specified line segment
     * @param px the X coordinate of the specified point to be
     *           compared with the specified line segment
     * @param py the Y coordinate of the specified point to be
     *           compared with the specified line segment
     * @return an integer that indicates the position of the third specified
     *          coordinates with respect to the line segment formed
     *          by the first two specified coordinates.
     */
    public static int relativeCCW(float x1, float y1,
                                  float x2, float y2,
                                  float px, float py)
    {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        float ccw = px * y2 - py * x2;
        if (ccw == 0.0f) {
            // The point is colinear, classify based on which side of
            // the segment the point falls on.  We can calculate a
            // relative value using the projection of px,py onto the
            // segment - a negative value indicates the point projects
            // outside of the segment in the direction of the particular
            // endpoint used as the origin for the projection.
            ccw = px * x2 + py * y2;
            if (ccw > 0.0f) {
                // Reverse the projection to be relative to the original x2,y2
                // x2 and y2 are simply negated.
                // px and py need to have (x2 - x1) or (y2 - y1) subtracted
                //    from them (based on the original values)
                // Since we really want to get a positive answer when the
                //    point is "beyond (x2,y2)", then we want to calculate
                //    the inverse anyway - thus we leave x2 & y2 negated.
                px -= x2;
                py -= y2;
                ccw = px * x2 + py * y2;
                if (ccw < 0.0f) {
                    ccw = 0.0f;
                }
            }
        }
        return Float.compare(ccw, 0.0f);
    }

    @Override
    public int hashCode() {
        int bits = java.lang.Float.floatToIntBits(x1);
        bits += java.lang.Float.floatToIntBits(y1) * 37;
        bits += java.lang.Float.floatToIntBits(x2) * 43;
        bits += java.lang.Float.floatToIntBits(y2) * 47;
        return bits;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Line2D) {
            Line2D line = (Line2D) obj;
            return ((x1 == line.x1) && (y1 == line.y1) &&
                    (x2 == line.x2) && (y2 == line.y2));
        }
        return false;
    }
}
