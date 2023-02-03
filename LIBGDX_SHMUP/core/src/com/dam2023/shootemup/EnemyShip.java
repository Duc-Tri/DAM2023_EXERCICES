package com.dam2023.shootemup;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyShip extends Ship {

    public EnemyShip(float xCentre, float yCentre,
                     float width, float height,
                     float movementSpeed, int shield,
                     float laserW, float laserH, float laserMS, float timeBetweenShots,
                     TextureRegion shipTR, TextureRegion shieldTR, TextureRegion laserTR) {

        super(xCentre, yCentre, width, height, movementSpeed, shield, laserW, laserH, laserMS, timeBetweenShots, shipTR, shieldTR, laserTR);
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[2];

        lasers[0] = new Laser(xPosition + width * .18f, yPosition - laserHeight,
                laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);

        lasers[1] = new Laser(xPosition + width * .82f, yPosition - laserHeight,
                laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);

        timeSinceLastShot = 0;

        return lasers;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, xPosition, yPosition, width, height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, xPosition , yPosition - height * .2f, width, height);
        }
    }

}
