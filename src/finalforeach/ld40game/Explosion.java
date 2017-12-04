package com.finalforeach.ld40game;

import com.badlogic.gdx.graphics.Texture;

public class Explosion implements Drawable
{
  public static Texture[] img = new Texture[7];
  int texIndex = 0;
  double x;
  double y;
  double z;
  double animationTimer;
  boolean dead = false;
  
  public Explosion() {}
  
  public Explosion(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public void load()
  {
    for (int i = 0; i < img.length; i++) {
      img[i] = new Texture("explosion/explosion-" + (i + 1) + ".png");
    }
  }
  
  public void update(double deltaTime)
  {
    if (this.dead) {
      return;
    }
    this.animationTimer += deltaTime * 10;
    if (this.animationTimer > img.length) {
      this.dead = true;
    }
    this.texIndex = ((int)Math.floor(this.animationTimer));
  }
  
  public void draw()
  {
    if (this.dead) {
      return;
    }
    LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)this.x, (float)this.y, 64, 64, 0, 0, 64, 64, false, false);
  }
  
  public double getZ()
  {
    return this.z;
  }
}

