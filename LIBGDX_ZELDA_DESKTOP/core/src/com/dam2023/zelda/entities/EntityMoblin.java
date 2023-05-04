package com.dam2023.zelda.entities;

import com.dam2023.zelda.entities.instances.InstanceEntity;
import com.dam2023.zelda.entities.instances.InstanceEntityMoblin;

/**
 * Created by Aurelien on 16/01/2016.
 */
public class EntityMoblin extends EntityHostileMonster {
    private final EntityMoblin.MoblinType type;

    public EntityMoblin(String textureName, String name, int id, int maxLife, float damage, float moveSpeed, MoblinType type) {
        super(textureName, name, id, maxLife, damage, moveSpeed);
        this.type = type;
    }

    public enum MoblinType {
        RED,
        BLUE,
    }

    @Override
    public InstanceEntity newInstance(float x, float y) {
        return new InstanceEntityMoblin(x, y, type);
    }
}
