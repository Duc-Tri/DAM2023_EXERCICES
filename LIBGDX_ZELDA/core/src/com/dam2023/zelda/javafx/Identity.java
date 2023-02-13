package com.dam2023.zelda.javafx;


/**
 *
 */
public final class Identity extends BaseTransform {
    @Override
    public Degree getDegree() {
        return Degree.IDENTITY;
    }

    @Override
    public int getType() {
        return TYPE_IDENTITY;
    }

    @Override
    public boolean isIdentity() {
        return true;
    }

    @Override
    public boolean isTranslateOrIdentity() {
        return true;
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public double getDeterminant() {
        return 1.0;
    }

    @Override
    public Point2D transform(Point2D src, Point2D dst) {
        if (dst == null) dst = makePoint(dst);
        dst.setLocation(src);
        return dst;
    }

    @Override
    public Point2D inverseTransform(Point2D src, Point2D dst) {
        if (dst == null) dst = makePoint(dst);
        dst.setLocation(src);
        return dst;
    }

    @Override
    public Vec3d transform(Vec3d src, Vec3d dst) {
        if (dst == null) return new Vec3d(src);
        dst.set(src);
        return dst;
    }

    @Override
    public Vec3d deltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) return new Vec3d(src);
        dst.set(src);
        return dst;
    }

    @Override
    public Vec3d inverseTransform(Vec3d src, Vec3d dst) {
        if (dst == null) return new Vec3d(src);
        dst.set(src);
        return dst;
    }

    @Override
    public Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) return new Vec3d(src);
        dst.set(src);
        return dst;
    }

    @Override
    public void transform(float[] srcPts, int srcOff,
                          float[] dstPts, int dstOff,
                          int numPts)
    {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override
    public void transform(double[] srcPts, int srcOff,
                          double[] dstPts, int dstOff,
                          int numPts)
    {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override
    public void transform(float[] srcPts, int srcOff,
                          double[] dstPts, int dstOff,
                          int numPts)
    {
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = srcPts[srcOff++];
            dstPts[dstOff++] = srcPts[srcOff++];
        }
    }

    @Override
    public void transform(double[] srcPts, int srcOff,
                          float[] dstPts, int dstOff,
                          int numPts)
    {
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = (float) srcPts[srcOff++];
            dstPts[dstOff++] = (float) srcPts[srcOff++];
        }
    }

    @Override
    public void deltaTransform(float[] srcPts, int srcOff,
                               float[] dstPts, int dstOff,
                               int numPts)
    {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override
    public void inverseTransform(float[] srcPts, int srcOff,
                                 float[] dstPts, int dstOff,
                                 int numPts)
    {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override
    public void inverseDeltaTransform(float[] srcPts, int srcOff,
                                      float[] dstPts, int dstOff,
                                      int numPts)
    {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override
    public void inverseTransform(double[] srcPts, int srcOff,
                                 double[] dstPts, int dstOff,
                                 int numPts)
    {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override
    public BaseBounds transform(BaseBounds bounds, BaseBounds result) {
        if (result != bounds) {
            result = result.deriveWithNewBounds(bounds);
        }
        return result;
    }

    @Override
    public void transform(Rectangle rect, Rectangle result) {
        if (result != rect) {
            result.setBounds(rect);
        }
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds bounds, BaseBounds result) {
        if (result != bounds) {
            result = result.deriveWithNewBounds(bounds);
        }
        return result;
    }

    @Override
    public void inverseTransform(Rectangle rect, Rectangle result) {
        if (result != rect) {
            result.setBounds(rect);
        }
    }

    @Override
    public void setToIdentity() {
    }

    @Override
    public void setTransform(BaseTransform xform) {
        if (!xform.isIdentity()) {
            degreeError(Degree.IDENTITY);
        }
    }

    @Override
    public void invert() {
    }

    @Override
    public BaseTransform createInverse() {
        return this;
    }

    @Override
    public String toString() {
        return ("Identity[]");
    }

    @Override
    public BaseTransform copy() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BaseTransform &&
                ((BaseTransform) obj).isIdentity());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
