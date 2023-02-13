package com.dam2023.zelda.javafx;


import java.util.Arrays;

/**
 * The <code>CubicCurve2D</code> class defines a cubic parametric curve
 * segment in {@code (x,y)} coordinate space.
 * <p>
 * This class is only the abstract superclass for all objects which
 * store a 2D cubic curve segment.
 * The actual storage representation of the coordinates is left to
 * the subclass.
 *
 * @version     1.42, 05/05/07
 */
public class CubicCurve2D extends Shape {
    /**
     * The X coordinate of the start point
     * of the cubic curve segment.
     */
    public float x1;

    /**
     * The Y coordinate of the start point
     * of the cubic curve segment.
     */
    public float y1;

    /**
     * The X coordinate of the first control point
     * of the cubic curve segment.
     */
    public float ctrlx1;

    /**
     * The Y coordinate of the first control point
     * of the cubic curve segment.
     */
    public float ctrly1;

    /**
     * The X coordinate of the second control point
     * of the cubic curve segment.
     */
    public float ctrlx2;

    /**
     * The Y coordinate of the second control point
     * of the cubic curve segment.
     */
    public float ctrly2;

    /**
     * The X coordinate of the end point
     * of the cubic curve segment.
     */
    public float x2;

    /**
     * The Y coordinate of the end point
     * of the cubic curve segment.
     */
    public float y2;

    /**
     * Constructs and initializes a {@code CubicCurve2D} from
     * the specified {@code float} coordinates.
     *
     * @param x1 the X coordinate for the start point
     *           of the resulting {@code CubicCurve2D}
     * @param y1 the Y coordinate for the start point
     *           of the resulting {@code CubicCurve2D}
     * @param ctrlx1 the X coordinate for the first control point
     *               of the resulting {@code CubicCurve2D}
     * @param ctrly1 the Y coordinate for the first control point
     *               of the resulting {@code CubicCurve2D}
     * @param ctrlx2 the X coordinate for the second control point
     *               of the resulting {@code CubicCurve2D}
     * @param ctrly2 the Y coordinate for the second control point
     *               of the resulting {@code CubicCurve2D}
     * @param x2 the X coordinate for the end point
     *           of the resulting {@code CubicCurve2D}
     * @param y2 the Y coordinate for the end point
     *           of the resulting {@code CubicCurve2D}
     */
    public CubicCurve2D(float x1, float y1,
                        float ctrlx1, float ctrly1,
                        float ctrlx2, float ctrly2,
                        float x2, float y2)
    {
        setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }

