
package com.mygdx.game.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.bullet.BulletType;
import com.mygdx.game.entity.enemy.script.ActionScript;
import com.mygdx.game.utility.EntityStore;
import com.mygdx.game.utility.graphic.Animator;

public class BossTootter extends Enemy {
    private Vector2 position;
    private Sprite sprite;
    private Animator animation;
    private ActionScript script;
    private int hitpoints = 500;
    private float moveAccumulator;
    private float shootAccumulator;
    private float phaseAccumulator;
    private EntityStore store;
    private boolean dead = false;

    private Vector2 leftWing;
    private Vector2 rightWing;

    private float shootingAngle;
    private int shootingPhase;

    private int burst;
    private float interval;

    private int deadFrames;

    public BossTootter(float x, float y, EntityStore store, ActionScript script) {
        this.store = store;
        this.position = new Vector2(x, y);

        animation = new Animator(new Texture("images/enemies/bossTootter.png"), 3, 40);
        this.sprite = new Sprite(animation.getFrame());
        this.sprite.setPosition(x, y);

        this.hitbox = new Hitbox(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, 150, 150);
        this.script = script;

        shootingAngle = 90;
        shootingPhase = 1;

        setWingPosition();
    }

    @Override
    public void step() {
        if (dead) {
            if (deadFrames < 36) {
                deadFrames++;
            } else {
                this.position = new Vector2(-1000,-1000);
            }

            return;
        }

        moveAccumulator += Gdx.graphics.getDeltaTime();
        shootAccumulator += Gdx.graphics.getDeltaTime();
        interval += Gdx.graphics.getDeltaTime();
        phaseAccumulator += Gdx.graphics.getDeltaTime();

        hitbox.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
        setWingPosition();

        while (moveAccumulator > 0.0167) {
            script.step(this);
            moveAccumulator -= 0.0167;
        }

        this.isHit = false;
        shoot();

        if (shootingPhase == 0) {
            if (phaseAccumulator > 1) {
                phaseAccumulator -= 1;
                shootingPhase = 2;
            }
        } else if (shootingPhase == 2) {
            if (phaseAccumulator > 1) {
                phaseAccumulator -= 1;
                interval = 0;
                shootAccumulator = 2;
                shootingPhase = 1;
            } 
        } else if (shootingPhase == 1) {
            if (phaseAccumulator > 1) {
                phaseAccumulator -= 1;
                shootingPhase = 3;
            } 
        } else if (shootingPhase == 3) {
            if (phaseAccumulator > 1) {
                phaseAccumulator -= 1;
                interval = 0;
                shootAccumulator = 0;
                shootingPhase = 0;
            } 
        }
        
    }

    private void shoot() {
        if (shootingPhase == 0) {
            while (shootAccumulator > 0.01) {
                spirals();
                shootAccumulator -= 0.01;
            }
        } else if (shootingPhase == 1) {
            if (shootAccumulator > 2) {
                burst = 6;
                shootAccumulator -= 2;
            }

            if (interval > 0.15) {
                if (burst > 0) {
                    burst();
                    burst--;

                }
                interval -= 0.15;
            }
        }
    }

    private void spirals() {
        store.bulletSystem.newBullet(BulletType.BASIC, position.x + this.sprite.getWidth() / 2, position.y + this.sprite.getHeight() / 2, shootingAngle);
        store.bulletSystem.newBullet(BulletType.BASIC, position.x + this.sprite.getWidth() / 2, position.y + this.sprite.getHeight() / 2, shootingAngle + 180);

        shootingAngle += 8.7;
    }

    private void burst() {
        Vector2 playerPos = store.player.getPosition().cpy();
        playerPos.x = playerPos.x + store.player.getSprite().getWidth() / 2;
        playerPos.y = playerPos.y + store.player.getSprite().getHeight() / 2;
        
        int angleL = (int) playerPos.cpy().sub(leftWing).angle(new Vector2(0, 1));
        int angleR = (int) playerPos.cpy().sub(rightWing).angle(new Vector2(0, 1));
        
        store.bulletSystem.newBullet(BulletType.ANGLED, leftWing.x, leftWing.y, angleL);
        store.bulletSystem.newBullet(BulletType.ANGLED, rightWing.x, rightWing.y, angleR);

    }

    private void setWingPosition() {
        leftWing = new Vector2(position.x + 100, position.y + sprite.getHeight() / 2);
        rightWing = new Vector2(position.x + sprite.getWidth() - 100, position.y + sprite.getHeight() / 2);
    }

    @Override
    public void hit() {
        this.isHit = true;
        this.hitpoints -= 1;

        if (!dead && hitpoints <= 0) {
            die();
        }
    }

    private void die() {
        dead = true;
        hitbox.setPosition(-1000, -1000);
        animation = new Animator(new Texture("images/effects/explosion512alt.png"), 6, 7, 60);
        store.scoring.increase(100_000);
    }

    @Override
    public void disappear() {
        dead = true;
        position = new Vector2(-1000,-1000);
        hitbox.setPosition(-1000, -1000);
    }

    @Override
    public Sprite getSprite() {
        return this.sprite;
    }

    @Override
    public TextureRegion getFrame(float accumulator) {
        return animation.getFrame();
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.position = position;
    }
}