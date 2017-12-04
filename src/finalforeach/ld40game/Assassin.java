package com.finalforeach.ld40game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Assassin implements Drawable {
	public static Texture[] img = new Texture[17];
	static Sound jumpsound;
	static Sound hurtsound;
	int texIndex = 0;
	double x;
	double y;
	double z;
	double xvel;
	double yvel;
	double xaccel;
	double yaccel;
	boolean isFacingRight = true;
	double jumpTimer = -1.0;
	double maxJumpTimer = 0.25;
	double walkTimer = 0;
	double maxWalkTimer = 0.5;
	boolean jumpPeaked = false;
	boolean isWalking = false;
	boolean isMovingX = false;
	double walkScroll = 0;
	double hitCooldown = 0;
	double stillTime = 0;
	int health = 10;
	Rectangle bounds = new Rectangle(0, 0, 32, 32);

	public void load() 
	{
		jumpsound = Gdx.audio.newSound(Gdx.files.internal("ninja-jump.wav"));
		hurtsound = Gdx.audio.newSound(Gdx.files.internal("ninja-hurt.wav"));
		img[0] = new Texture("assassin/assassin-idle1.png");
		for (int i = 1; i <= 7; i++) 
		{
			img[i] = new Texture("assassin/assassin-jump" + i + ".png");
		}
		for (int i = 1; i <= 9; i++) 
		{
			img[i + 7] = new Texture("assassin/assassin-walk" + i + ".png");
		}
	}

	public void dispose() 
	{
		for (int i = 0; i < img.length; i++) 
		{
			img[i].dispose();
		}
	}

	public void draw() 
	{
		if (getHealth() <= 0) {
			return;
		}
		LD40Game.getSpriteBatch().draw(img[texIndex], (float) x, (float) (y - z), 32, 32, 0, 0, 32, 32, !isFacingRight,
				false);
	}

	double walkingSpeed = 100;

	public void update(double deltaTime)
	{
		if (getHealth() <= 0) {
			return;
		}
		if ((Gdx.input.isKeyPressed(Keys.RIGHT) | Gdx.input.isKeyPressed(Keys.D))) 
		{
			x += deltaTime * walkingSpeed;
			walkingSpeed = Math.min(walkingSpeed + deltaTime * 10, 1000);
			isFacingRight = true;
			if (y == 0) {
				isWalking = (isMovingX = true);
			}
		}
		if ((Gdx.input.isKeyPressed(Keys.LEFT) | Gdx.input.isKeyPressed(Keys.A)))
		{
			x -= deltaTime * walkingSpeed;
			walkingSpeed = Math.min(walkingSpeed + deltaTime * 10, 1000);
			isFacingRight = false;
			if (y == 0) {
				isWalking = (isMovingX = true);
			}
		}
		if ((Gdx.input.isKeyPressed(Keys.UP) | Gdx.input.isKeyPressed(Keys.W)))
		{
			z -= deltaTime * 100;
			z = Math.max(-8, z);
			if (y == 0) {
				isWalking = true;
			}
		}
		if ((Gdx.input.isKeyPressed(Keys.DOWN) | Gdx.input.isKeyPressed(Keys.S)))
		{
			z += deltaTime * 100;
			z = Math.min(32, z);
			if (y == 0) {
				isWalking = true;
			}
		}
		if ((Gdx.input.isKeyJustPressed(Keys.SPACE) & isOnGround()))
		{
			jumpTimer = 0;
			jumpPeaked = false;
			yvel = 500;
			jumpsound.play();
		}
		if (jumpTimer != -1) 
		{
			if (jumpPeaked) 
			{
				if (texIndex == 7) 
				{
					jumpTimer -= deltaTime / 20;
				} else {
					jumpTimer -= deltaTime;
				}
			} else {
				jumpTimer += deltaTime;
			}
			if (jumpTimer > maxJumpTimer)
			{
				jumpPeaked = true;
			}
			if (jumpTimer < 0) {
				jumpTimer = -1;
				texIndex = 0;
			}
		}
		if (jumpTimer != -1) {
			texIndex = ((int) Math.floor(jumpTimer / maxJumpTimer * 6) + 1);
		}
		if ((isWalking & jumpTimer == -1)) 
		{
			if (isMovingX) 
			{
				stillTime = 0;
				if (isFacingRight) 
				{
					if (walkScroll < 0)
					{
						walkScroll *= (1 - deltaTime);
					}
					walkScroll += deltaTime * 10;
					walkScroll = Math.min(walkScroll, 100);
				} else 
				{
					if (walkScroll > 0) 
					{
						walkScroll *= (1 - deltaTime);
					}
					walkScroll -= deltaTime * 10;
					walkScroll = Math.max(walkScroll, -100);
				}
			}
			walkTimer += deltaTime;
			walkTimer %= maxWalkTimer;
			texIndex = (7 + (int) Math.floor(walkTimer / maxWalkTimer * 9) + 1);
			if (texIndex == 16) 
			{
				if (isOnGround()) 
				{
					isWalking =isMovingX = false;
				}
				texIndex = 0;
			}
		}
		if (!isMovingX)
		{
			if (stillTime > 0.1)
			{
				walkingSpeed = 100;
			}
			stillTime += deltaTime;
			if (stillTime > 1) {
				walkScroll *= (1 - deltaTime);
			}
		}
		updatePhysics(deltaTime);
		bounds.x = ((float) x);
		bounds.y = ((float) y);
		bounds.width = 32;
		bounds.height = 32;
		hitCooldown -= deltaTime;
	}

	public boolean isOnGround()
	{
		return y <= 0;
	}

	public void updatePhysics(double deltaTime) 
	{
		x += xvel * deltaTime;
		y += yvel * deltaTime;
		xvel += xaccel * deltaTime;
		yvel += yaccel * deltaTime;
		if (!isOnGround()) {
			yaccel -= LD40Game.METERS_PER_PIXEL*9.8;
		}
		if (isOnGround()) {
			xvel = (y = yvel = yaccel = 0);
		}
	}

	public int getHealth() {
		return health;
	}

	public void hit()
	{
		if (hitCooldown <= 0)
		{
			health -= 1;
			hitCooldown = 1;
		}
		yvel += 300;
		hurtsound.play();
	}

	public void hitForwards(double xVel)
	{
		hit();
		xvel += (xVel > 0 ? 100 + walkingSpeed : -100 - walkingSpeed);
	}

	public void hitBackwards(double xSrc)
	{
		hit();
		xvel += (x < xSrc ? -100 - walkingSpeed : 100 + walkingSpeed);
	}

	public double getZ() {
		return z;
	}
}
