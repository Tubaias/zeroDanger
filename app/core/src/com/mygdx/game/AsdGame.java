package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.entity.Player;
import com.mygdx.game.entity.bullet.Bullet;
import com.mygdx.game.entity.bullet.BasicBullet;

public class AsdGame extends ApplicationAdapter {
	float w;
	float h;

	SpriteBatch batch;
    Texture playerTexture;
    Texture bulletTexture;
    Sprite playerSprite;

    Player player;
    ArrayList<Sprite> sprites;
    ArrayList<Bullet> bullets;

    float accumulator = 0;

	@Override
	public void create () {
		w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
        playerTexture = new Texture("ship.png");
        bulletTexture = new Texture("bullet.png");

		playerSprite = new Sprite(playerTexture);

        player = new Player(w / 2 - playerSprite.getWidth() / 2, h / 2 - playerSprite.getHeight() / 2, playerSprite);

        sprites = new ArrayList<>();
        bullets = new ArrayList<>();
	}

	@Override
	public void render () {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += delta;
		System.out.println(1 / Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        while (accumulator > 1) {
            Sprite bulletSprite = new Sprite(bulletTexture);
            Bullet bullet = new BasicBullet(player.position.x + 32 - 8, player.position.y + 128, 0, bulletSprite);
            sprites.add(bulletSprite);
            bullets.add(bullet);
            accumulator--;
        }


        handleInputs();


        for (Bullet b : bullets) {
            b.move();
        }

		for (Sprite s : sprites) {
            s.draw(batch);
        }


        player.move();
		player.sprite.draw(batch);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
	}

	private void handleInputs() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                player.position.x += (-3f);
            } else {
                player.position.x += (-7f);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                player.position.x += (3f);
            } else {
                player.position.x += (7f);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                player.position.y += (3f);
            } else {
                player.position.y += (7f);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                player.position.y += (-3f);
            } else {
                player.position.y += (-7f);
            }
        }
    }
}
