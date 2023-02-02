package com.dam2023.shootemup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {

    public PlayerShip(float xCentre, float yCentre,
                      float width, float height,
                      float movementSpeed, int shield,
                      float laserW, float laserH, float laserMS, float timeBetweenShots,
                      TextureRegion shipTR, TextureRegion shieldTR, TextureRegion laserTR) {
        super(xCentre, yCentre, width, height, movementSpeed, shield, laserW, laserH, laserMS, timeBetweenShots, shipTR, shieldTR, laserTR);
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[2];
        lasers[0] = new Laser(xPosition + width * .07f, yPosition + height * .045f,
                4*laserWidth, 4*laserHeight, laserMovementSpeed, laserTextureRegion);
        lasers[1] = new Laser(xPosition + width * .93f, yPosition + height * .045f,
                10*laserWidth, 10*laserHeight, laserMovementSpeed, laserTextureRegion);

        timeSinceLastShot = 0;

        return lasers;
    }
}