package com.dam2023.zelda.tiles;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Tile animé, se baser sur le Tile 'fleurs' pour voir comment le contruire.
 */
public class AnimatedTile extends Tile {
    protected final TextureRegion[] frames;
    protected final Animation animation;

    /**
     * @param name          Le nom du fichier de texture
     * @param frameDuration La durée entre deux frames de l'animation en seconde
     * @param playMode      Le mode d'animation
     */
    public AnimatedTile(String name, int id, float frameDuration, Animation.PlayMode playMode) {
        super(name, id);
        TextureRegion[][] tmp = TextureRegion.split(texture, TILE_SIZE, TILE_SIZE);
        frames = new TextureRegion[tmp[0].length];
        System.arraycopy(tmp[0], 0, frames, 0, tmp[0].length);
        animation = new Animation(0.5f, frames);
        animation.setPlayMode(playMode);
        animation.setFrameDuration(frameDuration);
    }

    public TextureRegion getKeyFrame(float time) {
        return (TextureRegion) animation.getKeyFrame(time, true);
//        return new TextureRegion(); // NULL !!!!!!!!!!!!!!!!!!!!!!
    }

    @Override
    public void draw(SpriteBatch batch, float time, int xPos, int yPos) {
        TextureRegion tr = getKeyFrame(time);
        batch.draw(tr, xPos, yPos);
    }
}
