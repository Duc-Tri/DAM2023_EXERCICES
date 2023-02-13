package com.dam2023.zelda.javafx;



/**
 *
 */
public class BoxBounds extends BaseBounds {
    // minimum x value of bounding box
    private float minX;
    // maximum x value of bounding box
    private float maxX;
    // minimum y value of bounding box
    private float minY;
    // maximum y value of bounding box
    private float maxY;
    // minimum z value of bounding box
    private float minZ;
    // maximum z value of bounding box
    private float maxZ;

    /**
     * Create an axis aligned bounding box object, with an empty bounds
     * where maxX < minX, maxY < minY and maxZ < minZ.
     */
    public BoxBounds() {
        minX = minY = minZ = 0.0f;
        maxX = maxY = maxZ = -1.0f;
    }

    @Override
    public BaseBounds copy() {
        return new BoxBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Creates an axis aligned bounding box based on the minX, minY, minZ, maxX, maxY,
     * and maxZ values specified.
     */
    public BoxBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        setBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Creates an axis aligned bounding box as a copy of the specified
     * BoxBounds object.
     */
    public BoxBounds(BoxBounds other) {
        setBounds(other);
    }

    @Override
    public BoundsType getBoundsType() {
        return BoundsType.BOX;
    }

    /**
     * Convenience function for getting the width of this bounds.
     * The dimension along the X-Axis.
     */
    @Override
    public float getWidth() {
        return maxX - minX;
    }

    /**
     * Convenience function for getting the height of this bounds.
     * The dimension along the Y-Axis.
     */
    @Override
    public float getHeight() {
        return maxY - minY;
    }

    @Override
    public float getMinX() {
        return minX;
    }

    @Override
    public float getMinY() {
        return minY;
    }

    @Override
    public float getMinZ() {
        return minZ;
    }

    @Override
    public float getMaxX() {
        return maxX;
    }

    @Override
    public float getMaxY() {
        return maxY;
    }

    @Override
    public float getMaxZ() {
        return maxZ;
    }

    @Override
    public BaseBounds deriveWithNewBounds(BaseBounds other) {
        if (other.isEmpty()) return makeEmpty();
        if ((other.getBoundsType() == BoundsType.RECTANGLE) ||
                (other.getBoundsType() == BoundsType.BOX)) {
            minX = other.getMinX();
            minY = other.getMinY();
            minZ = other.getMinZ();
            maxX = other.getMaxX();
            maxY = other.getMaxY();
            maxZ = other.getMaxZ();
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(float minX, float minY, float minZ,
                                          float maxX, float maxY, float maxZ) {
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) return makeEmpty();
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBoundsAndSort(float minX, float minY, float minZ,
                                                 float maxX, float maxY, float maxZ) {
        setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
        return this;
    }

    /**
     * Set the bounds to match that of the BaseBounds object specified. The
     * specified bounds object must not be null.
     */
    public final void setBounds(BaseBounds other) {
        minX = other.getMinX();
        minY = other.getMinY();
        minZ = other.getMinZ();
        maxX = other.getMaxX();
        maxY = other.getMaxY();
        maxZ = other.getMaxZ();
    }

    /**
     * Set the bounds to the given values.
     */
    public final void setBounds(float minX, float minY,  float minZ,
                                float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    @Override
    public void setBoundsAndSort(float minX, float minY,  float minZ,
                                 float maxX, float maxY, float maxZ) {
        setBounds(minX, minY, minZ, maxX, maxY, maxZ);
        sortMinMax();
    }


    public void unionWith(float minX, float minY, float minZ,
                          float maxX, float maxY, float maxZ) {
        // Short circuit union if either bounds is empty.
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) return;
        if (this.isEmpty()) {
            setBounds(minX, minY, minZ, maxX, maxY, maxZ);
            return;
        }

        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.minZ = Math.min(this.minZ, minZ);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
        this.maxZ = Math.max(this.maxZ, maxZ);
    }

    @Override
    public void add(float x, float y, float z) {
        unionWith(x, y, z, x, y, z);
    }

    @Override
    public void add(Point2D p) {
        add(p.x, p.y, 0.0f);
    }

    @Override
    public boolean contains(Point2D p) {
        if ((p == null) || isEmpty()) return false;
        return contains(p.x, p.y, 0.0f);
    }

    @Override
    public boolean contains(float x, float y) {
        if (isEmpty()) return false;
        return contains(x, y, 0.0f);
    }

    public boolean contains(float x, float y, float z) {
        if (isEmpty()) return false;
        return (x >= minX && x <= maxX && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ);
    }

    public boolean contains(float x, float y, float z,
                            float width, float height, float depth) {
        if (isEmpty()) return false;
        return contains(x, y, z) && contains(x+width, y+height, z+depth);
    }

    @Override
    public boolean intersects(float x, float y, float width, float height) {
        return intersects(x, y, 0.0f, width, height, 0.0f);
    }

    public boolean intersects(float x, float y, float z,
                              float width, float height, float depth) {
        if (isEmpty()) return false;
        return (x + width >= minX &&
                y + height >= minY &&
                z + depth >= minZ &&
                x <= maxX &&
                y <= maxY &&
                z <= maxZ);
    }

    public boolean intersects(BaseBounds other) {
        if ((other == null) || other.isEmpty() || isEmpty()) {
            return false;
        }
        return (other.getMaxX() >= minX &&
                other.getMaxY() >= minY &&
                other.getMaxZ() >= minZ &&
                other.getMinX() <= maxX &&
                other.getMinY() <= maxY &&
                other.getMinZ() <= maxZ);
    }

    @Override
    public boolean isEmpty() {
        return maxX < minX || maxY < minY || maxZ < minZ;
    }

    // for convenience, this function returns a reference to itself, so we can
    // change from using "bounds.makeEmpty(); return bounds;" to just
    // "return bounds.makeEmpty()"
    @Override
    public BoxBounds makeEmpty() {
        minX = minY = minZ = 0.0f;
        maxX = maxY = maxZ = -1.0f;
        return this;
    }

    @Override
    protected void sortMinMax() {
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
        if (minZ > maxZ) {
            float tmp = maxZ;
            maxZ = minZ;
            minZ = tmp;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final BoxBounds other = (BoxBounds) obj;
        if (minX != other.getMinX()) return false;
        if (minY != other.getMinY()) return false;
        if (minZ != other.getMinZ()) return false;
        if (maxX != other.getMaxX()) return false;
        if (maxY != other.getMaxY()) return false;
        return maxZ == other.getMaxZ();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Float.floatToIntBits(minX);
        hash = 79 * hash + Float.floatToIntBits(minY);
        hash = 79 * hash + Float.floatToIntBits(minZ);
        hash = 79 * hash + Float.floatToIntBits(maxX);
        hash = 79 * hash + Float.floatToIntBits(maxY);
        hash = 79 * hash + Float.floatToIntBits(maxZ);

        return hash;
    }

    @Override
    public String toString() {
        return "BoxBounds { minX:" + minX + ", minY:" + minY + ", minZ:" + minZ + ", maxX:" + maxX + ", maxY:" + maxY + ", maxZ:" + maxZ + "}";
    }

}
