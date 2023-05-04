package com.dam2023.zelda.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dam2023.zelda.sound.Sounds;
import com.dam2023.zelda.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Aurelien on 12/01/2016.
 */
public class ItemSword extends Item {
    private static final int regionHeight = 16;
    private static final int regionWidth = 16;

    // Ensemble des frames de textures possible
    public final TextureRegion[][] textureFrames;

    // Animations
    public Animation animSlashTop;
    public Animation animSlashBottom;
    public Animation animSlashLeft;
    public Animation animSlashRight;

    // Le temps pour donner une coup d'épée
    public final float slashTime;

    // Tous les sonds qui peuvent êtres joués lors du slash de l'épée
    public final List<Sound> slashSounds;

    // L'allonge de l'epee en pixels
    public final float allonge;

    public ItemSword(String textureName, String name, int id, float slashTime, float allonge) {
        super(textureName, name, id);
        this.slashTime = slashTime;
        this.allonge = allonge;
        this.textureFrames = TextureRegion.split(texture, regionWidth, regionHeight);
        this.slashSounds = Arrays.asList(Sounds.swordSlash1, Sounds.swordSlash2, Sounds.swordSlash3);

        makeAnimations();
    }

    protected void makeAnimations() {
        // MOVE
        this.animSlashBottom = new Animation(slashTime / 4, this.textureFrames[0][0], this.textureFrames[0][1], this.textureFrames[0][2], this.textureFrames[0][2]);
        this.animSlashBottom.setPlayMode(Animation.PlayMode.LOOP);

        this.animSlashLeft = new Animation(slashTime / 4, this.textureFrames[1][0], this.textureFrames[1][1], this.textureFrames[1][2], this.textureFrames[1][2]);
        this.animSlashLeft.setPlayMode(Animation.PlayMode.LOOP);

        this.animSlashRight = new Animation(slashTime / 4, this.textureFrames[2][0], this.textureFrames[2][1], this.textureFrames[2][2], this.textureFrames[2][2]);
        this.animSlashRight.setPlayMode(Animation.PlayMode.LOOP);

        this.animSlashTop = new Animation(slashTime / 4, this.textureFrames[3][0], this.textureFrames[3][1], this.textureFrames[3][2], this.textureFrames[3][2]);
        this.animSlashTop.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void playRandomSlashSound() {
        this.slashSounds.get(World.random.nextInt(this.slashSounds.size())).play();
    }
}
