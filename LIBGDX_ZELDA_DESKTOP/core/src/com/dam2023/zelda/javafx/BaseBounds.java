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

    // Only allow subclasses in this package
    BaseBounds() {
    }

    public abstract float getMinX();

    public abstract float getMinY();

    public abstract float getMaxX();

    public abstract float getMaxY();

    protected abstract void sortMinMax();

}
