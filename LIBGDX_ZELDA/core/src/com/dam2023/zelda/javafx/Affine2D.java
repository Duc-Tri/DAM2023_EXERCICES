package com.dam2023.zelda.javafx;

import java.awt.geom.NoninvertibleTransformException;

/**
 * The <code>Affine2D</code> class represents a 2D affine transform
 * that performs a linear mapping from 2D coordinates to other 2D
 * coordinates that preserves the "straightness" and
 * "parallelness" of lines.  Affine transformations can be constructed
 * using sequences of translations, scales, flips, rotations, and shears.
 * <p>
 * Such a coordinate transformation can be represented by a 3 row by
 * 3 column matrix with an implied last row of [ 0 0 1 ].  This matrix
 * transforms source coordinates {@code (x,y)} into
 * destination coordinates {@code (x',y')} by considering
 * them to be a column vector and multiplying the coordinate vector
 * by the matrix according to the following process:
 * <pre>
 *  [ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
 *  [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
 *  [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
 * </pre>
 * <p>
 * <a name="quadrantapproximation"><h4>Handling 90-Degree Rotations</h4></a>
 * <p>
 * In some variations of the <code>rotate</code> methods in the
 * <code>Affine2D</code> class, a double-precision argument
 * specifies the angle of rotation in radians.
 * These methods have special handling for rotations of approximately
 * 90 degrees (including multiples such as 180, 270, and 360 degrees),
 * so that the common case of quadrant rotation is handled more
 * efficiently.
 * This special handling can cause angles very close to multiples of
 * 90 degrees to be treated as if they were exact multiples of
 * 90 degrees.
 * For small multiples of 90 degrees the range of angles treated
 * as a quadrant rotation is approximately 0.00000121 degrees wide.
 * This section explains why such special care is needed and how
 * it is implemented.
 * <p>
 * Since 90 degrees is represented as <code>PI/2</code> in radians,
 * and since PI is a transcendental (and therefore irrational) number,
 * it is not possible to exactly represent a multiple of 90 degrees as
 * an exact double precision value measured in radians.
 * As a result it is theoretically impossible to describe quadrant
 * rotations (90, 180, 270 or 360 degrees) using these values.
 * Double precision floating point values can get very close to
 * non-zero multiples of <code>PI/2</code> but never close enough
 * for the sine or cosine to be exactly 0.0, 1.0 or -1.0.
 * The implementations of <code>Math.sin()</code> and
 * <code>Math.cos()</code> correspondingly never return 0.0
 * for any case other than <code>Math.sin(0.0)</code>.
 * These same implementations do, however, return exactly 1.0 and
 * -1.0 for some range of numbers around each multiple of 90
 * degrees since the correct answer is so close to 1.0 or -1.0 that
 * the double precision significand cannot represent the difference
 * as accurately as it can for numbers that are near 0.0.
 * <p>
 * The net result of these issues is that if the
 * <code>Math.sin()</code> and <code>Math.cos()</code> methods
 * are used to directly generate the values for the matrix modifications
 * during these radian-based rotation operations then the resulting
 * transform is never strictly classifiable as a quadrant rotation
 * even for a simple case like <code>rotate(Math.PI/2.0)</code>,
 * due to minor variations in the matrix caused by the non-0.0 values
 * obtained for the sine and cosine.
 * If these transforms are not classified as quadrant rotations then
 * subsequent code which attempts to optimize further operations based
 * upon the type of the transform will be relegated to its most general
 * implementation.
 * <p>
 * Because quadrant rotations are fairly common,
 * this class should handle these cases reasonably quickly, both in
 * applying the rotations to the transform and in applying the resulting
 * transform to the coordinates.
 * To facilitate this optimal handling, the methods which take an angle
 * of rotation measured in radians attempt to detect angles that are
 * intended to be quadrant rotations and treat them as such.
 * These methods therefore treat an angle <em>theta</em> as a quadrant
 * rotation if either <code>Math.sin(<em>theta</em>)</code> or
 * <code>Math.cos(<em>theta</em>)</code> returns exactly 1.0 or -1.0.
 * As a rule of thumb, this property holds true for a range of
 * approximately 0.0000000211 radians (or 0.00000121 degrees) around
 * small multiples of <code>Math.PI/2.0</code>.
 *
 * @version 1.83, 05/05/07
 */
