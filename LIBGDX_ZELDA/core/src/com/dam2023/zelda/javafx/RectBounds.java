
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

    /**
     * Creates a RectBounds based on the minX, minY, maxX, and maxY values specified.
     */
    public RectBounds(float minX, float minY, float maxX, float maxY) {
        setBounds(minX, minY, maxX, maxY);
    }

    @Override public float getMinX() {
        return minX;
    }

    @Override public float getMinY() {
        return minY;
    }

    @Override public float getMaxX() {
        return maxX;
    }

    @Override public float getMaxY() {
        return maxY;
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
