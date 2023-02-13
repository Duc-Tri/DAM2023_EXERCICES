package com.dam2023.zelda.javafx;


import java.awt.geom.NoninvertibleTransformException;

/**
 *
 */
public abstract class AffineBase extends BaseTransform {
    /**
     * This constant is used for the internal state variable to indicate
     * that no calculations need to be performed and that the source
     * coordinates only need to be copied to their destinations to
     * complete the transformation equation of this transform.
     *
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SCALE
     * @see #APPLY_SHEAR
     * @see #APPLY_3D
     * @see #state
     */
    protected static final int APPLY_IDENTITY = 0;

    /**
     * This constant is used for the internal state variable to indicate
     * that the translation components of the matrix (m02 and m12) need
     * to be added to complete the transformation equation of this transform.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_SCALE
     * @see #APPLY_SHEAR
     * @see #APPLY_3D
     * @see #state
     */
    protected static final int APPLY_TRANSLATE = 1;

    /**
     * This constant is used for the internal state variable to indicate
     * that the scaling components of the matrix (m00 and m11) need
     * to be factored in to complete the transformation equation of
     * this transform.  If the APPLY_SHEAR bit is also set then it
     * indicates that the scaling components are not both 0.0.  If the
     * APPLY_SHEAR bit is not also set then it indicates that the
     * scaling components are not both 1.0.  If neither the APPLY_SHEAR
     * nor the APPLY_SCALE bits are set then the scaling components
     * are both 1.0, which means that the x and y components contribute
     * to the transformed coordinate, but they are not multiplied by
     * any scaling factor.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SHEAR
     * @see #APPLY_3D
     * @see #state
     */
    protected static final int APPLY_SCALE = 2;

    /**
     * This constant is used for the internal state variable to indicate
     * that the shearing components of the matrix (m01 and m10) need
     * to be factored in to complete the transformation equation of this
     * transform.  The presence of this bit in the state variable changes
     * the interpretation of the APPLY_SCALE bit as indicated in its
     * documentation.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SCALE
     * @see #APPLY_3D
     * @see #state
     */
    protected static final int APPLY_SHEAR = 4;

    /**
     * This constant is used for the internal state variable to indicate
     * that the 3D (Z) components of the matrix (m*z and mz*) need
     * to be factored in to complete the transformation equation of this
     * transform.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SCALE
     * @see #APPLY_SHEAR
     * @see #state
     */
    protected static final int APPLY_3D = 8;

    /*
     * The following mask can be used to extract the 2D state constants from
     * a state variable for cases where we know we can ignore the 3D matrix
     * elements (such as in the 2D coordinate transform methods).
     */
    protected static final int APPLY_2D_MASK = (APPLY_TRANSLATE | APPLY_SCALE | APPLY_SHEAR);
    protected static final int APPLY_2D_DELTA_MASK = (APPLY_SCALE | APPLY_SHEAR);

    /**
     * The X coordinate scaling element of the 3x3
     * affine transformation matrix.
     */
    protected double mxx;

    /**
     * The Y coordinate shearing element of the 3x3
     * affine transformation matrix.
     */
    protected double myx;

    /**
     * The X coordinate shearing element of the 3x3
     * affine transformation matrix.
     */
    protected double mxy;

    /**
     * The Y coordinate scaling element of the 3x3
     * affine transformation matrix.
     */
    protected double myy;

    /**
     * The X coordinate of the translation element of the
     * 3x3 affine transformation matrix.
     */
    protected double mxt;

    /**
     * The Y coordinate of the translation element of the
     * 3x3 affine transformation matrix.
     */
    protected double myt;

    /**
     * This field keeps track of which components of the matrix need to
     * be applied when performing a transformation.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SCALE
     * @see #APPLY_SHEAR
     * @see #APPLY_3D
     */
    protected transient int state;

    /**
     * This field caches the current transformation type of the matrix.
     *
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #TYPE_UNKNOWN
     * @see #getType
     */
    protected transient int type;

    /*
     * Convenience method used internally to throw exceptions when
     * a case was forgotten in a switch statement.
     */
    protected static void stateError() {
        throw new InternalError("missing case in transform state switch");
    }

    /**
     * Manually recalculates the state of the transform when the matrix
     * changes too much to predict the effects on the state.
     * The following table specifies what the various settings of the
     * state field say about the values of the corresponding matrix
     * element fields.
     * Note that the rules governing the SCALE fields are slightly
     * different depending on whether the SHEAR flag is also set.
     * <pre>
     *                     SCALE            SHEAR          TRANSLATE
     *                    m00/m11          m01/m10          m02/m12
     *
     * IDENTITY             1.0              0.0              0.0
     * TRANSLATE (TR)       1.0              0.0          not both 0.0
     * SCALE (SC)       not both 1.0         0.0              0.0
     * TR | SC          not both 1.0         0.0          not both 0.0
     * SHEAR (SH)           0.0          not both 0.0         0.0
     * TR | SH              0.0          not both 0.0     not both 0.0
     * SC | SH          not both 0.0     not both 0.0         0.0
     * TR | SC | SH     not both 0.0     not both 0.0     not both 0.0
     * </pre>
     */
    protected void updateState() {
        updateState2D();
    }

    /*
     * This variant of the method is for cases where we know the 3D elements
     * are set to identity...
     */
    protected void updateState2D() {
        if (mxy == 0.0 && myx == 0.0) {
            if (mxx == 1.0 && myy == 1.0) {
                if (mxt == 0.0 && myt == 0.0) {
                    state = APPLY_IDENTITY;
                    type = TYPE_IDENTITY;
                } else {
                    state = APPLY_TRANSLATE;
                    type = TYPE_TRANSLATION;
                }
            } else {
                if (mxt == 0.0 && myt == 0.0) {
                    state = APPLY_SCALE;
                } else {
                    state = (APPLY_SCALE | APPLY_TRANSLATE);
                }
                type = TYPE_UNKNOWN;
            }
        } else {
            if (mxx == 0.0 && myy == 0.0) {
                if (mxt == 0.0 && myt == 0.0) {
                    state = APPLY_SHEAR;
                } else {
                    state = (APPLY_SHEAR | APPLY_TRANSLATE);
                }
            } else {
                if (mxt == 0.0 && myt == 0.0) {
                    state = (APPLY_SHEAR | APPLY_SCALE);
                } else {
                    state = (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE);
                }
            }
            type = TYPE_UNKNOWN;
        }
    }

    @Override
    public int getType() {
        if (type == TYPE_UNKNOWN) {
            updateState(); // TODO: Is this really needed? (RT-26884)
            if (type == TYPE_UNKNOWN) {
                type = calculateType();
            }
        }
        return type;
    }