public class Affine2D extends AffineBase {
    private Affine2D(double mxx, double myx,
                     double mxy, double myy,
                     double mxt, double myt,
                     int state) {
        this.mxx = mxx;
        this.myx = myx;
        this.mxy = mxy;
        this.myy = myy;
        this.mxt = mxt;
        this.myt = myt;
        this.state = state;
        this.type = TYPE_UNKNOWN;
    }

    /**
     * Constructs a new <code>Affine2D</code> representing the
     * Identity transformation.
     */
    public Affine2D() {
        mxx = myy = 1.0;
    }

    /**
     * Constructs a new <code>Affine2D</code> that uses the same transform
     * as the specified <code>BaseTransform</code> object.
     *
     * @param Tx the <code>BaseTransform</code> object to copy
     */
    public Affine2D(BaseTransform Tx) {
        setTransform(Tx);
    }

    @Override
    public Degree getDegree() {
        return Degree.AFFINE_2D;
    }

    @Override
    protected void reset3Delements() { /* NOP for Affine2D */ }

    /**
     * Sets this transform to a translation transformation.
     * The matrix representing this transform becomes:
     * <pre>
     *      [   1    0    tx  ]
     *      [   0    1    ty  ]
     *      [   0    0    1   ]
     * </pre>
     *
     * @param tx the distance by which coordinates are translated in the
     *           X axis direction
     * @param ty the distance by which coordinates are translated in the
     *           Y axis direction
     */
    public void setToTranslation(double tx, double ty) {
        mxx = 1.0;
        myx = 0.0;
        mxy = 0.0;
        myy = 1.0;
        mxt = tx;
        myt = ty;
        if (tx != 0.0 || ty != 0.0) {
            state = APPLY_TRANSLATE;
            type = TYPE_TRANSLATION;
        } else {
            state = APPLY_IDENTITY;
            type = TYPE_IDENTITY;
        }
    }

    /**
     * Sets this transform to a copy of the transform in the specified
     * <code>BaseTransform</code> object.
     *
     * @param Tx the <code>BaseTransform</code> object from which to
     *           copy the transform
     */
    @Override
    public void setTransform(BaseTransform Tx) {
        switch (Tx.getDegree()) {
            case IDENTITY:
                setToIdentity();
                break;
            case TRANSLATE_2D:
                setToTranslation(Tx.getMxt(), Tx.getMyt());
                break;
            default:
                if (Tx.getType() > TYPE_AFFINE2D_MASK) {
                    System.out.println(Tx + " is " + Tx.getType());
                    System.out.print("  " + Tx.getMxx());
                    System.out.print(", " + Tx.getMxy());
                    System.out.print(", " + Tx.getMxz());
                    System.out.print(", " + Tx.getMxt());
                    System.out.println();
                    System.out.print("  " + Tx.getMyx());
                    System.out.print(", " + Tx.getMyy());
                    System.out.print(", " + Tx.getMyz());
                    System.out.print(", " + Tx.getMyt());
                    System.out.println();
                    System.out.print("  " + Tx.getMzx());
                    System.out.print(", " + Tx.getMzy());
                    System.out.print(", " + Tx.getMzz());
                    System.out.print(", " + Tx.getMzt());
                    System.out.println();
                    // TODO: Should this be thrown before we modify anything?
                    // (RT-26801)
                    degreeError(Degree.AFFINE_2D);
                }
                /* No Break */
            case AFFINE_2D:
                this.mxx = Tx.getMxx();
                this.myx = Tx.getMyx();
                this.mxy = Tx.getMxy();
                this.myy = Tx.getMyy();
                this.mxt = Tx.getMxt();
                this.myt = Tx.getMyt();
                if (Tx instanceof AffineBase) {
                    this.state = ((AffineBase) Tx).state;
                    this.type = ((AffineBase) Tx).type;
                } else {
                    updateState2D();
                }
                break;
        }
    }