    /**
     * Sets the location of the end points and control points
     * of this curve to the specified {@code float} coordinates.
     *
     * @param x1 the X coordinate used to set the start point
     *           of this {@code CubicCurve2D}
     * @param y1 the Y coordinate used to set the start point
     *           of this {@code CubicCurve2D}
     * @param ctrlx1 the X coordinate used to set the first control point
     *               of this {@code CubicCurve2D}
     * @param ctrly1 the Y coordinate used to set the first control point
     *               of this {@code CubicCurve2D}
     * @param ctrlx2 the X coordinate used to set the second control point
     *               of this {@code CubicCurve2D}
     * @param ctrly2 the Y coordinate used to set the second control point
     *               of this {@code CubicCurve2D}
     * @param x2 the X coordinate used to set the end point
     *           of this {@code CubicCurve2D}
     * @param y2 the Y coordinate used to set the end point
     *           of this {@code CubicCurve2D}
     */
    public void setCurve(float x1, float y1,
                         float ctrlx1, float ctrly1,
                         float ctrlx2, float ctrly2,
                         float x2, float y2)
    {
        this.x1     = x1;
        this.y1     = y1;
        this.ctrlx1 = ctrlx1;
        this.ctrly1 = ctrly1;
        this.ctrlx2 = ctrlx2;
        this.ctrly2 = ctrly2;
        this.x2     = x2;
        this.y2     = y2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RectBounds getBounds() {
        float left   = Math.min(Math.min(x1, x2),
                Math.min(ctrlx1, ctrlx2));
        float top    = Math.min(Math.min(y1, y2),
                Math.min(ctrly1, ctrly2));
        float right  = Math.max(Math.max(x1, x2),
                Math.max(ctrlx1, ctrlx2));
        float bottom = Math.max(Math.max(y1, y2),
                Math.max(ctrly1, ctrly2));
        return new RectBounds(left, top, right, bottom);
    }

    /**
     * Solve the cubic whose coefficients are in the <code>eqn</code>
     * array and place the non-complex roots into the <code>res</code>
     * array, returning the number of roots.
     * The cubic solved is represented by the equation:
     *     eqn = {c, b, a, d}
     *     dx^3 + ax^2 + bx + c = 0
     * A return value of -1 is used to distinguish a constant equation,
     * which may be always 0 or never 0, from an equation which has no
     * zeroes.
     * @param eqn the specified array of coefficients to use to solve
     *        the cubic equation
     * @param res the array that contains the non-complex roots
     *        resulting from the solution of the cubic equation
     * @return the number of roots, or -1 if the equation is a constant
     */
    public static int solveCubic(float[] eqn, float[] res) {
        // From Numerical Recipes, 5.6, Quadratic and Cubic Equations
        float d = eqn[3];
        if (d == 0f) {
            // The cubic has degenerated to quadratic (or line or ...).
            return QuadCurve2D.solveQuadratic(eqn, res);
        }
        float a = eqn[2] / d;
        float b = eqn[1] / d;
        float c = eqn[0] / d;
        int roots = 0;
        float Q = (a * a - 3f * b) / 9f;
        float R = (2f * a * a * a - 9f * a * b + 27f * c) / 54f;
        float R2 = R * R;
        float Q3 = Q * Q * Q;
        a = a / 3f;
        if (R2 < Q3) {
            float theta = (float) Math.acos(R / Math.sqrt(Q3));
            Q = (float) (-2f * Math.sqrt(Q));
            if (res == eqn) {
                // Copy the eqn so that we don't clobber it with the
                // roots.  This is needed so that fixRoots can do its
                // work with the original equation.
                eqn = new float[4];
                System.arraycopy(res, 0, eqn, 0, 4);
            }
            res[roots++] = (float) (Q * Math.cos(theta / 3f) - a);
            res[roots++] = (float) (Q * Math.cos((theta + Math.PI * 2f)/ 3f) - a);
            res[roots++] = (float) (Q * Math.cos((theta - Math.PI * 2f)/ 3f) - a);
            fixRoots(res, eqn);
        } else {
            boolean neg = (R < 0f);
            float S = (float) Math.sqrt(R2 - Q3);
            if (neg) {
                R = -R;
            }
            float A = (float) Math.pow(R + S, 1f / 3f);
            if (!neg) {
                A = -A;
            }
            float B = (A == 0f) ? 0f : (Q / A);
            res[roots++] = (A + B) - a;
        }
        return roots;
    }

    /*
     * This pruning step is necessary since solveCubic uses the
     * cosine function to calculate the roots when there are 3
     * of them.  Since the cosine method can have an error of
     * +/- 1E-14 we need to make sure that we don't make any
     * bad decisions due to an error.
     *
     * If the root is not near one of the endpoints, then we will
     * only have a slight inaccuracy in calculating the x intercept
     * which will only cause a slightly wrong answer for some
     * points very close to the curve.  While the results in that
     * case are not as accurate as they could be, they are not
     * disastrously inaccurate either.
     *
     * On the other hand, if the error happens near one end of
     * the curve, then our processing to reject values outside
     * of the t=[0,1] range will fail and the results of that
     * failure will be disastrous since for an entire horizontal
     * range of test points, we will either overcount or undercount
     * the crossings and get a wrong answer for all of them, even
     * when they are clearly and obviously inside or outside the
     * curve.
     *
     * To work around this problem, we try a couple of Newton-Raphson
     * iterations to see if the true root is closer to the endpoint
     * or further away.  If it is further away, then we can stop
     * since we know we are on the right side of the endpoint.  If
     * we change direction, then either we are now being dragged away
     * from the endpoint in which case the first condition will cause
     * us to stop, or we have passed the endpoint and are headed back.
     * In the second case, we simply evaluate the slope at the
     * endpoint itself and place ourselves on the appropriate side
     * of it or on it depending on that result.
     */
    private static void fixRoots(float[] res, float[] eqn) {
        final float EPSILON = (float) 1E-5; // eek, Rich may have botched this
        for (int i = 0; i < 3; i++) {
            float t = res[i];
            if (Math.abs(t) < EPSILON) {
                res[i] = findZero(t, 0, eqn);
            } else if (Math.abs(t - 1) < EPSILON) {
                res[i] = findZero(t, 1, eqn);
            }
        }
    }

    private static float solveEqn(float[] eqn, int order, float t) {
        float v = eqn[order];
        while (--order >= 0) {
            v = v * t + eqn[order];
        }
        return v;
    }

    private static float findZero(float t, float target, float[] eqn) {
        float[] slopeqn = {eqn[1], 2*eqn[2], 3*eqn[3]};
        float slope;
        float origdelta = 0f;
        float origt = t;
        while (true) {
            slope = solveEqn(slopeqn, 2, t);
            if (slope == 0f) {
                // At a local minima - must return
                return t;
            }
            float y = solveEqn(eqn, 3, t);
            if (y == 0f) {
                // Found it! - return it
                return t;
            }
            // assert(slope != 0 && y != 0);
            float delta = - (y / slope);
            // assert(delta != 0);
            if (origdelta == 0f) {
                origdelta = delta;
            }
            if (t < target) {
                if (delta < 0f) return t;
            } else if (t > target) {
                if (delta > 0f) return t;
            } else { /* t == target */
                return (delta > 0f
                        ? (target + java.lang.Float.MIN_VALUE)
                        : (target - java.lang.Float.MIN_VALUE));
            }
            float newt = t + delta;
            if (t == newt) {
                // The deltas are so small that we aren't moving...
                return t;
            }
            if (delta * origdelta < 0) {
                // We have reversed our path.
                int tag = (origt < t
                        ? getTag(target, origt, t)
                        : getTag(target, t, origt));
                if (tag != INSIDE) {
                    // Local minima found away from target - return the middle
                    return (origt + t) / 2;
                }
                // Local minima somewhere near target - move to target
                // and let the slope determine the resulting t.
                t = target;
            } else {
                t = newt;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(float x, float y) {
        if (!(x * 0f + y * 0f == 0f)) {
            /* Either x or y was infinite or NaN.
             * A NaN always produces a negative response to any test
             * and Infinity values cannot be "inside" any path so
             * they should return false as well.
             */
            return false;
        }
        // We count the "Y" crossings to determine if the point is
        // inside the curve bounded by its closing line.
        int crossings =
                (Shape.pointCrossingsForLine(x, y, x1, y1, x2, y2) +
                        Shape.pointCrossingsForCubic(x, y,
                                x1, y1,
                                ctrlx1, ctrly1,
                                ctrlx2, ctrly2,
                                x2, y2, 0));
        return ((crossings & 1) == 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Point2D p) {
        return contains(p.x, p.y);
    }

    /*
     * Fill an array with the coefficients of the parametric equation
     * in t, ready for solving against val with solveCubic.
     * We currently have:
     * <pre>
     *   val = P(t) = C1(1-t)^3 + 3CP1 t(1-t)^2 + 3CP2 t^2(1-t) + C2 t^3
     *              = C1 - 3C1t + 3C1t^2 - C1t^3 +
     *                3CP1t - 6CP1t^2 + 3CP1t^3 +
     *                3CP2t^2 - 3CP2t^3 +
     *                C2t^3
     *            0 = (C1 - val) +
     *                (3CP1 - 3C1) t +
     *                (3C1 - 6CP1 + 3CP2) t^2 +
     *                (C2 - 3CP2 + 3CP1 - C1) t^3
     *            0 = C + Bt + At^2 + Dt^3
     *     C = C1 - val
     *     B = 3*CP1 - 3*C1
     *     A = 3*CP2 - 6*CP1 + 3*C1
     *     D = C2 - 3*CP2 + 3*CP1 - C1
     * </pre>
     */
    private static void fillEqn(float[] eqn, float val,
                                float c1, float cp1, float cp2, float c2) {
        eqn[0] = c1 - val;
        eqn[1] = (cp1 - c1) * 3f;
        eqn[2] = (cp2 - cp1 - cp1 + c1) * 3f;
        eqn[3] = c2 + (cp1 - cp2) * 3f - c1;
    }

    /*
     * Evaluate the t values in the first num slots of the vals[] array
     * and place the evaluated values back into the same array.  Only
     * evaluate t values that are within the range <0, 1>, including
     * the 0 and 1 ends of the range iff the include0 or include1
     * booleans are true.  If an "inflection" equation is handed in,
     * then any points which represent a point of inflection for that
     * cubic equation are also ignored.
     */
    private static int evalCubic(float[] vals, int num,
                                 float c1, float cp1,
                                 float cp2, float c2) {
        int j = 0;
        for (int i = 0; i < num; i++) {
            float t = vals[i];
            if (t >= 0 && t <= 1)
            {
                float u = 1 - t;
                vals[j++] = c1*u*u*u + 3*cp1*t*u*u + 3*cp2*t*t*u + c2*t*t*t;
            }
        }
        return j;
    }

    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    /*
     * Determine where coord lies with respect to the range from
     * low to high.  It is assumed that low <= high.  The return
     * value is one of the 5 values BELOW, LOWEDGE, INSIDE, HIGHEDGE,
     * or ABOVE.
     */
    private static int getTag(float coord, float low, float high) {
        if (coord <= low) {
            return (coord < low ? BELOW : LOWEDGE);
        }
        if (coord >= high) {
            return (coord > high ? ABOVE : HIGHEDGE);
        }
        return INSIDE;
    }

    /*
     * Determine if the pttag represents a coordinate that is already
     * in its test range, or is on the border with either of the two
     * opttags representing another coordinate that is "towards the
     * inside" of that test range.  In other words, are either of the
     * two "opt" points "drawing the pt inward"?
     */
    private static boolean inwards(int pttag, int opt1tag, int opt2tag) {
        switch (pttag) {
            case BELOW:
            case ABOVE:
            default:
                return false;
            case LOWEDGE:
                return (opt1tag >= INSIDE || opt2tag >= INSIDE);
            case INSIDE:
                return true;
            case HIGHEDGE:
                return (opt1tag <= INSIDE || opt2tag <= INSIDE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean intersects(float x, float y, float w, float h) {
        // Trivially reject non-existant rectangles
        if (w <= 0 || h <= 0) {
            return false;
        }

        // Trivially accept if either endpoint is inside the rectangle
        // (not on its border since it may end there and not go inside)
        // Record where they lie with respect to the rectangle.
        //     -1 => left, 0 => inside, 1 => right
        float x1 = this.x1;
        float y1 = this.y1;
        int x1tag = getTag(x1, x, x + w);
        int y1tag = getTag(y1, y, y + h);
        if (x1tag == INSIDE && y1tag == INSIDE) {
            return true;
        }
        float x2 = this.x2;
        float y2 = this.y2;
        int x2tag = getTag(x2, x, x + w);
        int y2tag = getTag(y2, y, y + h);
        if (x2tag == INSIDE && y2tag == INSIDE) {
            return true;
        }

        float ctrlx1 = this.ctrlx1;
        float ctrly1 = this.ctrly1;
        float ctrlx2 = this.ctrlx2;
        float ctrly2 = this.ctrly2;
        int ctrlx1tag = getTag(ctrlx1, x, x + w);
        int ctrly1tag = getTag(ctrly1, y, y + h);
        int ctrlx2tag = getTag(ctrlx2, x, x + w);
        int ctrly2tag = getTag(ctrly2, y, y + h);

        // Trivially reject if all points are entirely to one side of
        // the rectangle.
        if (x1tag < INSIDE && x2tag < INSIDE &&
                ctrlx1tag < INSIDE && ctrlx2tag < INSIDE)
        {
            return false;   // All points left
        }
        if (y1tag < INSIDE && y2tag < INSIDE &&
                ctrly1tag < INSIDE && ctrly2tag < INSIDE)
        {
            return false;   // All points above
        }
        if (x1tag > INSIDE && x2tag > INSIDE &&
                ctrlx1tag > INSIDE && ctrlx2tag > INSIDE)
        {
            return false;   // All points right
        }
        if (y1tag > INSIDE && y2tag > INSIDE &&
                ctrly1tag > INSIDE && ctrly2tag > INSIDE)
        {
            return false;   // All points below
        }

        // Test for endpoints on the edge where either the segment
        // or the curve is headed "inwards" from them
        // Note: These tests are a superset of the fast endpoint tests
        //       above and thus repeat those tests, but take more time
        //       and cover more cases
        if (inwards(x1tag, x2tag, ctrlx1tag) &&
                inwards(y1tag, y2tag, ctrly1tag))
        {
            // First endpoint on border with either edge moving inside
            return true;
        }
        if (inwards(x2tag, x1tag, ctrlx2tag) &&
                inwards(y2tag, y1tag, ctrly2tag))
        {
            // Second endpoint on border with either edge moving inside
            return true;
        }

        // Trivially accept if endpoints span directly across the rectangle
        boolean xoverlap = (x1tag * x2tag <= 0);
        boolean yoverlap = (y1tag * y2tag <= 0);
        if (x1tag == INSIDE && x2tag == INSIDE && yoverlap) {
            return true;
        }
        if (y1tag == INSIDE && y2tag == INSIDE && xoverlap) {
            return true;
        }

        // We now know that both endpoints are outside the rectangle
        // but the 4 points are not all on one side of the rectangle.
        // Therefore the curve cannot be contained inside the rectangle,
        // but the rectangle might be contained inside the curve, or
        // the curve might intersect the boundary of the rectangle.

        float[] eqn = new float[4];
        float[] res = new float[4];
        if (!yoverlap) {
            // Both y coordinates for the closing segment are above or
            // below the rectangle which means that we can only intersect
            // if the curve crosses the top (or bottom) of the rectangle
            // in more than one place and if those crossing locations
            // span the horizontal range of the rectangle.
            fillEqn(eqn, (y1tag < INSIDE ? y : y+h), y1, ctrly1, ctrly2, y2);
            int num = solveCubic(eqn, res);
            num = evalCubic(res, num,
                    x1, ctrlx1, ctrlx2, x2);
            // odd counts imply the crossing was out of [0,1] bounds
            // otherwise there is no way for that part of the curve to
            // "return" to meet its endpoint
            return (num == 2 &&
                    getTag(res[0], x, x+w) * getTag(res[1], x, x+w) <= 0);
        }

        // Y ranges overlap.  Now we examine the X ranges
        if (!xoverlap) {
            // Both x coordinates for the closing segment are left of
            // or right of the rectangle which means that we can only
            // intersect if the curve crosses the left (or right) edge
            // of the rectangle in more than one place and if those
            // crossing locations span the vertical range of the rectangle.
            fillEqn(eqn, (x1tag < INSIDE ? x : x+w), x1, ctrlx1, ctrlx2, x2);
            int num = solveCubic(eqn, res);
            num = evalCubic(res, num,
                    y1, ctrly1, ctrly2, y2);
            // odd counts imply the crossing was out of [0,1] bounds
            // otherwise there is no way for that part of the curve to
            // "return" to meet its endpoint
            return (num == 2 &&
                    getTag(res[0], y, y+h) * getTag(res[1], y, y+h) <= 0);
        }

        // The X and Y ranges of the endpoints overlap the X and Y
        // ranges of the rectangle, now find out how the endpoint
        // line segment intersects the Y range of the rectangle
        float dx = x2 - x1;
        float dy = y2 - y1;
        float k = y2 * x1 - x2 * y1;
        int c1tag, c2tag;
        if (y1tag == INSIDE) {
            c1tag = x1tag;
        } else {
            c1tag = getTag((k + dx * (y1tag < INSIDE ? y : y+h)) / dy, x, x+w);
        }
        if (y2tag == INSIDE) {
            c2tag = x2tag;
        } else {
            c2tag = getTag((k + dx * (y2tag < INSIDE ? y : y+h)) / dy, x, x+w);
        }
        // If the part of the line segment that intersects the Y range
        // of the rectangle crosses it horizontally - trivially accept
        if (c1tag * c2tag <= 0) {
            return true;
        }

        // Now we know that both the X and Y ranges intersect and that
        // the endpoint line segment does not directly cross the rectangle.
        //
        // We can almost treat this case like one of the cases above
        // where both endpoints are to one side, except that we may
        // get one or three intersections of the curve with the vertical
        // side of the rectangle.  This is because the endpoint segment
        // accounts for the other intersection in an even pairing.  Thus,
        // with the endpoint crossing we end up with 2 or 4 total crossings.
        //
        // (Remember there is overlap in both the X and Y ranges which
        //  means that the segment itself must cross at least one vertical
        //  edge of the rectangle - in particular, the "near vertical side"
        //  - leaving an odd number of intersections for the curve.)
        //
        // Now we calculate the y tags of all the intersections on the
        // "near vertical side" of the rectangle.  We will have one with
        // the endpoint segment, and one or three with the curve.  If
        // any pair of those vertical intersections overlap the Y range
        // of the rectangle, we have an intersection.  Otherwise, we don't.

        // c1tag = vertical intersection class of the endpoint segment
        //
        // Choose the y tag of the endpoint that was not on the same
        // side of the rectangle as the subsegment calculated above.
        // Note that we can "steal" the existing Y tag of that endpoint
        // since it will be provably the same as the vertical intersection.
        c1tag = ((c1tag * x1tag <= 0) ? y1tag : y2tag);

        // Now we have to calculate an array of solutions of the curve
        // with the "near vertical side" of the rectangle.  Then we
        // need to sort the tags and do a pairwise range test to see
        // if either of the pairs of crossings spans the Y range of
        // the rectangle.
        //
        // Note that the c2tag can still tell us which vertical edge
        // to test against.
        fillEqn(eqn, (c2tag < INSIDE ? x : x+w), x1, ctrlx1, ctrlx2, x2);
        int num = solveCubic(eqn, res);
        num = evalCubic(res, num, y1, ctrly1, ctrly2, y2);

        // Now put all of the tags into a bucket and sort them.  There
        // is an intersection iff one of the pairs of tags "spans" the
        // Y range of the rectangle.
        int[] tags = new int[num+1];
        for (int i = 0; i < num; i++) {
            tags[i] = getTag(res[i], y, y+h);
        }
        tags[num] = c1tag;
        Arrays.sort(tags);
        return ((num >= 1 && tags[0] * tags[1] <= 0) ||
                (num >= 3 && tags[2] * tags[3] <= 0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(float x, float y, float w, float h) {
        if (w <= 0 || h <= 0) {
            return false;
        }
        // Assertion: Cubic curves closed by connecting their
        // endpoints form either one or two convex halves with
        // the closing line segment as an edge of both sides.
        if (!(contains(x, y) &&
                contains(x + w, y) &&
                contains(x + w, y + h) &&
                contains(x, y + h))) {
            return false;
        }
        // Either the rectangle is entirely inside one of the convex
        // halves or it crosses from one to the other, in which case
        // it must intersect the closing line segment.
        return !Shape.intersectsLine(x, y, w, h, x1, y1, x2, y2);
    }

    /**
     * Returns an iteration object that defines the boundary of the
     * shape.
     * The iterator for this class is not multi-threaded safe,
     * which means that this <code>CubicCurve2D</code> class does not
     * guarantee that modifications to the geometry of this
     * <code>CubicCurve2D</code> object do not affect any iterations of
     * that geometry that are already in process.
     * @param tx an optional <code>BaseTransform</code> to be applied to the
     * coordinates as they are returned in the iteration, or <code>null</code>
     * if untransformed coordinates are desired
     * @return    the <code>PathIterator</code> object that returns the
     *          geometry of the outline of this <code>CubicCurve2D</code>, one
     *          segment at a time.
     */
    @Override
    public PathIterator getPathIterator(BaseTransform tx) {
        return new CubicIterator(this, tx);
    }

    @Override
    public CubicCurve2D copy() {
        return new CubicCurve2D(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }

    @Override
    public int hashCode() {
        int bits = java.lang.Float.floatToIntBits(x1);
        bits += java.lang.Float.floatToIntBits(y1) * 37;
        bits += java.lang.Float.floatToIntBits(x2) * 43;
        bits += java.lang.Float.floatToIntBits(y2) * 47;
        bits += java.lang.Float.floatToIntBits(ctrlx1) * 53;
        bits += java.lang.Float.floatToIntBits(ctrly1) * 59;
        bits += java.lang.Float.floatToIntBits(ctrlx2) * 61;
        bits += java.lang.Float.floatToIntBits(ctrly2) * 101;
        return bits;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CubicCurve2D) {
            CubicCurve2D curve = (CubicCurve2D) obj;
            return ((x1 == curve.x1) && (y1 == curve.y1) &&
                    (x2 == curve.x2) && (y2 == curve.y2) &&
                    (ctrlx1 == curve.ctrlx1) && (ctrly1 == curve.ctrly1) &&
                    (ctrlx2 == curve.ctrlx2) && (ctrly2 == curve.ctrly2));
        }
        return false;
    }

}
