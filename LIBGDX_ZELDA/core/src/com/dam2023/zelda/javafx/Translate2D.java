package com.dam2023.zelda.javafx;

/**
 *
 */
public class Translate2D extends BaseTransform {
    private double mxt;
    private double myt;

    public Translate2D(double tx, double ty) {
        this.mxt = tx;
        this.myt = ty;
    }

    @Override
    public Degree getDegree() {
        return Degree.TRANSLATE_2D;
    }

    @Override
    public double getDeterminant() {
        return 1.0;
    }

    @Override
    public double getMxt() {
        return mxt;
    }

    @Override
    public double getMyt() {
        return myt;
    }

    @Override
    public int getType() {
        return (mxt == 0.0 && myt == 0.0) ? TYPE_IDENTITY : TYPE_TRANSLATION;
    }

    @Override
    public boolean isIdentity() {
        return (mxt == 0.0 && myt == 0.0);
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
    public Point2D transform(Point2D src, Point2D dst) {
        if (dst == null) dst = makePoint(dst);
        dst.setLocation(
                (float) (src.x + mxt),
                (float) (src.y + myt));
        return dst;
    }

    @Override
    public Point2D inverseTransform(Point2D src, Point2D dst) {
        if (dst == null) dst = makePoint(dst);
        dst.setLocation(
                (float) (src.x - mxt),
                (float) (src.y - myt));
        return dst;
    }

    @Override
    public Vec3d transform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.x = src.x + mxt;
        dst.y = src.y + myt;
        dst.z = src.z;
        return dst;
    }

    @Override
    public Vec3d deltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.set(src);
        return dst;
    }

    @Override
    public Vec3d inverseTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.x = src.x - mxt;
        dst.y = src.y - myt;
        dst.z = src.z;
        return dst;
    }

    @Override
    public Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.set(src);
        return dst;
    }

    @Override
    public void transform(float[] srcPts, int srcOff,
                          float[] dstPts, int dstOff,
                          int numPts)
    {
        float tx = (float) this.mxt;
        float ty = (float) this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + numPts * 2) {
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
            if (dstOff == srcOff && tx == 0.0f && ty == 0.0f) {
                return;
            }
        }
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = srcPts[srcOff++] + tx;
            dstPts[dstOff++] = srcPts[srcOff++] + ty;
        }
    }

    @Override
    public void transform(double[] srcPts, int srcOff,
                          double[] dstPts, int dstOff,
                          int numPts)
    {
        double tx = this.mxt;
        double ty = this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + numPts * 2) {
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
            if (dstOff == srcOff && tx == 0.0 && ty == 0.0) {
                return;
            }
        }
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = srcPts[srcOff++] + tx;
            dstPts[dstOff++] = srcPts[srcOff++] + ty;
        }
    }

    @Override
    public void transform(float[] srcPts, int srcOff,
                          double[] dstPts, int dstOff,
                          int numPts)
    {
        double tx = this.mxt;
        double ty = this.myt;
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = srcPts[srcOff++] + tx;
            dstPts[dstOff++] = srcPts[srcOff++] + ty;
        }
    }

    @Override
    public void transform(double[] srcPts, int srcOff,
                          float[] dstPts, int dstOff,
                          int numPts)
    {
        double tx = this.mxt;
        double ty = this.myt;
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = (float) (srcPts[srcOff++] + tx);
            dstPts[dstOff++] = (float) (srcPts[srcOff++] + ty);
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
        float tx = (float) this.mxt;
        float ty = (float) this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + numPts * 2) {
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
            if (dstOff == srcOff && tx == 0.0f && ty == 0.0f) {
                return;
            }
        }
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = srcPts[srcOff++] - tx;
            dstPts[dstOff++] = srcPts[srcOff++] - ty;
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
        double tx = this.mxt;
        double ty = this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + numPts * 2) {
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
            if (dstOff == srcOff && tx == 0f && ty == 0f) {
                return;
            }
        }
        for (int i = 0; i < numPts; i++) {
            dstPts[dstOff++] = srcPts[srcOff++] - tx;
            dstPts[dstOff++] = srcPts[srcOff++] - ty;
        }
    }

    @Override
    public BaseBounds transform(BaseBounds bounds, BaseBounds result) {
        float minX = (float) (bounds.getMinX() + mxt);
        float minY = (float) (bounds.getMinY() + myt);
        float minZ = bounds.getMinZ();
        float maxX = (float) (bounds.getMaxX() + mxt);
        float maxY = (float) (bounds.getMaxY() + myt);
        float maxZ = bounds.getMaxZ();
        return result.deriveWithNewBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void transform(Rectangle rect, Rectangle result) {
        transform(rect, result, mxt, myt);
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds bounds, BaseBounds result) {
        float minX = (float) (bounds.getMinX() - mxt);
        float minY = (float) (bounds.getMinY() - myt);
        float minZ = bounds.getMinZ();
        float maxX = (float) (bounds.getMaxX() - mxt);
        float maxY = (float) (bounds.getMaxY() - myt);
        float maxZ = bounds.getMaxZ();
        return result.deriveWithNewBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void inverseTransform(Rectangle rect, Rectangle result) {
        transform(rect, result, -mxt, -myt);
    }

    static void transform(Rectangle rect, Rectangle result,
                          double mxt, double myt)
    {
        int imxt = (int) mxt;
        int imyt = (int) myt;
        if (imxt == mxt && imyt == myt) {
            result.setBounds(rect);
            result.translate(imxt, imyt);
        } else {
            double x1 = rect.x + mxt;
            double y1 = rect.y + myt;
            double x2 = Math.ceil(x1 + rect.width);
            double y2 = Math.ceil(y1 + rect.height);
            x1 = Math.floor(x1);
            y1 = Math.floor(y1);
            result.setBounds((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
        }
    }

    @Override
    public void setToIdentity() {
        this.mxt = this.myt = 0.0;
    }

    @Override
    public void setTransform(BaseTransform xform) {
        if (!xform.isTranslateOrIdentity()) {
            degreeError(Degree.TRANSLATE_2D);
        }
        this.mxt = xform.getMxt();
        this.myt = xform.getMyt();
    }

    @Override
    public void invert() {
        this.mxt = -this.mxt;
        this.myt = -this.myt;
    }

    @Override
    public BaseTransform createInverse() {
        if (isIdentity()) {
            return IDENTITY_TRANSFORM;
        } else {
            return new Translate2D(-this.mxt, -this.myt);
        }
    }

    // Round values to sane precision for printing
    // Note that Math.sin(Math.PI) has an error of about 10^-16
    private static double _matround(double matval) {
        return Math.rint(matval * 1E15) / 1E15;
    }

    @Override
    public String toString() {
        return ("Translate2D["
                + _matround(mxt) + ", "
                + _matround(myt) + "]");
    }

    @Override
    public BaseTransform copy() {
        return new Translate2D(this.mxt, this.myt);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseTransform) {
            BaseTransform tx = (BaseTransform) obj;
            return (tx.isTranslateOrIdentity() &&
                    tx.getMxt() == this.mxt &&
                    tx.getMyt() == this.myt);
        }
        return false;
    }

    private static final long BASE_HASH;
    static {
        long bits = 0;
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyy());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyx());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxy());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxx());
        bits = bits * 31 + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzt());
        BASE_HASH = bits;
    }

    @Override
    public int hashCode() {
        if (isIdentity()) return 0;
        long bits = BASE_HASH;
        bits = bits * 31 + Double.doubleToLongBits(getMyt());
        bits = bits * 31 + Double.doubleToLongBits(getMxt());
        return (((int) bits) ^ ((int) (bits >> 32)));
    }
}
