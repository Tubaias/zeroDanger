package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;

public class Hitbox {
    private Vector2 position;
    private Vector2 size;

    public Hitbox(float centerX, float centerY, float width, float height) {
        this.position = new Vector2(centerX - width / 2, centerY - height / 2);
        this.size = new Vector2(width, height);
    }

    public boolean collide(Hitbox other) {
        Vector2 otherPos = other.getPosition();
        Vector2 otherSize = other.getSize();

        return otherPos.x < this.position.x + this.size.x && otherPos.x + otherSize.x > this.position.x &&
               otherPos.y < this.position.y + this.size.y && otherPos.y + otherSize.y > this.position.y;
    }

    public boolean collide(Vector2 point) {
        return point.x < this.position.x + this.size.x && point.x > this.position.x &&
               point.y < this.position.y + this.size.y && point.y > this.position.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }
}