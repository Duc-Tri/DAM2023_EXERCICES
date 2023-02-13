
package com.dam2023.zelda.javafx;

/**
 * A simple object which carries bounds information as floats, and
 * has no Z components.
 */
public final class RectBounds extends BaseBounds {
    // minimum x value of bounding box
    private float minX;
    // maximum x value of bounding box
    private float maxX;
    // minimum y value of bounding box
    private float minY;
    // maximum y value of bounding box
    private float maxY;

    /**
     * Create an axis aligned bounding rectangle object, with an empty bounds
     * where maxX < minX and maxY < minY.
     */
    public RectBounds() {
        minX = minY = 0.0f;
        maxX = maxY = -1.0f;
    }

    @Override public BaseBounds copy() {
        return new RectBounds(minX, minY, maxX, maxY);
    }

    /**
     * Creates a RectBounds based on the minX, minY, maxX, and maxY values specified.
     */
    public RectBounds(float minX, float minY, float maxX, float maxY) {
        setBounds(minX, minY, maxX, maxY);
    }

    /**
     * Creates a RectBounds object as a copy of the specified RectBounds object.
     */
    public RectBounds(RectBounds other) {
        setBounds(other);
    }

    /**
     * Creates a RectBounds object as a copy of the specified RECTANGLE.
     */
    public RectBounds(Rectangle other) {
        setBounds(other.x, other.y,
                other.x + other.width, other.y + other.height);
    }

    @Override public BoundsType getBoundsType() {
        return BoundsType.RECTANGLE;
    }

    /**
     * Convenience function for getting the width of this RectBounds.
     * The dimension along the X-Axis.
     */
    @Override public float getWidth() {
        return maxX - minX;
    }

    /**
     * Convenience function for getting the height of this RectBounds
     * The dimension along the Y-Axis.
     */
    @Override public float getHeight() {
        return maxY - minY;
    }

    @Override public float getMinX() {
        return minX;
    }

    @Override public float getMinY() {
        return minY;
    }

    @Override public float getMinZ() {
        return 0.0f;
    }

    @Override public float getMaxX() {
        return maxX;
    }

    @Override public float getMaxY() {
        return maxY;
    }

    @Override public float getMaxZ() {
        return 0.0f;
    }

    @Override public BaseBounds deriveWithNewBounds(BaseBounds other) {
        if (other.isEmpty()) return makeEmpty();
        if (other.getBoundsType() == BoundsType.RECTANGLE) {
            RectBounds rb = (RectBounds) other;
            minX = rb.getMinX();
            minY = rb.getMinY();
            maxX = rb.getMaxX();
            maxY = rb.getMaxY();
        } else if (other.getBoundsType() == BoundsType.BOX) {
            return new BoxBounds((BoxBounds) other);
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

    @Override public BaseBounds deriveWithNewBounds(float minX, float minY, float minZ,
                                                    float maxX, float maxY, float maxZ) {
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) return makeEmpty();
        if ((minZ == 0) && (maxZ == 0)) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            return this;
        }
        return new BoxBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override public BaseBounds deriveWithNewBoundsAndSort(float minX, float minY, float minZ,
                                                           float maxX, float maxY, float maxZ) {
        if ((minZ == 0) && (maxZ == 0)) {
            setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
            return this;
        }

        BaseBounds bb = new BoxBounds();
        bb.setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
        return bb;
    }

    /**
     * Set the bounds to match that of the RectBounds object specified. The
     * specified bounds object must not be null.
     */
    public void setBounds(RectBounds other) {
        minX = other.getMinX();
        minY = other.getMinY();
        maxX = other.getMaxX();
        maxY = other.getMaxY();
    }

    /**
     * Set the bounds to the given values.
     */
    public void setBounds(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Sets the bounds based on the given coords, and also ensures that after
     * having done so that this RectBounds instance is normalized.
     */
    public void setBoundsAndSort(float minX, float minY, float maxX, float maxY) {
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    @Override public void setBoundsAndSort(float minX, float minY,  float minZ,
                                           float maxX, float maxY, float maxZ) {
        if (minZ != 0 || maxZ != 0) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    public void unionWith(float minX, float minY, float maxX, float maxY) {
        // Short circuit union if either bounds is empty.
        if ((maxX < minX) || (maxY < minY)) return;
        if (this.isEmpty()) {
            setBounds(minX, minY, maxX, maxY);
            return;
        }

        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
    }

    @Override public void add(float x, float y, float z) {
        if (z != 0) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        unionWith(x, y, x, y);
    }

    public void add(float x, float y) {
        unionWith(x, y, x, y);
    }

    @Override public void add(Point2D p) {
        add(p.x, p.y);
    }

    @Override public boolean contains(Point2D p) {
        if ((p == null) || isEmpty()) return false;
        return (p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY);
    }

    @Override public boolean contains(float x, float y) {
        if (isEmpty()) return false;
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }

    /**
     * Determines whether the given <code>other</code> RectBounds is completely
     * contained within this RectBounds. Equivalent RectBounds will return true.
     *
     * @param other The other rect bounds to check against.
     * @return Whether the other rect bounds is contained within this one, which also
     * includes equivalence.
     */
    public boolean contains(RectBounds other) {
        if (isEmpty() || other.isEmpty()) return false;
        return minX <= other.minX && maxX >= other.maxX && minY <= other.minY && maxY >= other.maxY;
    }

    @Override public boolean intersects(float x, float y, float width, float height) {
        if (isEmpty()) return false;
        return (x + width >= minX &&
                y + height >= minY &&
                x <= maxX &&
                y <= maxY);
    }

    public boolean intersects(BaseBounds other) {
        if ((other == null) || other.isEmpty() || isEmpty()) {
            return false;
        }
        return (other.getMaxX() >= minX &&
                other.getMaxY() >= minY &&
                other.getMaxZ() >= getMinZ() &&
                other.getMinX() <= maxX &&
                other.getMinY() <= maxY &&
                other.getMinZ() <= getMaxZ());
    }

    @Override public boolean isEmpty() {
        // NaN values will cause the comparisons to fail and return "empty"
        return !(maxX >= minX && maxY >= minY);
    }

    // for convenience, this function returns a reference to itself, so we can
    // change from using "bounds.makeEmpty(); return bounds;" to just
    // "return bounds.makeEmpty()"
    @Override public RectBounds makeEmpty() {
        minX = minY = 0.0f;
        maxX = maxY = -1.0f;
        return this;
    }

    @Override protected void sortMinMax() {
        if (minX > maxX) {
            float tmp = maxX;
            maxX = minX;
            minX = tmp;
        }
        if (minY > maxY) {
            float tmp = maxY;
            maxY = minY;
            minY = tmp;
        }
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final RectBounds other = (RectBounds) obj;
        if (minX != other.getMinX()) return false;
        if (minY != other.getMinY()) return false;
        if (maxX != other.getMaxX()) return false;
        return maxY == other.getMaxY();
    }

    @Override public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Float.floatToIntBits(minX);
        hash = 79 * hash + Float.floatToIntBits(minY);
        hash = 79 * hash + Float.floatToIntBits(maxX);
        hash = 79 * hash + Float.floatToIntBits(maxY);
        return hash;
    }

    @Override public String toString() {
        return "RectBounds { minX:" + minX + ", minY:" + minY + ", maxX:" + maxX + ", maxY:" + maxY + "} (w:" + (maxX-minX) + ", h:" + (maxY-minY) +")";
    }
}