    /**
     * Returns an <code>Affine2D</code> object representing the
     * inverse transformation.
     * The inverse transform Tx' of this transform Tx
     * maps coordinates transformed by Tx back
     * to their original coordinates.
     * In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
     * <p>
     * If this transform maps all coordinates onto a point or a line
     * then it will not have an inverse, since coordinates that do
     * not lie on the destination point or line will not have an inverse
     * mapping.
     * The <code>getDeterminant</code> method can be used to determine if this
     * transform has no inverse, in which case an exception will be
     * thrown if the <code>createInverse</code> method is called.
     *
     * @return a new <code>Affine2D</code> object representing the
     * inverse transformation.
     * @see #getDeterminant
     */
    @Override
    public Affine2D createInverse() throws NoninvertibleTransformException
    {
        double det;
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                det = mxx * myy - mxy * myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                return new Affine2D(myy / det, -myx / det,
                        -mxy / det, mxx / det,
                        (mxy * myt - myy * mxt) / det,
                        (myx * mxt - mxx * myt) / det,
                        (APPLY_SHEAR |
                                APPLY_SCALE |
                                APPLY_TRANSLATE));
            case (APPLY_SHEAR | APPLY_SCALE):
                det = mxx * myy - mxy * myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                return new Affine2D(myy / det, -myx / det,
                        -mxy / det, mxx / det,
                        0.0, 0.0,
                        (APPLY_SHEAR | APPLY_SCALE));
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                if (mxy == 0.0 || myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(0.0, 1.0 / mxy,
                        1.0 / myx, 0.0,
                        -myt / myx, -mxt / mxy,
                        (APPLY_SHEAR | APPLY_TRANSLATE));
            case (APPLY_SHEAR):
                if (mxy == 0.0 || myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(0.0, 1.0 / mxy,
                        1.0 / myx, 0.0,
                        0.0, 0.0,
                        (APPLY_SHEAR));
            case (APPLY_SCALE | APPLY_TRANSLATE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(1.0 / mxx, 0.0,
                        0.0, 1.0 / myy,
                        -mxt / mxx, -myt / myy,
                        (APPLY_SCALE | APPLY_TRANSLATE));
            case (APPLY_SCALE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(1.0 / mxx, 0.0,
                        0.0, 1.0 / myy,
                        0.0, 0.0,
                        (APPLY_SCALE));
            case (APPLY_TRANSLATE):
                return new Affine2D(1.0, 0.0,
                        0.0, 1.0,
                        -mxt, -myt,
                        (APPLY_TRANSLATE));
            case (APPLY_IDENTITY):
                return new Affine2D();
        }

        /* NOTREACHED */
    }

    /**
     * Transforms an array of point objects by this transform.
     * If any element of the <code>ptDst</code> array is
     * <code>null</code>, a new <code>Point2D</code> object is allocated
     * and stored into that element before storing the results of the
     * transformation.
     * <p>
     * Note that this method does not take any precautions to
     * avoid problems caused by storing results into <code>Point2D</code>
     * objects that will be used as the source for calculations
     * further down the source array.
     * This method does guarantee that if a specified <code>Point2D</code>
     * object is both the source and destination for the same single point
     * transform operation then the results will not be stored until
     * the calculations are complete to avoid storing the results on
     * top of the operands.
     * If, however, the destination <code>Point2D</code> object for one
     * operation is the same object as the source <code>Point2D</code>
     * object for another operation further down the source array then
     * the original coordinates in that point are overwritten before
     * they can be converted.
     *
     * @param ptSrc  the array containing the source point objects
     * @param ptDst  the array into which the transform point objects are
     *               returned
     * @param srcOff the offset to the first point object to be
     *               transformed in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point object that is stored in the destination array
     * @param numPts the number of point objects to be transformed
     */
    public void transform(Point2D[] ptSrc, int srcOff,
                          Point2D[] ptDst, int dstOff,
                          int numPts) {
        int mystate = this.state;
        while (--numPts >= 0) {
            // Copy source coords into local variables in case src == dst
            Point2D src = ptSrc[srcOff++];
            double x = src.x;
            double y = src.y;
            Point2D dst = ptDst[dstOff++];
            if (dst == null) {
                dst = new Point2D();
                ptDst[dstOff - 1] = dst;
            }
            switch (mystate) {
                default:
                    stateError();
                    /* NOTREACHED */
                case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                    dst.setLocation((float) (x * mxx + y * mxy + mxt),
                            (float) (x * myx + y * myy + myt));
                    break;
                case (APPLY_SHEAR | APPLY_SCALE):
                    dst.setLocation((float) (x * mxx + y * mxy),
                            (float) (x * myx + y * myy));
                    break;
                case (APPLY_SHEAR | APPLY_TRANSLATE):
                    dst.setLocation((float) (y * mxy + mxt),
                            (float) (x * myx + myt));
                    break;
                case (APPLY_SHEAR):
                    dst.setLocation((float) (y * mxy), (float) (x * myx));
                    break;
                case (APPLY_SCALE | APPLY_TRANSLATE):
                    dst.setLocation((float) (x * mxx + mxt), (float) (y * myy + myt));
                    break;
                case (APPLY_SCALE):
                    dst.setLocation((float) (x * mxx), (float) (y * myy));
                    break;
                case (APPLY_TRANSLATE):
                    dst.setLocation((float) (x + mxt), (float) (y + myt));
                    break;
                case (APPLY_IDENTITY):
                    dst.setLocation((float) x, (float) y);
                    break;
            }
        }

        /* NOTREACHED */
    }

    // Round values to sane precision for printing
    // Note that Math.sin(Math.PI) has an error of about 10^-16
    private static double _matround(double matval) {
        return Math.rint(matval * 1E15) / 1E15;
    }

    /**
     * Returns a <code>String</code> that represents the value of this
     * {@link Object}.
     *
     * @return a <code>String</code> representing the value of this
     * <code>Object</code>.
     */
    @Override
    public String toString() {
        return ("Affine2D[["
                + _matround(mxx) + ", "
                + _matround(mxy) + ", "
                + _matround(mxt) + "], ["
                + _matround(myx) + ", "
                + _matround(myy) + ", "
                + _matround(myt) + "]]");
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public BaseTransform copy() {
        return new Affine2D(this);
    }

    private static final long BASE_HASH;

    static {
        long bits = 0;
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz());
        BASE_HASH = bits;
    }

    /**
     * Returns the hashcode for this transform.  The base algorithm for
     * computing the hashcode is defined by the implementation in
     * the {@code BaseTransform} class.  This implementation is just a
     * faster way of computing the same value knowing which elements of
     * the transform matrix are populated.
     *
     * @return a hash code for this transform.
     */
    @Override
    public int hashCode() {
        if (isIdentity()) return 0;
        long bits = BASE_HASH;
        bits = bits * 31 + Double.doubleToLongBits(getMyy());
        bits = bits * 31 + Double.doubleToLongBits(getMyx());
        bits = bits * 31 + Double.doubleToLongBits(getMxy());
        bits = bits * 31 + Double.doubleToLongBits(getMxx());
        bits = bits * 31 + Double.doubleToLongBits(0.0); // mzt
        bits = bits * 31 + Double.doubleToLongBits(getMyt());
        bits = bits * 31 + Double.doubleToLongBits(getMxt());
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    /**
     * Returns <code>true</code> if this <code>Affine2D</code>
     * represents the same coordinate transform as the specified
     * argument.
     *
     * @param obj the <code>Object</code> to test for equality with this
     *            <code>Affine2D</code>
     * @return <code>true</code> if <code>obj</code> equals this
     * <code>Affine2D</code> object; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseTransform) {
            BaseTransform a = (BaseTransform) obj;
            return (a.getType() <= TYPE_AFFINE2D_MASK &&
                    a.getMxx() == this.mxx &&
                    a.getMxy() == this.mxy &&
                    a.getMxt() == this.mxt &&
                    a.getMyx() == this.myx &&
                    a.getMyy() == this.myy &&
                    a.getMyt() == this.myt);
        }
        return false;
    }
}
