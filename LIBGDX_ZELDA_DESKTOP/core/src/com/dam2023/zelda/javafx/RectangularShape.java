package com.dam2023.zelda.javafx;


/**
 * <code>RectangularShape</code> is the base class for a number of
 * objects whose geometry is defined by a rectangular frame.
 * This class does not directly specify any specific geometry by
 * itself, but merely provides manipulation methods inherited by
 * a whole category of <code>Shape</code> objects.
 * The manipulation methods provided by this class can be used to
 * query and modify the rectangular frame, which provides a reference
 * for the subclasses to define their geometry.
 *
 * @version 1.26, 05/05/07
 */
public abstract class RectangularShape extends Shape {

    /**
     * This is an abstract class that cannot be instantiated directly.
     */
    protected RectangularShape() {
    }

    /**
     * Returns the X coordinate of the upper-left corner of
     * the framing rectangle in <code>double</code> precision.
     *
     * @return the X coordinate of the upper-left corner of
     * the framing rectangle.
     */
    public abstract float getX();

    /**
     * Returns the Y coordinate of the upper-left corner of
     * the framing rectangle in <code>double</code> precision.
     *
     * @return the Y coordinate of the upper-left corner of
     * the framing rectangle.
     */
    public abstract float getY();

    /**
     * Returns the width of the framing rectangle in
     * <code>double</code> precision.
     *
     * @return the width of the framing rectangle.
     */
    public abstract float getWidth();

    /**
     * Returns the height of the framing rectangle
     * in <code>double</code> precision.
     *
     * @return the height of the framing rectangle.
     */
    public abstract float getHeight();


    /**
     * Returns the X coordinate of the center of the framing
     * rectangle of the <code>Shape</code> in <code>double</code>
     * precision.
     *
     * @return the X coordinate of the center of the framing rectangle
     * of the <code>Shape</code>.
     */
    public float getCenterX() {
        return getX() + getWidth() / 2f;
    }

    /**
     * Returns the Y coordinate of the center of the framing
     * rectangle of the <code>Shape</code> in <code>double</code>
     * precision.
     *
     * @return the Y coordinate of the center of the framing rectangle
     * of the <code>Shape</code>.
     */
    public float getCenterY() {
        return getY() + getHeight() / 2f;
    }

    /**
     * Determines whether the <code>RectangularShape</code> is empty.
     * When the <code>RectangularShape</code> is empty, it encloses no
     * area.
     *
     * @return <code>true</code> if the <code>RectangularShape</code> is empty;
     * <code>false</code> otherwise.
     */
    public abstract boolean isEmpty();

    /**
     * {@inheritDoc}
     */
    @Override
    public RectBounds getBounds() {
        float width = getWidth();
        float height = getHeight();
        if (width < 0 || height < 0) {
            return new RectBounds();
        }
        float x = getX();
        float y = getY();
        float x1 = (float) Math.floor(x);
        float y1 = (float) Math.floor(y);
        float x2 = (float) Math.ceil(x + width);
        float y2 = (float) Math.ceil(y + height);
        return new RectBounds(x1, y1, x2, y2);
    }

    @Override
    public String toString() {
        return getClass().getName() +
                "[x=" + getX() +
                ",y=" + getY() +
                ",w=" + getWidth() +
                ",h=" + getHeight() + "]";
    }
}

