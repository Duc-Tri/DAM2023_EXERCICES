package com.dam2023.shootemup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {

    int lives;


    public PlayerShip(float xCentre, float yCentre,
                      float width, float height,
                      float movementSpeed, int shield,
                      float laserW, float laserH, float laserMS, float timeBetweenShots,
                      TextureRegion shipTR, TextureRegion shieldTR, TextureRegion laserTR) {

        super(xCentre, yCentre, width, height, movementSpeed, shield, laserW, laserH, laserMS, timeBetweenShots, shipTR, shieldTR, laserTR);
        lives = 3;
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[2];
        lasers[0] = new Laser(boundingBox.x + boundingBox.width * .07f,
                boundingBox.y + boundingBox.height * .045f,
                laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);
        lasers[1] = new Laser(boundingBox.x + boundingBox.width * .93f,
                boundingBox.y + boundingBox.height * .045f,
                laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);

        timeSinceLastShot = 0;

        return lasers;
    }
}
