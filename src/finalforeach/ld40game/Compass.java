package com.finalforeach.ld40game;

import com.badlogic.gdx.graphics.Texture;

public class Compass
{
  public static Texture img;
  int texIndex = 0;
  double x;
  double y;
  double z;
  double animationTimer;
  boolean flipped = false;
  boolean invisible = false;
  
  public void load()
  {
    img = new Texture("compass.png");
  }
  
  public void update(double deltaTime)
  {
    this.animationTimer += deltaTime * 16;
    this.z = (Math.sin(this.animationTimer) * 2);
    int closestIndex = -1;
    double minDist = Double.MAX_VALUE;
    for (int i = 0; i < LD40Game.ninjewels.length; i++)
    {
      double d = Math.abs(LD40Game.ninjewels[i].x - LD40Game.assassin.x);
      if ((!LD40Game.ninjewels[i].invisible & LD40Game.ninjewels[i].canGrab)) {
        if (d < minDist)
        {
          minDist = Math.abs(d);
          closestIndex = i;
        }
      }
    }
    if (((closestIndex != -1 ? 1 : 0) & (minDist > 150 ? 1 : 0)) != 0)
    {
      double dir = LD40Game.ninjewels[closestIndex].x - LD40Game.assassin.x;
      this.flipped = (dir < 0);
      this.invisible = false;
    }
    else
    {
      this.invisible = true;
    }
  }
  
  public void draw()
  {
    if (this.invisible) {
      return;
    }
    LD40Game.getSpriteBatch().draw(img, (float)(this.x - this.z), (float)this.y, 24, 24, 0, 0, 32, 32, this.flipped, false);
  }
}

