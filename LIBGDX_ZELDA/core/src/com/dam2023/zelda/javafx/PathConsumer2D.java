package com.dam2023.zelda.javafx;


/**
 * OK copied from JAVAFX github
 */
public interface PathConsumer2D {
    void moveTo(float x0, float y0);
    void lineTo(float x1, float y1);
    void quadTo(float xc, float yc,
                       float x1, float y1);
    void curveTo(float xc0, float yc0,
                        float xc1, float yc1,
                        float x1, float y1);
    void closePath();
}