    protected int calculateType() {
        int ret = ((state & APPLY_3D) == 0) ? TYPE_IDENTITY : TYPE_AFFINE_3D;
        boolean sgn0, sgn1;
        switch (state & APPLY_2D_MASK) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                ret |= TYPE_TRANSLATION;
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_SCALE):
                if (mxx * mxy + myx * myy != 0) {
                    // Transformed unit vectors are not perpendicular...
                    ret |= TYPE_GENERAL_TRANSFORM;
                    break;
                }
                sgn0 = (mxx >= 0.0);
                sgn1 = (myy >= 0.0);
                if (sgn0 == sgn1) {
                    // sgn(mxx) == sgn(myy) therefore sgn(mxy) == -sgn(myx)
                    // This is the "unflipped" (right-handed) state
                    if (mxx != myy || mxy != -myx) {
                        ret |= (TYPE_GENERAL_ROTATION | TYPE_GENERAL_SCALE);
                    } else if (mxx * myy - mxy * myx != 1.0) {
                        ret |= (TYPE_GENERAL_ROTATION | TYPE_UNIFORM_SCALE);
                    } else {
                        ret |= TYPE_GENERAL_ROTATION;
                    }
                } else {
                    // sgn(mxx) == -sgn(myy) therefore sgn(mxy) == sgn(myx)
                    // This is the "flipped" (left-handed) state
                    if (mxx != -myy || mxy != myx) {
                        ret |= (TYPE_GENERAL_ROTATION |
                                TYPE_FLIP |
                                TYPE_GENERAL_SCALE);
                    } else if (mxx * myy - mxy * myx != 1.0) {
                        ret |= (TYPE_GENERAL_ROTATION |
                                TYPE_FLIP |
                                TYPE_UNIFORM_SCALE);
                    } else {
                        ret |= (TYPE_GENERAL_ROTATION | TYPE_FLIP);
                    }
                }
                break;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                ret |= TYPE_TRANSLATION;
                /* NOBREAK */
            case (APPLY_SHEAR):
                sgn0 = (mxy >= 0.0);
                sgn1 = (myx >= 0.0);
                if (sgn0 != sgn1) {
                    // Different signs - simple 90 degree rotation
                    if (mxy != -myx) {
                        ret |= (TYPE_QUADRANT_ROTATION | TYPE_GENERAL_SCALE);
                    } else if (mxy != 1.0 && mxy != -1.0) {
                        ret |= (TYPE_QUADRANT_ROTATION | TYPE_UNIFORM_SCALE);
                    } else {
                        ret |= TYPE_QUADRANT_ROTATION;
                    }
                } else {
                    // Same signs - 90 degree rotation plus an axis flip too
                    if (mxy == myx) {
                        ret |= (TYPE_QUADRANT_ROTATION |
                                TYPE_FLIP |
                                TYPE_UNIFORM_SCALE);
                    } else {
                        ret |= (TYPE_QUADRANT_ROTATION |
                                TYPE_FLIP |
                                TYPE_GENERAL_SCALE);
                    }
                }
                break;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                ret |= TYPE_TRANSLATION;
                /* NOBREAK */
            case (APPLY_SCALE):
                sgn0 = (mxx >= 0.0);
                sgn1 = (myy >= 0.0);
                if (sgn0 == sgn1) {
                    if (sgn0) {
                        // Both scaling factors non-negative - simple scale
                        // Note: APPLY_SCALE implies M0, M1 are not both 1
                        if (mxx == myy) {
                            ret |= TYPE_UNIFORM_SCALE;
                        } else {
                            ret |= TYPE_GENERAL_SCALE;
                        }
                    } else {
                        // Both scaling factors negative - 180 degree rotation
                        if (mxx != myy) {
                            ret |= (TYPE_QUADRANT_ROTATION | TYPE_GENERAL_SCALE);
                        } else if (mxx != -1.0) {
                            ret |= (TYPE_QUADRANT_ROTATION | TYPE_UNIFORM_SCALE);
                        } else {
                            ret |= TYPE_QUADRANT_ROTATION;
                        }
                    }
                } else {
                    // Scaling factor signs different - flip about some axis
                    if (mxx == -myy) {
                        if (mxx == 1.0 || mxx == -1.0) {
                            ret |= TYPE_FLIP;
                        } else {
                            ret |= (TYPE_FLIP | TYPE_UNIFORM_SCALE);
                        }
                    } else {
                        ret |= (TYPE_FLIP | TYPE_GENERAL_SCALE);
                    }
                }
                break;
            case (APPLY_TRANSLATE):
                ret |= TYPE_TRANSLATION;
                break;
            case (APPLY_IDENTITY):
                break;
        }
        return ret;
    }

    /**
     * Returns the X coordinate scaling element (mxx) of the 3x3
     * affine transformation matrix.
     *
     * @return a double value that is the X coordinate of the scaling
     * element of the affine transformation matrix.
     */
    @Override
    public double getMxx() {
        return mxx;
    }

    /**
     * Returns the Y coordinate scaling element (myy) of the 3x3
     * affine transformation matrix.
     *
     * @return a double value that is the Y coordinate of the scaling
     * element of the affine transformation matrix.
     */
    @Override
    public double getMyy() {
        return myy;
    }

    /**
     * Returns the X coordinate shearing element (mxy) of the 3x3
     * affine transformation matrix.
     *
     * @return a double value that is the X coordinate of the shearing
     * element of the affine transformation matrix.
     */
    @Override
    public double getMxy() {
        return mxy;
    }

    /**
     * Returns the Y coordinate shearing element (myx) of the 3x3
     * affine transformation matrix.
     *
     * @return a double value that is the Y coordinate of the shearing
     * element of the affine transformation matrix.
     */
    @Override
    public double getMyx() {
        return myx;
    }

    /**
     * Returns the X coordinate of the translation element (mxt) of the
     * 3x3 affine transformation matrix.
     *
     * @return a double value that is the X coordinate of the translation
     * element of the affine transformation matrix.
     */
    @Override
    public double getMxt() {
        return mxt;
    }

    /**
     * Returns the Y coordinate of the translation element (myt) of the
     * 3x3 affine transformation matrix.
     *
     * @return a double value that is the Y coordinate of the translation
     * element of the affine transformation matrix.
     */
    @Override
    public double getMyt() {
        return myt;
    }

    /**
     * Returns <code>true</code> if this <code>Affine2D</code> is
     * an identity transform.
     *
     * @return <code>true</code> if this <code>Affine2D</code> is
     * an identity transform; <code>false</code> otherwise.
     */
    @Override
    public boolean isIdentity() {
        return (state == APPLY_IDENTITY || (getType() == TYPE_IDENTITY));
    }

    @Override
    public boolean isTranslateOrIdentity() {
        return (state <= APPLY_TRANSLATE || (getType() <= TYPE_TRANSLATION));
    }

    @Override
    public boolean is2D() {
        return (state < APPLY_3D || getType() <= TYPE_AFFINE2D_MASK);
    }

    /**
     * Returns the determinant of the matrix representation of the transform.
     * The determinant is useful both to determine if the transform can
     * be inverted and to get a single value representing the
     * combined X and Y scaling of the transform.
     * <p>
     * If the determinant is non-zero, then this transform is
     * invertible and the various methods that depend on the inverse
     * transform do not need to throw a
     * {@link NoninvertibleTransformException}.
     * If the determinant is zero then this transform can not be
     * inverted since the transform maps all input coordinates onto
     * a line or a point.
     * If the determinant is near enough to zero then inverse transform
     * operations might not carry enough precision to produce meaningful
     * results.
     * <p>
     * If this transform represents a uniform scale, as indicated by
     * the <code>getType</code> method then the determinant also
     * represents the square of the uniform scale factor by which all of
     * the points are expanded from or contracted towards the origin.
     * If this transform represents a non-uniform scale or more general
     * transform then the determinant is not likely to represent a
     * value useful for any purpose other than determining if inverse
     * transforms are possible.
     * <p>
     * Mathematically, the determinant is calculated using the formula:
     * <pre>
     *      |  mxx  mxy  mxt  |
     *      |  myx  myy  myt  |  =  mxx * myy - mxy * myx
     *      |   0    0    1   |
     * </pre>
     *
     * @return the determinant of the matrix used to transform the
     * coordinates.
     * @see #getType
     * @see #createInverse
     * @see #inverseTransform
     * @see #TYPE_UNIFORM_SCALE
     */
    @Override
    public double getDeterminant() {
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SHEAR | APPLY_SCALE):
                return mxx * myy - mxy * myx;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
            case (APPLY_SHEAR):
                return -(mxy * myx);
            case (APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SCALE):
                return mxx * myy;
            case (APPLY_TRANSLATE):
            case (APPLY_IDENTITY):
                return 1.0;
        }
    }

    /**
     * Resets the 3D (Z) components of the matrix to identity settings
     * (if they are present).
     * This is a NOP unless the transform is Affine3D in which case it
     * needs to reset its added fields.
     */
    protected abstract void reset3Delements();

    /**
     * Resets this transform to the Identity transform.
     */
    @Override
    public void setToIdentity() {
        mxx = myy = 1.0;
        myx = mxy = mxt = myt = 0.0;
        reset3Delements();
        state = APPLY_IDENTITY;
        type = TYPE_IDENTITY;
    }

    public Point2D transform(Point2D pt) {
        return transform(pt, pt);
    }

    /**
     * Transforms the specified <code>ptSrc</code> and stores the result
     * in <code>ptDst</code>.
     * If <code>ptDst</code> is <code>null</code>, a new {@link Point2D}
     * object is allocated and then the result of the transformation is
     * stored in this object.
     * In either case, <code>ptDst</code>, which contains the
     * transformed point, is returned for convenience.
     * If <code>ptSrc</code> and <code>ptDst</code> are the same
     * object, the input point is correctly overwritten with
     * the transformed point.
     *
     * @param ptSrc the specified <code>Point2D</code> to be transformed
     * @param ptDst the specified <code>Point2D</code> that stores the
     *              result of transforming <code>ptSrc</code>
     * @return the <code>ptDst</code> after transforming
     * <code>ptSrc</code> and stroring the result in <code>ptDst</code>.
     */
    @Override
    public Point2D transform(Point2D ptSrc, Point2D ptDst) {
        if (ptDst == null) {
            ptDst = new Point2D();
        }
        // Copy source coords into local variables in case src == dst
        double x = ptSrc.x;
        double y = ptSrc.y;
        // double z = 0.0
        // Note that this method also works for 3D transforms since the
        // mxz and myz matrix elements get multiplied by z (0.0) and the
        // mzx, mzy, mzz, and mzt elements only get used to calculate
        // the resulting Z coordinate, which we drop (ignore).
        switch (state & APPLY_2D_MASK) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                ptDst.setLocation((float) (x * mxx + y * mxy + mxt),
                        (float) (x * myx + y * myy + myt));
                return ptDst;
            case (APPLY_SHEAR | APPLY_SCALE):
                ptDst.setLocation((float) (x * mxx + y * mxy),
                        (float) (x * myx + y * myy));
                return ptDst;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                ptDst.setLocation((float) (y * mxy + mxt),
                        (float) (x * myx + myt));
                return ptDst;
            case (APPLY_SHEAR):
                ptDst.setLocation((float) (y * mxy), (float) (x * myx));
                return ptDst;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                ptDst.setLocation((float) (x * mxx + mxt), (float) (y * myy + myt));
                return ptDst;
            case (APPLY_SCALE):
                ptDst.setLocation((float) (x * mxx), (float) (y * myy));
                return ptDst;
            case (APPLY_TRANSLATE):
                ptDst.setLocation((float) (x + mxt), (float) (y + myt));
                return ptDst;
            case (APPLY_IDENTITY):
                ptDst.setLocation((float) x, (float) y);
                return ptDst;
        }

        /* NOTREACHED */
    }

    @Override
    public Vec3d transform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        // Copy source coords into local variables in case src == dst
        double x = src.x;
        double y = src.y;
        double z = src.z;
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                dst.x = x * mxx + y * mxy + mxt;
                dst.y = x * myx + y * myy + myt;
                dst.z = z;
                return dst;
            case (APPLY_SHEAR | APPLY_SCALE):
                dst.x = x * mxx + y * mxy;
                dst.y = x * myx + y * myy;
                dst.z = z;
                return dst;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                dst.x = y * mxy + mxt;
                dst.y = x * myx + myt;
                dst.z = z;
                return dst;
            case (APPLY_SHEAR):
                dst.x = y * mxy;
                dst.y = x * myx;
                dst.z = z;
                return dst;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                dst.x = x * mxx + mxt;
                dst.y = y * myy + myt;
                dst.z = z;
                return dst;
            case (APPLY_SCALE):
                dst.x = x * mxx;
                dst.y = y * myy;
                dst.z = z;
                return dst;
            case (APPLY_TRANSLATE):
                dst.x = x + mxt;
                dst.y = y + myt;
                dst.z = z;
                return dst;
            case (APPLY_IDENTITY):
                dst.x = x;
                dst.y = y;
                dst.z = z;
                return dst;
        }

        /* NOTREACHED */
    }

    /**
     * Transforms the specified <code>src</code> vector and stores the result
     * in <code>dst</code> vector, without applying the translation elements.
     * If <code>dst</code> is <code>null</code>, a new {@link Vec3d}
     * object is allocated and then the result of the transformation is
     * stored in this object.
     * In either case, <code>dst</code>, which contains the
     * transformed vector, is returned for convenience.
     * If <code>src</code> and <code>dst</code> are the same
     * object, the input vector is correctly overwritten with
     * the transformed vector.
     *
     * @param src the specified <code>Vec3d</code> to be transformed
     * @param dst the specified <code>Vec3d</code> that stores the
     *            result of transforming <code>src</code>
     * @return the <code>dst</code> vector after transforming
     * <code>src</code> and storing the result in <code>dst</code>.
     * @since JavaFX 8.0
     */
    @Override
    public Vec3d deltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        // Copy source coords into local variables in case src == dst
        double x = src.x;
        double y = src.y;
        double z = src.z;
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SHEAR | APPLY_SCALE):
                dst.x = x * mxx + y * mxy;
                dst.y = x * myx + y * myy;
                dst.z = z;
                return dst;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
            case (APPLY_SHEAR):
                dst.x = y * mxy;
                dst.y = x * myx;
                dst.z = z;
                return dst;
            case (APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SCALE):
                dst.x = x * mxx;
                dst.y = y * myy;
                dst.z = z;
                return dst;
            case (APPLY_TRANSLATE):
            case (APPLY_IDENTITY):
                dst.x = x;
                dst.y = y;
                dst.z = z;
                return dst;
        }

        /* NOTREACHED */
    }

    private BaseBounds transform2DBounds(RectBounds src, RectBounds dst) {
        switch (state & APPLY_2D_MASK) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_SCALE):
                double x1 = src.getMinX();
                double y1 = src.getMinY();
                double x2 = src.getMaxX();
                double y2 = src.getMaxY();
                dst.setBoundsAndSort((float) (x1 * mxx + y1 * mxy),
                        (float) (x1 * myx + y1 * myy),
                        (float) (x2 * mxx + y2 * mxy),
                        (float) (x2 * myx + y2 * myy));
                dst.add((float) (x1 * mxx + y2 * mxy),
                        (float) (x1 * myx + y2 * myy));
                dst.add((float) (x2 * mxx + y1 * mxy),
                        (float) (x2 * myx + y1 * myy));
                dst.setBounds((float) (dst.getMinX() + mxt),
                        (float) (dst.getMinY() + myt),
                        (float) (dst.getMaxX() + mxt),
                        (float) (dst.getMaxY() + myt));
                break;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                dst.setBoundsAndSort((float) (src.getMinY() * mxy + mxt),
                        (float) (src.getMinX() * myx + myt),
                        (float) (src.getMaxY() * mxy + mxt),
                        (float) (src.getMaxX() * myx + myt));
                break;
            case (APPLY_SHEAR):
                dst.setBoundsAndSort((float) (src.getMinY() * mxy),
                        (float) (src.getMinX() * myx),
                        (float) (src.getMaxY() * mxy),
                        (float) (src.getMaxX() * myx));
                break;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                dst.setBoundsAndSort((float) (src.getMinX() * mxx + mxt),
                        (float) (src.getMinY() * myy + myt),
                        (float) (src.getMaxX() * mxx + mxt),
                        (float) (src.getMaxY() * myy + myt));
                break;
            case (APPLY_SCALE):
                dst.setBoundsAndSort((float) (src.getMinX() * mxx),
                        (float) (src.getMinY() * myy),
                        (float) (src.getMaxX() * mxx),
                        (float) (src.getMaxY() * myy));
                break;
            case (APPLY_TRANSLATE):
                dst.setBounds((float) (src.getMinX() + mxt),
                        (float) (src.getMinY() + myt),
                        (float) (src.getMaxX() + mxt),
                        (float) (src.getMaxY() + myt));
                break;
            case (APPLY_IDENTITY):
                if (src != dst) {
                    dst.setBounds(src);
                }
                break;
        }
        return dst;
    }

    // Note: Only use this method if src or dst is a 3D bounds
    private BaseBounds transform3DBounds(BaseBounds src, BaseBounds dst) {
        switch (state & APPLY_2D_MASK) {
            default:
                stateError();
                /* NOTREACHED */
                // Note: Assuming mxz = myz = mzx = mzy = mzt 0
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_SCALE):
                double x1 = src.getMinX();
                double y1 = src.getMinY();
                double z1 = src.getMinZ();
                double x2 = src.getMaxX();
                double y2 = src.getMaxY();
                double z2 = src.getMaxZ();
                dst.setBoundsAndSort((float) (x1 * mxx + y1 * mxy),
                        (float) (x1 * myx + y1 * myy),
                        (float) z1,
                        (float) (x2 * mxx + y2 * mxy),
                        (float) (x2 * myx + y2 * myy),
                        (float) z2);
                dst.add((float) (x1 * mxx + y2 * mxy),
                        (float) (x1 * myx + y2 * myy), 0);
                dst.add((float) (x2 * mxx + y1 * mxy),
                        (float) (x2 * myx + y1 * myy), 0);
                dst.deriveWithNewBounds((float) (dst.getMinX() + mxt),
                        (float) (dst.getMinY() + myt),
                        dst.getMinZ(),
                        (float) (dst.getMaxX() + mxt),
                        (float) (dst.getMaxY() + myt),
                        dst.getMaxZ());
                break;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinY() * mxy + mxt),
                        (float) (src.getMinX() * myx + myt),
                        src.getMinZ(),
                        (float) (src.getMaxY() * mxy + mxt),
                        (float) (src.getMaxX() * myx + myt),
                        src.getMaxZ());
                break;
            case (APPLY_SHEAR):
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinY() * mxy),
                        (float) (src.getMinX() * myx),
                        src.getMinZ(),
                        (float) (src.getMaxY() * mxy),
                        (float) (src.getMaxX() * myx),
                        src.getMaxZ());
                break;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinX() * mxx + mxt),
                        (float) (src.getMinY() * myy + myt),
                        src.getMinZ(),
                        (float) (src.getMaxX() * mxx + mxt),
                        (float) (src.getMaxY() * myy + myt),
                        src.getMaxZ());
                break;
            case (APPLY_SCALE):
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinX() * mxx),
                        (float) (src.getMinY() * myy),
                        src.getMinZ(),
                        (float) (src.getMaxX() * mxx),
                        (float) (src.getMaxY() * myy),
                        src.getMaxZ());
                break;
            case (APPLY_TRANSLATE):
                dst = dst.deriveWithNewBounds((float) (src.getMinX() + mxt),
                        (float) (src.getMinY() + myt),
                        src.getMinZ(),
                        (float) (src.getMaxX() + mxt),
                        (float) (src.getMaxY() + myt),
                        src.getMaxZ());
                break;
            case (APPLY_IDENTITY):
                if (src != dst) {
                    dst = dst.deriveWithNewBounds(src);
                }
                break;
        }
        return dst;
    }

    @Override
    public BaseBounds transform(BaseBounds src, BaseBounds dst) {
        // assert(APPLY_3D was dealt with at a higher level)
        if (src.getBoundsType() != BaseBounds.BoundsType.RECTANGLE ||
                dst.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
            return transform3DBounds(src, dst);
        }
        return transform2DBounds((RectBounds) src, (RectBounds) dst);
    }

    @Override
    public void transform(Rectangle src, Rectangle dst) {
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state & APPLY_2D_MASK) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SHEAR | APPLY_SCALE):
            case (APPLY_SHEAR | APPLY_TRANSLATE):
            case (APPLY_SHEAR):
            case (APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SCALE):
                RectBounds b = new RectBounds(src);
                //TODO: Need to verify that this is a safe cast ... (RT-26885)
                b = (RectBounds) transform(b, b);
                dst.setBounds(b);
                return;
            case (APPLY_TRANSLATE):
                Translate2D.transform(src, dst, mxt, myt);
                return;
            case (APPLY_IDENTITY):
                if (dst != src) {
                    dst.setBounds(src);
                }
        }
    }

    /**
     * Transforms an array of floating point coordinates by this transform.
     * The two coordinate array sections can be exactly the same or
     * can be overlapping sections of the same array without affecting the
     * validity of the results.
     * This method ensures that no source coordinates are overwritten by a
     * previous operation before they can be transformed.
     * The coordinates are stored in the arrays starting at the specified
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the source point coordinates.
     *               Each point is stored as a pair of x,&nbsp;y coordinates.
     * @param dstPts the array into which the transformed point coordinates
     *               are returned.  Each point is stored as a pair of x,&nbsp;y
     *               coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the destination array
     * @param numPts the number of points to be transformed
     */
    @Override
    public void transform(float[] srcPts, int srcOff,
                          float[] dstPts, int dstOff,
                          int numPts) {
        doTransform(srcPts, srcOff, dstPts, dstOff, numPts,
                (this.state & APPLY_2D_MASK));
    }

    /**
     * Transforms an array of relative distance vectors by this
     * transform.
     * A relative distance vector is transformed without applying the
     * translation components of the affine transformation matrix
     * using the following equations:
     * <pre>
     *  [  x' ]   [  m00  m01 (m02) ] [  x  ]   [ m00x + m01y ]
     *  [  y' ] = [  m10  m11 (m12) ] [  y  ] = [ m10x + m11y ]
     *  [ (1) ]   [  (0)  (0) ( 1 ) ] [ (1) ]   [     (1)     ]
     * </pre>
     * The two coordinate array sections can be exactly the same or
     * can be overlapping sections of the same array without affecting the
     * validity of the results.
     * This method ensures that no source coordinates are
     * overwritten by a previous operation before they can be transformed.
     * The coordinates are stored in the arrays starting at the indicated
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the source distance vectors.
     *               Each vector is stored as a pair of relative x,&nbsp;y coordinates.
     * @param dstPts the array into which the transformed distance vectors
     *               are returned.  Each vector is stored as a pair of relative
     *               x,&nbsp;y coordinates.
     * @param srcOff the offset to the first vector to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed vector that is stored in the destination array
     * @param numPts the number of vector coordinate pairs to be
     *               transformed
     */
    @Override
    public void deltaTransform(float[] srcPts, int srcOff,
                               float[] dstPts, int dstOff,
                               int numPts) {
        doTransform(srcPts, srcOff, dstPts, dstOff, numPts,
                (this.state & APPLY_2D_DELTA_MASK));
    }

    private void doTransform(float[] srcPts, int srcOff,
                             float[] dstPts, int dstOff,
                             int numPts, int thestate) {
        double Mxx, Mxy, Mxt, Myx, Myy, Myt;    // For caching
        if (dstPts == srcPts &&
                dstOff > srcOff && dstOff < srcOff + numPts * 2) {
            // If the arrays overlap partially with the destination higher
            // than the source and we transform the coordinates normally
            // we would overwrite some of the later source coordinates
            // with results of previous transformations.
            // To get around this we use arraycopy to copy the points
            // to their final destination with correct overwrite
            // handling and then transform them in place in the new
            // safer location.
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            // srcPts = dstPts;     // They are known to be equal.
            srcOff = dstOff;
        }
        // Note that this method also works for 3D transforms since the
        // mxz and myz matrix elements get multiplied by z (0.0) and the
        // mzx, mzy, mzz, and mzt elements only get used to calculate
        // the resulting Z coordinate, which we drop (ignore).
        switch (thestate) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxx * x + Mxy * y + Mxt);
                    dstPts[dstOff++] = (float) (Myx * x + Myy * y + Myt);
                }
                return;
            case (APPLY_SHEAR | APPLY_SCALE):
                Mxx = mxx;
                Mxy = mxy;
                Myx = myx;
                Myy = myy;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxx * x + Mxy * y);
                    dstPts[dstOff++] = (float) (Myx * x + Myy * y);
                }
                return;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxy * srcPts[srcOff++] + Mxt);
                    dstPts[dstOff++] = (float) (Myx * x + Myt);
                }
                return;
            case (APPLY_SHEAR):
                Mxy = mxy;
                Myx = myx;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxy * srcPts[srcOff++]);
                    dstPts[dstOff++] = (float) (Myx * x);
                }
                return;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxt = mxt;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (Mxx * srcPts[srcOff++] + Mxt);
                    dstPts[dstOff++] = (float) (Myy * srcPts[srcOff++] + Myt);
                }
                return;
            case (APPLY_SCALE):
                Mxx = mxx;
                Myy = myy;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (Mxx * srcPts[srcOff++]);
                    dstPts[dstOff++] = (float) (Myy * srcPts[srcOff++]);
                }
                return;
            case (APPLY_TRANSLATE):
                Mxt = mxt;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] + Mxt);
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] + Myt);
                }
                return;
            case (APPLY_IDENTITY):
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff,
                            numPts * 2);
                }
        }

        /* NOTREACHED */
    }

    /**
     * Transforms an array of double precision coordinates by this transform.
     * The two coordinate array sections can be exactly the same or
     * can be overlapping sections of the same array without affecting the
     * validity of the results.
     * This method ensures that no source coordinates are
     * overwritten by a previous operation before they can be transformed.
     * The coordinates are stored in the arrays starting at the indicated
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the source point coordinates.
     *               Each point is stored as a pair of x,&nbsp;y coordinates.
     * @param dstPts the array into which the transformed point
     *               coordinates are returned.  Each point is stored as a pair of
     *               x,&nbsp;y coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the destination array
     * @param numPts the number of point objects to be transformed
     */
    @Override
    public void transform(double[] srcPts, int srcOff,
                          double[] dstPts, int dstOff,
                          int numPts) {
        doTransform(srcPts, srcOff, dstPts, dstOff, numPts,
                (this.state & APPLY_2D_MASK));
    }

    private void doTransform(double[] srcPts, int srcOff,
                             double[] dstPts, int dstOff,
                             int numPts, int thestate) {
        double Mxx, Mxy, Mxt, Myx, Myy, Myt;    // For caching
        if (dstPts == srcPts &&
                dstOff > srcOff && dstOff < srcOff + numPts * 2) {
            // If the arrays overlap partially with the destination higher
            // than the source and we transform the coordinates normally
            // we would overwrite some of the later source coordinates
            // with results of previous transformations.
            // To get around this we use arraycopy to copy the points
            // to their final destination with correct overwrite
            // handling and then transform them in place in the new
            // safer location.
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            // srcPts = dstPts;     // They are known to be equal.
            srcOff = dstOff;
        }
        // Note that this method also works for 3D transforms since the
        // mxz and myz matrix elements get multiplied by z (0.0) and the
        // mzx, mzy, mzz, and mzt elements only get used to calculate
        // the resulting Z coordinate, which we drop (ignore).
        switch (thestate) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxx * x + Mxy * y + Mxt;
                    dstPts[dstOff++] = Myx * x + Myy * y + Myt;
                }
                return;
            case (APPLY_SHEAR | APPLY_SCALE):
                Mxx = mxx;
                Mxy = mxy;
                Myx = myx;
                Myy = myy;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxx * x + Mxy * y;
                    dstPts[dstOff++] = Myx * x + Myy * y;
                }
                return;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxy * srcPts[srcOff++] + Mxt;
                    dstPts[dstOff++] = Myx * x + Myt;
                }
                return;
            case (APPLY_SHEAR):
                Mxy = mxy;
                Myx = myx;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxy * srcPts[srcOff++];
                    dstPts[dstOff++] = Myx * x;
                }
                return;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxt = mxt;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = Mxx * srcPts[srcOff++] + Mxt;
                    dstPts[dstOff++] = Myy * srcPts[srcOff++] + Myt;
                }
                return;
            case (APPLY_SCALE):
                Mxx = mxx;
                Myy = myy;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = Mxx * srcPts[srcOff++];
                    dstPts[dstOff++] = Myy * srcPts[srcOff++];
                }
                return;
            case (APPLY_TRANSLATE):
                Mxt = mxt;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = srcPts[srcOff++] + Mxt;
                    dstPts[dstOff++] = srcPts[srcOff++] + Myt;
                }
                return;
            case (APPLY_IDENTITY):
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff,
                            numPts * 2);
                }
        }

        /* NOTREACHED */
    }

    /**
     * Transforms an array of floating point coordinates by this transform
     * and stores the results into an array of doubles.
     * The coordinates are stored in the arrays starting at the specified
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the source point coordinates.
     *               Each point is stored as a pair of x,&nbsp;y coordinates.
     * @param dstPts the array into which the transformed point coordinates
     *               are returned.  Each point is stored as a pair of x,&nbsp;y
     *               coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the destination array
     * @param numPts the number of points to be transformed
     */
    @Override
    public void transform(float[] srcPts, int srcOff,
                          double[] dstPts, int dstOff,
                          int numPts) {
        double Mxx, Mxy, Mxt, Myx, Myy, Myt;    // For caching
        // Note that this method also works for 3D transforms since the
        // mxz and myz matrix elements get multiplied by z (0.0) and the
        // mzx, mzy, mzz, and mzt elements only get used to calculate
        // the resulting Z coordinate, which we drop (ignore).
        switch (state & APPLY_2D_MASK) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxx * x + Mxy * y + Mxt;
                    dstPts[dstOff++] = Myx * x + Myy * y + Myt;
                }
                return;
            case (APPLY_SHEAR | APPLY_SCALE):
                Mxx = mxx;
                Mxy = mxy;
                Myx = myx;
                Myy = myy;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxx * x + Mxy * y;
                    dstPts[dstOff++] = Myx * x + Myy * y;
                }
                return;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxy * srcPts[srcOff++] + Mxt;
                    dstPts[dstOff++] = Myx * x + Myt;
                }
                return;
            case (APPLY_SHEAR):
                Mxy = mxy;
                Myx = myx;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = Mxy * srcPts[srcOff++];
                    dstPts[dstOff++] = Myx * x;
                }
                return;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxt = mxt;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = Mxx * srcPts[srcOff++] + Mxt;
                    dstPts[dstOff++] = Myy * srcPts[srcOff++] + Myt;
                }
                return;
            case (APPLY_SCALE):
                Mxx = mxx;
                Myy = myy;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = Mxx * srcPts[srcOff++];
                    dstPts[dstOff++] = Myy * srcPts[srcOff++];
                }
                return;
            case (APPLY_TRANSLATE):
                Mxt = mxt;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = srcPts[srcOff++] + Mxt;
                    dstPts[dstOff++] = srcPts[srcOff++] + Myt;
                }
                return;
            case (APPLY_IDENTITY):
                while (--numPts >= 0) {
                    dstPts[dstOff++] = srcPts[srcOff++];
                    dstPts[dstOff++] = srcPts[srcOff++];
                }
        }

        /* NOTREACHED */
    }

    /**
     * Transforms an array of double precision coordinates by this transform
     * and stores the results into an array of floats.
     * The coordinates are stored in the arrays starting at the specified
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the source point coordinates.
     *               Each point is stored as a pair of x,&nbsp;y coordinates.
     * @param dstPts the array into which the transformed point
     *               coordinates are returned.  Each point is stored as a pair of
     *               x,&nbsp;y coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the destination array
     * @param numPts the number of point objects to be transformed
     */
    @Override
    public void transform(double[] srcPts, int srcOff,
                          float[] dstPts, int dstOff,
                          int numPts) {
        double Mxx, Mxy, Mxt, Myx, Myy, Myt;    // For caching
        // Note that this method also works for 3D transforms since the
        // mxz and myz matrix elements get multiplied by z (0.0) and the
        // mzx, mzy, mzz, and mzt elements only get used to calculate
        // the resulting Z coordinate, which we drop (ignore).
        switch (state & APPLY_2D_MASK) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxx * x + Mxy * y + Mxt);
                    dstPts[dstOff++] = (float) (Myx * x + Myy * y + Myt);
                }
                return;
            case (APPLY_SHEAR | APPLY_SCALE):
                Mxx = mxx;
                Mxy = mxy;
                Myx = myx;
                Myy = myy;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxx * x + Mxy * y);
                    dstPts[dstOff++] = (float) (Myx * x + Myy * y);
                }
                return;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myt = myt;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxy * srcPts[srcOff++] + Mxt);
                    dstPts[dstOff++] = (float) (Myx * x + Myt);
                }
                return;
            case (APPLY_SHEAR):
                Mxy = mxy;
                Myx = myx;
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (Mxy * srcPts[srcOff++]);
                    dstPts[dstOff++] = (float) (Myx * x);
                }
                return;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxt = mxt;
                Myy = myy;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (Mxx * srcPts[srcOff++] + Mxt);
                    dstPts[dstOff++] = (float) (Myy * srcPts[srcOff++] + Myt);
                }
                return;
            case (APPLY_SCALE):
                Mxx = mxx;
                Myy = myy;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (Mxx * srcPts[srcOff++]);
                    dstPts[dstOff++] = (float) (Myy * srcPts[srcOff++]);
                }
                return;
            case (APPLY_TRANSLATE):
                Mxt = mxt;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] + Mxt);
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] + Myt);
                }
                return;
            case (APPLY_IDENTITY):
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (srcPts[srcOff++]);
                    dstPts[dstOff++] = (float) (srcPts[srcOff++]);
                }
        }

        /* NOTREACHED */
    }

    /**
     * Inverse transforms the specified <code>ptSrc</code> and stores the
     * result in <code>ptDst</code>.
     * If <code>ptDst</code> is <code>null</code>, a new
     * <code>Point2D</code> object is allocated and then the result of the
     * transform is stored in this object.
     * In either case, <code>ptDst</code>, which contains the transformed
     * point, is returned for convenience.
     * If <code>ptSrc</code> and <code>ptDst</code> are the same
     * object, the input point is correctly overwritten with the
     * transformed point.
     *
     * @param ptSrc the point to be inverse transformed
     * @param ptDst the resulting transformed point
     * @return <code>ptDst</code>, which contains the result of the
     * inverse transform.
     * inverted.
     */
    @Override
    public Point2D inverseTransform(Point2D ptSrc, Point2D ptDst) throws NoninvertibleTransformException {
        if (ptDst == null) {
            ptDst = new Point2D();
        }
        // Copy source coords into local variables in case src == dst
        double x = ptSrc.x;
        double y = ptSrc.y;
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                x -= mxt;
                y -= myt;
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_SCALE):
                double det = mxx * myy - mxy * myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                ptDst.setLocation((float) ((x * myy - y * mxy) / det),
                        (float) ((y * mxx - x * myx) / det));
                return ptDst;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                x -= mxt;
                y -= myt;
                /* NOBREAK */
            case (APPLY_SHEAR):
                if (mxy == 0.0 || myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                ptDst.setLocation((float) (y / myx), (float) (x / mxy));
                return ptDst;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                x -= mxt;
                y -= myt;
                /* NOBREAK */
            case (APPLY_SCALE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                ptDst.setLocation((float) (x / mxx), (float) (y / myy));
                return ptDst;
            case (APPLY_TRANSLATE):
                ptDst.setLocation((float) (x - mxt), (float) (y - myt));
                return ptDst;
            case (APPLY_IDENTITY):
                ptDst.setLocation((float) x, (float) y);
                return ptDst;
        }

        /* NOTREACHED */
    }

    /**
     * Inverse transforms the specified <code>src</code> and stores the
     * result in <code>dst</code>.
     * If <code>dst</code> is <code>null</code>, a new
     * <code>Vec3d</code> object is allocated and then the result of the
     * transform is stored in this object.
     * In either case, <code>dst</code>, which contains the transformed
     * point, is returned for convenience.
     * If <code>src</code> and <code>dst</code> are the same
     * object, the input point is correctly overwritten with the
     * transformed point.
     *
     * @param src the point to be inverse transformed
     * @param dst the resulting transformed point
     * @return <code>dst</code>, which contains the result of the
     * inverse transform.
     * @throws NoninvertibleTransformException if the matrix cannot be
     *                                         inverted.
     */
    @Override
    public Vec3d inverseTransform(Vec3d src, Vec3d dst) throws NoninvertibleTransformException {
        if (dst == null) {
            dst = new Vec3d();
        }
        // Copy source coords into local variables in case src == dst
        double x = src.x;
        double y = src.y;
        double z = src.z;
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                x -= mxt;
                y -= myt;
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_SCALE):
                double det = mxx * myy - mxy * myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                dst.set(((x * myy - y * mxy) / det), ((y * mxx - x * myx) / det), z);
                return dst;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                x -= mxt;
                y -= myt;
                /* NOBREAK */
            case (APPLY_SHEAR):
                if (mxy == 0.0 || myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.set((y / myx), (x / mxy), z);
                return dst;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                x -= mxt;
                y -= myt;
                /* NOBREAK */
            case (APPLY_SCALE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.set((x / mxx), (y / myy), z);
                return dst;
            case (APPLY_TRANSLATE):
                dst.set((x - mxt), (y - myt), z);
                return dst;
            case (APPLY_IDENTITY):
                dst.set(x, y, z);
                return dst;
        }

        /* NOTREACHED */
    }

    /**
     * Inverse transforms the specified <code>src</code> vector and stores the
     * result in <code>dst</code> vector (without applying the translation
     * elements).
     * If <code>dst</code> is <code>null</code>, a new
     * <code>Vec3d</code> object is allocated and then the result of the
     * transform is stored in this object.
     * In either case, <code>dst</code>, which contains the transformed
     * vector, is returned for convenience.
     * If <code>src</code> and <code>dst</code> are the same
     * object, the input vector is correctly overwritten with the
     * transformed vector.
     *
     * @param src the vector to be inverse transformed
     * @param dst the resulting transformed vector
     * @return <code>dst</code>, which contains the result of the
     * inverse transform.
     * inverted.
     * @since JavaFX 8.0
     */
    @Override
    public Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst) throws NoninvertibleTransformException {
        if (dst == null) {
            dst = new Vec3d();
        }
        // Copy source coords into local variables in case src == dst
        double x = src.x;
        double y = src.y;
        double z = src.z;
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SHEAR | APPLY_SCALE):
                double det = mxx * myy - mxy * myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                dst.set(((x * myy - y * mxy) / det), ((y * mxx - x * myx) / det), z);
                return dst;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
            case (APPLY_SHEAR):
                if (mxy == 0.0 || myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.set((y / myx), (x / mxy), z);
                return dst;
            case (APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SCALE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.set((x / mxx), (y / myy), z);
                return dst;
            case (APPLY_TRANSLATE):
            case (APPLY_IDENTITY):
                dst.set(x, y, z);
                return dst;
        }

        /* NOTREACHED */
    }

    private BaseBounds inversTransform2DBounds(RectBounds src, RectBounds dst)
            throws NoninvertibleTransformException {
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_SCALE):
                double det = mxx * myy - mxy * myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                double x1 = src.getMinX() - mxt;
                double y1 = src.getMinY() - myt;
                double x2 = src.getMaxX() - mxt;
                double y2 = src.getMaxY() - myt;
                dst.setBoundsAndSort((float) ((x1 * myy - y1 * mxy) / det),
                        (float) ((y1 * mxx - x1 * myx) / det),
                        (float) ((x2 * myy - y2 * mxy) / det),
                        (float) ((y2 * mxx - x2 * myx) / det));
                dst.add((float) ((x2 * myy - y1 * mxy) / det),
                        (float) ((y1 * mxx - x2 * myx) / det));
                dst.add((float) ((x1 * myy - y2 * mxy) / det),
                        (float) ((y2 * mxx - x1 * myx) / det));
                return dst;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                if (mxy == 0.0 || myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) ((src.getMinY() - myt) / myx),
                        (float) ((src.getMinX() - mxt) / mxy),
                        (float) ((src.getMaxY() - myt) / myx),
                        (float) ((src.getMaxX() - mxt) / mxy));
                break;
            case (APPLY_SHEAR):
                if (mxy == 0.0 || myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) (src.getMinY() / myx),
                        (float) (src.getMinX() / mxy),
                        (float) (src.getMaxY() / myx),
                        (float) (src.getMaxX() / mxy));
                break;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) ((src.getMinX() - mxt) / mxx),
                        (float) ((src.getMinY() - myt) / myy),
                        (float) ((src.getMaxX() - mxt) / mxx),
                        (float) ((src.getMaxY() - myt) / myy));
                break;
            case (APPLY_SCALE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) (src.getMinX() / mxx),
                        (float) (src.getMinY() / myy),
                        (float) (src.getMaxX() / mxx),
                        (float) (src.getMaxY() / myy));
                break;
            case (APPLY_TRANSLATE):
                dst.setBounds((float) (src.getMinX() - mxt),
                        (float) (src.getMinY() - myt),
                        (float) (src.getMaxX() - mxt),
                        (float) (src.getMaxY() - myt));
                break;
            case (APPLY_IDENTITY):
                if (dst != src) {
                    dst.setBounds(src);
                }
                break;
        }
        return dst;
    }

    // Note: Only use this method if src or dst is a 3D bounds
    private BaseBounds inversTransform3DBounds(BaseBounds src, BaseBounds dst)
            throws NoninvertibleTransformException {
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_SCALE):
                /* NOBREAK */
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                /* NOBREAK */
            case (APPLY_SHEAR):
                double det = mxx * myy - mxy * myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                double x1 = src.getMinX() - mxt;
                double y1 = src.getMinY() - myt;
                double z1 = src.getMinZ();
                double x2 = src.getMaxX() - mxt;
                double y2 = src.getMaxY() - myt;
                double z2 = src.getMaxZ();
                dst = dst.deriveWithNewBoundsAndSort(
                        (float) ((x1 * myy - y1 * mxy) / det),
                        (float) ((y1 * mxx - x1 * myx) / det),
                        (float) (z1 / det),
                        (float) ((x2 * myy - y2 * mxy) / det),
                        (float) ((y2 * mxx - x2 * myx) / det),
                        (float) (z2 / det));
                dst.add((float) ((x2 * myy - y1 * mxy) / det),
                        (float) ((y1 * mxx - x2 * myx) / det), 0);
                dst.add((float) ((x1 * myy - y2 * mxy) / det),
                        (float) ((y2 * mxx - x1 * myx) / det), 0);
                return dst;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst = dst.deriveWithNewBoundsAndSort((float) ((src.getMinX() - mxt) / mxx),
                        (float) ((src.getMinY() - myt) / myy),
                        src.getMinZ(),
                        (float) ((src.getMaxX() - mxt) / mxx),
                        (float) ((src.getMaxY() - myt) / myy),
                        src.getMaxZ());
                break;
            case (APPLY_SCALE):
                if (mxx == 0.0 || myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinX() / mxx),
                        (float) (src.getMinY() / myy),
                        src.getMinZ(),
                        (float) (src.getMaxX() / mxx),
                        (float) (src.getMaxY() / myy),
                        src.getMaxZ());
                break;
            case (APPLY_TRANSLATE):
                dst = dst.deriveWithNewBounds((float) (src.getMinX() - mxt),
                        (float) (src.getMinY() - myt),
                        src.getMinZ(),
                        (float) (src.getMaxX() - mxt),
                        (float) (src.getMaxY() - myt),
                        src.getMaxZ());
                break;
            case (APPLY_IDENTITY):
                if (dst != src) {
                    dst = dst.deriveWithNewBounds(src);
                }
                break;
        }
        return dst;
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds src, BaseBounds dst) throws NoninvertibleTransformException {
        // assert(APPLY_3D was dealt with at a higher level)
        if (src.getBoundsType() != BaseBounds.BoundsType.RECTANGLE ||
                dst.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
            return inversTransform3DBounds(src, dst);
        }
        return inversTransform2DBounds((RectBounds) src, (RectBounds) dst);
    }

    @Override
    public void inverseTransform(Rectangle src, Rectangle dst) throws NoninvertibleTransformException {
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SHEAR | APPLY_SCALE):
            case (APPLY_SHEAR | APPLY_TRANSLATE):
            case (APPLY_SHEAR):
            case (APPLY_SCALE | APPLY_TRANSLATE):
            case (APPLY_SCALE):
                RectBounds b = new RectBounds(src);
                //TODO: Need to verify this casting is safe .... (RT-26885)
                b = (RectBounds) inverseTransform(b, b);
                dst.setBounds(b);
                return;
            case (APPLY_TRANSLATE):
                Translate2D.transform(src, dst, -mxt, -myt);
                return;
            case (APPLY_IDENTITY):
                if (dst != src) {
                    dst.setBounds(src);
                }
        }
    }

    /**
     * Inverse transforms an array of single precision coordinates by
     * this transform.
     * The two coordinate array sections can be exactly the same or
     * can be overlapping sections of the same array without affecting the
     * validity of the results.
     * This method ensures that no source coordinates are
     * overwritten by a previous operation before they can be transformed.
     * The coordinates are stored in the arrays starting at the specified
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the source point coordinates.
     *               Each point is stored as a pair of x,&nbsp;y coordinates.
     * @param dstPts the array into which the transformed point
     *               coordinates are returned.  Each point is stored as a pair of
     *               x,&nbsp;y coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the destination array
     * @param numPts the number of point objects to be transformed
     *               inverted.
     */
    @Override
    public void inverseTransform(float[] srcPts, int srcOff,
                                 float[] dstPts, int dstOff,
                                 int numPts)
            throws NoninvertibleTransformException {
        doInverseTransform(srcPts, srcOff, dstPts, dstOff, numPts, state);
    }

    /**
     * Inverse transforms an array of single precision relative coordinates by
     * this transform.
     * The two coordinate array sections can be exactly the same or
     * can be overlapping sections of the same array without affecting the
     * validity of the results.
     * This method ensures that no source coordinates are
     * overwritten by a previous operation before they can be transformed.
     * The coordinates are stored in the arrays starting at the specified
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the relative source coordinates.
     *               Each point is stored as a pair of x,&nbsp;y coordinates.
     * @param dstPts the array into which the relative transformed point
     *               coordinates are returned.  Each point is stored as a pair of
     *               x,&nbsp;y coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the destination array
     * @param numPts the number of point objects to be transformed
     *               inverted.
     */
    @Override
    public void inverseDeltaTransform(float[] srcPts, int srcOff,
                                      float[] dstPts, int dstOff,
                                      int numPts)
            throws NoninvertibleTransformException {
        doInverseTransform(srcPts, srcOff, dstPts, dstOff, numPts,
                state & ~APPLY_TRANSLATE);
    }

    /**
     * Inverse transforms an array of single precision coordinates by
     * this transform using the specified state type.
     */
    private void doInverseTransform(float[] srcPts, int srcOff,
                                    float[] dstPts, int dstOff,
                                    int numPts, int thestate)
            throws NoninvertibleTransformException {
        double Mxx, Mxy, Mxt, Myx, Myy, Myt;    // For caching
        double det;
        if (dstPts == srcPts &&
                dstOff > srcOff && dstOff < srcOff + numPts * 2) {
            // If the arrays overlap partially with the destination higher
            // than the source and we transform the coordinates normally
            // we would overwrite some of the later source coordinates
            // with results of previous transformations.
            // To get around this we use arraycopy to copy the points
            // to their final destination with correct overwrite
            // handling and then transform them in place in the new
            // safer location.
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            // srcPts = dstPts;     // They are known to be equal.
            srcOff = dstOff;
        }
        // assert(APPLY_3D was dealt with at a higher level)
        switch (thestate) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myy = myy;
                Myt = myt;
                det = Mxx * Myy - Mxy * Myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " +
                            det);
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++] - Mxt;
                    double y = srcPts[srcOff++] - Myt;
                    dstPts[dstOff++] = (float) ((x * Myy - y * Mxy) / det);
                    dstPts[dstOff++] = (float) ((y * Mxx - x * Myx) / det);
                }
                return;
            case (APPLY_SHEAR | APPLY_SCALE):
                Mxx = mxx;
                Mxy = mxy;
                Myx = myx;
                Myy = myy;
                det = Mxx * Myy - Mxy * Myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " +
                            det);
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) ((x * Myy - y * Mxy) / det);
                    dstPts[dstOff++] = (float) ((y * Mxx - x * Myx) / det);
                }
                return;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myt = myt;
                if (Mxy == 0.0 || Myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++] - Mxt;
                    dstPts[dstOff++] = (float) ((srcPts[srcOff++] - Myt) / Myx);
                    dstPts[dstOff++] = (float) (x / Mxy);
                }
                return;
            case (APPLY_SHEAR):
                Mxy = mxy;
                Myx = myx;
                if (Mxy == 0.0 || Myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] / Myx);
                    dstPts[dstOff++] = (float) (x / Mxy);
                }
                return;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxt = mxt;
                Myy = myy;
                Myt = myt;
                if (Mxx == 0.0 || Myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) ((srcPts[srcOff++] - Mxt) / Mxx);
                    dstPts[dstOff++] = (float) ((srcPts[srcOff++] - Myt) / Myy);
                }
                return;
            case (APPLY_SCALE):
                Mxx = mxx;
                Myy = myy;
                if (Mxx == 0.0 || Myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] / Mxx);
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] / Myy);
                }
                return;
            case (APPLY_TRANSLATE):
                Mxt = mxt;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] - Mxt);
                    dstPts[dstOff++] = (float) (srcPts[srcOff++] - Myt);
                }
                return;
            case (APPLY_IDENTITY):
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff,
                            numPts * 2);
                }
        }

        /* NOTREACHED */
    }

    /**
     * Inverse transforms an array of double precision coordinates by
     * this transform.
     * The two coordinate array sections can be exactly the same or
     * can be overlapping sections of the same array without affecting the
     * validity of the results.
     * This method ensures that no source coordinates are
     * overwritten by a previous operation before they can be transformed.
     * The coordinates are stored in the arrays starting at the specified
     * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     *
     * @param srcPts the array containing the source point coordinates.
     *               Each point is stored as a pair of x,&nbsp;y coordinates.
     * @param dstPts the array into which the transformed point
     *               coordinates are returned.  Each point is stored as a pair of
     *               x,&nbsp;y coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the destination array
     * @param numPts the number of point objects to be transformed
     * @throws NoninvertibleTransformException if the matrix cannot be
     *                                         inverted.
     */
    @Override
    public void inverseTransform(double[] srcPts, int srcOff,
                                 double[] dstPts, int dstOff,
                                 int numPts)
            throws NoninvertibleTransformException {
        double Mxx, Mxy, Mxt, Myx, Myy, Myt;    // For caching
        double det;
        if (dstPts == srcPts &&
                dstOff > srcOff && dstOff < srcOff + numPts * 2) {
            // If the arrays overlap partially with the destination higher
            // than the source and we transform the coordinates normally
            // we would overwrite some of the later source coordinates
            // with results of previous transformations.
            // To get around this we use arraycopy to copy the points
            // to their final destination with correct overwrite
            // handling and then transform them in place in the new
            // safer location.
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            // srcPts = dstPts;     // They are known to be equal.
            srcOff = dstOff;
        }
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myy = myy;
                Myt = myt;
                det = Mxx * Myy - Mxy * Myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " +
                            det);
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++] - Mxt;
                    double y = srcPts[srcOff++] - Myt;
                    dstPts[dstOff++] = (x * Myy - y * Mxy) / det;
                    dstPts[dstOff++] = (y * Mxx - x * Myx) / det;
                }
                return;
            case (APPLY_SHEAR | APPLY_SCALE):
                Mxx = mxx;
                Mxy = mxy;
                Myx = myx;
                Myy = myy;
                det = Mxx * Myy - Mxy * Myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " +
                            det);
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    double y = srcPts[srcOff++];
                    dstPts[dstOff++] = (x * Myy - y * Mxy) / det;
                    dstPts[dstOff++] = (y * Mxx - x * Myx) / det;
                }
                return;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myt = myt;
                if (Mxy == 0.0 || Myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++] - Mxt;
                    dstPts[dstOff++] = (srcPts[srcOff++] - Myt) / Myx;
                    dstPts[dstOff++] = x / Mxy;
                }
                return;
            case (APPLY_SHEAR):
                Mxy = mxy;
                Myx = myx;
                if (Mxy == 0.0 || Myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    double x = srcPts[srcOff++];
                    dstPts[dstOff++] = srcPts[srcOff++] / Myx;
                    dstPts[dstOff++] = x / Mxy;
                }
                return;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxt = mxt;
                Myy = myy;
                Myt = myt;
                if (Mxx == 0.0 || Myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (srcPts[srcOff++] - Mxt) / Mxx;
                    dstPts[dstOff++] = (srcPts[srcOff++] - Myt) / Myy;
                }
                return;
            case (APPLY_SCALE):
                Mxx = mxx;
                Myy = myy;
                if (Mxx == 0.0 || Myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--numPts >= 0) {
                    dstPts[dstOff++] = srcPts[srcOff++] / Mxx;
                    dstPts[dstOff++] = srcPts[srcOff++] / Myy;
                }
                return;
            case (APPLY_TRANSLATE):
                Mxt = mxt;
                Myt = myt;
                while (--numPts >= 0) {
                    dstPts[dstOff++] = srcPts[srcOff++] - Mxt;
                    dstPts[dstOff++] = srcPts[srcOff++] - Myt;
                }
                return;
            case (APPLY_IDENTITY):
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff,
                            numPts * 2);
                }
        }

        /* NOTREACHED */
    }

    // Utility methods to optimize rotate methods.
    // These tables translate the flags during predictable quadrant
    // rotations where the shear and scale values are swapped and negated.

    /**
     * Sets this transform to the inverse of itself.
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
     * thrown if the <code>invert</code> method is called.
     *
     * @throws NoninvertibleTransformException if the matrix cannot be inverted.
     * @see #getDeterminant
     */
    @Override
    public void invert()
            throws NoninvertibleTransformException {
        double Mxx, Mxy, Mxt;
        double Myx, Myy, Myt;
        double det;
        // assert(APPLY_3D was dealt with at a higher level)
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case (APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myy = myy;
                Myt = myt;
                det = Mxx * Myy - Mxy * Myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " +
                            det);
                }
                mxx = Myy / det;
                myx = -Myx / det;
                mxy = -Mxy / det;
                myy = Mxx / det;
                mxt = (Mxy * Myt - Myy * Mxt) / det;
                myt = (Myx * Mxt - Mxx * Myt) / det;
                break;
            case (APPLY_SHEAR | APPLY_SCALE):
                Mxx = mxx;
                Mxy = mxy;
                Myx = myx;
                Myy = myy;
                det = Mxx * Myy - Mxy * Myx;
                if (det == 0 || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " +
                            det);
                }
                mxx = Myy / det;
                myx = -Myx / det;
                mxy = -Mxy / det;
                myy = Mxx / det;
                break;
            case (APPLY_SHEAR | APPLY_TRANSLATE):
                Mxy = mxy;
                Mxt = mxt;
                Myx = myx;
                Myt = myt;
                if (Mxy == 0.0 || Myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                myx = 1.0 / Mxy;
                mxy = 1.0 / Myx;
                mxt = -Myt / Myx;
                myt = -Mxt / Mxy;
                break;
            case (APPLY_SHEAR):
                Mxy = mxy;
                Myx = myx;
                if (Mxy == 0.0 || Myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                myx = 1.0 / Mxy;
                mxy = 1.0 / Myx;
                break;
            case (APPLY_SCALE | APPLY_TRANSLATE):
                Mxx = mxx;
                Mxt = mxt;
                Myy = myy;
                Myt = myt;
                if (Mxx == 0.0 || Myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                mxx = 1.0 / Mxx;
                myy = 1.0 / Myy;
                mxt = -Mxt / Mxx;
                myt = -Myt / Myy;
                break;
            case (APPLY_SCALE):
                Mxx = mxx;
                Myy = myy;
                if (Mxx == 0.0 || Myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                mxx = 1.0 / Mxx;
                myy = 1.0 / Myy;
                break;
            case (APPLY_TRANSLATE):
                mxt = -mxt;
                myt = -myt;
                break;
            case (APPLY_IDENTITY):
                break;
        }
    }

}
