package com.dam2023.zelda.javafx;


/**
 * Base class for mutable bounds objects. There are two concrete specializations,
 * BoxBounds (3D) and RectBounds (2D). Various "derive" methods exist which are
 * used to mutate the bounds objects, such that they can be converted between the
 * different types as appropriate, or modified in place if possible. This allows
 * us to churn memory as little as possible without representing everything as
 * if it were in 3D space (there are some computational cost savings to being
 * able to ignore the Z values).
 */
public abstract class BaseBounds {

    /**
     * The different types of BaseBounds that are currently supported.
     * We might support other types of bounds in the future (such as
     * SPHERE) which are also 2D or 3D but are defined in some way
     * other than with a bounding box. Such bounds can sometimes more
     * accurately represent the pixels
     */
    public enum BoundsType {
        RECTANGLE, // A 2D axis-aligned bounding rectangle
        BOX,  // A 3D axis-aligned bounding box
    }

    // Only allow subclasses in this package
    BaseBounds() { }

    /**
     * Duplicates this instance. This differs from deriveWithNewBounds(other)
     * where "other" would be this, in that derive methods may return the
     * same instance, whereas copy will always return a new instance.
     */
    public abstract BaseBounds copy();

    public abstract BoundsType getBoundsType();

    /**
     * Convenience function for getting the width of this bounds.
     * The dimension along the X-Axis.
     */
    public abstract float getWidth();

    /**
     * Convenience function for getting the height of this bounds.
     * The dimension along the Y-Axis.
     */
    public abstract float getHeight();

    public abstract float getMinX();

    public abstract float getMinY();

    public abstract float getMinZ();

    public abstract float getMaxX();

    public abstract float getMaxY();

    public abstract float getMaxZ();

    public abstract BaseBounds deriveWithNewBounds(BaseBounds other);

    public abstract BaseBounds deriveWithNewBounds(float minX, float minY, float minZ,
                                                   float maxX, float maxY, float maxZ);

    public abstract BaseBounds deriveWithNewBoundsAndSort(float minX, float minY, float minZ,
                                                          float maxX, float maxY, float maxZ);

    public abstract void setBoundsAndSort(float minX, float minY,  float minZ,
                                          float maxX, float maxY, float maxZ);

    // TODO: obsolete add and replace with deriveWithUnion(Vec2f v) and deriveWithUnion(Vec3f v)
    // (RT-26886)
    public abstract void add(Point2D p);
    public abstract void add(float x, float y, float z);

    public abstract boolean contains(Point2D p);

    public abstract boolean contains(float x, float y);

    public abstract boolean intersects(float x, float y, float width, float height);

    public abstract boolean isEmpty();

    public abstract BaseBounds makeEmpty();

    protected abstract void sortMinMax();

}
