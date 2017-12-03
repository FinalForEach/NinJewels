package com.finalforeach.ld40game;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Assassin
  implements Drawable
{
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
  double walkTimer = 0.0;
  double maxWalkTimer = 0.5;
  boolean jumpPeaked = false;
  boolean isWalking = false;
  boolean isMovingX = false;
  double walkScroll = 0.0;
  double hitCooldown = 0.0;
  double stillTime = 0.0;
  int health = 10;
  Rectangle bounds = new Rectangle(0.0, 0.0, 32, 32);
  
  public void load()
  {
    jumpsound = Gdx.audio.newSound(Gdx.files.internal("ninja-jump.wav"));
    hurtsound = Gdx.audio.newSound(Gdx.files.internal("ninja-hurt.wav"));
    img[0] = new Texture("assassin/assassin-idle1.png");
    for (int i = 1; i <= 7; i++) {
      img[i] = new Texture("assassin/assassin-jump" + i + ".png");
    }
    for (int i = 1; i <= 9; i++) {
      img[(i + 7)] = new Texture("assassin/assassin-walk" + i + ".png");
    }
  }
  
  public void dispose()
  {
    for (int i = 0; i < img.length; i++) {
      img[i].dispose();
    }
  }
  
  public void draw()
  {
    if (getHealth() <= 0) {
      return;
    }
    LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)this.x, (float)(this.y - this.z), 32, 32, 0, 0, 32, 32, !this.isFacingRight, false);
  }
  
  double walkingSpeed = 100;
  
  public void update(double deltaTime)
  {
    if (getHealth() <= 0) {
      return;
    }
    if ((Gdx.input.isKeyPressed(22) | Gdx.input.isKeyPressed(32)))
    {
      this.x += deltaTime * this.walkingSpeed;
      this.walkingSpeed = Math.min(this.walkingSpeed + deltaTime * 10, 1000);
      this.isFacingRight = true;
      if (this.y == 0) {
        this.isWalking = (this.isMovingX = 1);
      }
    }
    if ((Gdx.input.isKeyPressed(21) | Gdx.input.isKeyPressed(29)))
    {
      this.x -= deltaTime * this.walkingSpeed;
      this.walkingSpeed = Math.min(this.walkingSpeed + deltaTime * 10, 1000);
      this.isFacingRight = false;
      if (this.y == 0) {
        this.isWalking = (this.isMovingX = 1);
      }
    }
    if ((Gdx.input.isKeyPressed(19) | Gdx.input.isKeyPressed(51)))
    {
      this.z -= deltaTime * 100;
      this.z = Math.max(-8, this.z);
      if (this.y == 0) {
        this.isWalking = true;
      }
    }
    if ((Gdx.input.isKeyPressed(20) | Gdx.input.isKeyPressed(47)))
    {
      this.z += deltaTime * 100;
      this.z = Math.min(32, this.z);
      if (this.y == 0) {
        this.isWalking = true;
      }
    }
    if ((Gdx.input.isKeyJustPressed(62) & isOnGround()))
    {
      this.jumpTimer = 0;
      this.jumpPeaked = false;
      this.yvel = 500;
      jumpsound.play();
    }
    if (this.jumpTimer != -1)
    {
      if (this.jumpPeaked)
      {
        if (this.texIndex == 7) {
          this.jumpTimer -= deltaTime / 20;
        } else {
          this.jumpTimer -= deltaTime;
        }
      }
      else {
        this.jumpTimer += deltaTime;
      }
      if (this.jumpTimer > this.maxJumpTimer) {
        this.jumpPeaked = true;
      }
      if (this.jumpTimer < 0)
      {
        this.jumpTimer = -1;
        this.texIndex = 0;
      }
    }
    if (this.jumpTimer != -1) {
      this.texIndex = ((int)Math.floor(this.jumpTimer / this.maxJumpTimer * 6) + 1);
    }
    if ((this.isWalking & this.jumpTimer == -1))
    {
      if (this.isMovingX)
      {
        this.stillTime = 0;
        if (this.isFacingRight)
        {
          if (this.walkScroll < 0) {
            this.walkScroll *= (1 - deltaTime);
          }
          this.walkScroll += deltaTime * 10;
          this.walkScroll = Math.min(this.walkScroll, 100);
        }
        else
        {
          if (this.walkScroll > 0) {
            this.walkScroll *= (1 - deltaTime);
          }
          this.walkScroll -= deltaTime * 10;
          this.walkScroll = Math.max(this.walkScroll, -100);
        }
      }
      this.walkTimer += deltaTime;
      this.walkTimer %= this.maxWalkTimer;
      this.texIndex = (7 + (int)Math.floor(this.walkTimer / this.maxWalkTimer * 9) + 1);
      if (this.texIndex == 16)
      {
        if (isOnGround()) {
          this.isWalking = (this.isMovingX = 0);
        }
        this.texIndex = 0;
      }
    }
    if (!this.isMovingX)
    {
      if (this.stillTime > 0.1D) {
        this.walkingSpeed = 100;
      }
      this.stillTime += deltaTime;
      if (this.stillTime > 1) {
        this.walkScroll *= (1 - deltaTime);
      }
    }
    updatePhysics(deltaTime);
    this.bounds.x = ((float)this.x);
    this.bounds.y = ((float)this.y);
    this.bounds.width = 32;
    this.bounds.height = 32;
    this.hitCooldown -= deltaTime;
  }
  
  public boolean isOnGround()
  {
    return this.y <= 0;
  }
  
  public void updatePhysics(double deltaTime)
  {
    this.x += this.xvel * deltaTime;
    this.y += this.yvel * deltaTime;
    this.xvel += this.xaccel * deltaTime;
    this.yvel += this.yaccel * deltaTime;
    if (!isOnGround()) {
      this.yaccel -= 156.8D;
    }
    if (isOnGround()) {
      this.xvel = (this.y = this.yvel = this.yaccel = 0);
    }
  }
  
  public int getHealth()
  {
    return this.health;
  }
  
  public void hit()
  {
    if (this.hitCooldown <= 0)
    {
      this.health -= 1;
      this.hitCooldown = 1;
    }
    this.yvel += 300;
    hurtsound.play();
  }
  
  public void hitForwards(double xVel)
  {
    hit();
    this.xvel += (xVel > 0 ? 100 + this.walkingSpeed : -100 - this.walkingSpeed);
  }
  
  public void hitBackwards(double xSrc)
  {
    hit();
    this.xvel += (this.x < xSrc ? -100 - this.walkingSpeed : 100 + this.walkingSpeed);
  }
  
  public double getZ()
  {
    return this.z;
  }
}

