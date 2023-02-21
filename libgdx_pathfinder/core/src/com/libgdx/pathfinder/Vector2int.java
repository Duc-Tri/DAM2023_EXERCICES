package com.libgdx.pathfinder;

public class Vector2int {
    int myX, myY;

    public Vector2int(int x, int y) {
        myX = x;
        myY = y;
    }

    public int getDistanceManhattan(Vector2int point2) {
        return Math.abs(myX - point2.myX) + Math.abs(myY - point2.myY);
    }

    public float getDistanceEucl(Vector2int point2) {
        int dx = myX - point2.myX;
        int dy = myY - point2.myY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public boolean equals(Vector2int point2) {
        return myX == point2.myX && myY == point2.myY;
    }
}
