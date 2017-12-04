package com.finalforeach.ld40game;

import com.badlogic.gdx.graphics.Texture;

public class Heart
{
  public static Texture[] img = new Texture[14];
  int texIndex = 0;
  double x;
  double y;
  double z;
  double animationTimer;
  boolean invisible = false;
  
  public void load()
  {
    for (int i = 0; i < img.length; i++) {
      img[i] = new Texture("heart/heart-" + i + ".png");
    }
  }
  
  public void update(double deltaTime)
  {
    if (this.invisible) {
      return;
    }
    this.animationTimer += deltaTime * img.length;
    this.animationTimer %= img.length;
    this.texIndex = ((int)Math.floor(this.animationTimer));
  }
  
  public void draw()
  {
    if (this.invisible) {
      return;
    }
    LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)this.x, (float)this.y, 32, 32, 0, 0, 32, 32, false, false);
  }
}

