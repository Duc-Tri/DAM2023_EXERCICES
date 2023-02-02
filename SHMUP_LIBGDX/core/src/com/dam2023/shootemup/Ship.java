package com.dam2023.shootemup;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ship {
    // ship characteristics
    float movementSpeed; // world units per second
    int shield;

    //position & dimension
    float xPosition, yPosition; // lower-left corner
    float width, height;

    // graphics
    TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    //laser information
    float laserWidth, laserHeight;
    float timeBetweenShots;
    float timeSinceLastShot = 0;
    float laserMovementSpeed;

    public Ship(float xCentre, float yCentre,
                float width, float height,
                float movementSpeed, int shield,
                float laserW, float laserH, float laserMS, float timeBetweenShots,
                TextureRegion shipTR, TextureRegion shieldTR, TextureRegion laserTR) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.xPosition = xCentre - width / 2;
        this.yPosition = yCentre - height / 2;
        this.width = width;
        this.height = height;
        this.shipTextureRegion = shipTR;
        this.shieldTextureRegion = shieldTR;

        this.laserTextureRegion = laserTR;
        this.laserHeight = laserH;
        this.laserWidth = laserW;
        this.laserMovementSpeed = laserMS;
        this.timeBetweenShots = timeBetweenShots;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    public abstract Laser[] fireLasers();

    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, xPosition, yPosition, width, height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, xPosition, yPosition, width, height);
        }
    }

}
