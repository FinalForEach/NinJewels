package com.finalforeach.ld40game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class NinJewel implements Drawable
{
  static Sound pickupsound;
  public static int numOfJewels = 0;
  public static Texture[] img = new Texture[14];
  int texIndex = 0;
  double x;
  double y;
  double z;
  double animationTimer;
  boolean canGrab = false;
  boolean invisible = false;
  
  public NinJewel()
  {
    this(true);
  }
  
  public NinJewel(boolean canGrab)
  {
    this.canGrab = canGrab;
  }
  
  public void load()
  {
    pickupsound = Gdx.audio.newSound(Gdx.files.internal("ninjewel-pickup.wav"));
    for (int i = 0; i < img.length; i++) {
      img[i] = new Texture("ninjewel/ninjewel-" + i + ".png");
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
    if (this.canGrab)
    {
      Rectangle bounds = new Rectangle((float)this.x, (float)this.y, 32, 32);
      if (bounds.overlaps(LD40Game.assassin.bounds))
      {
        numOfJewels += 1;
        if (LD40Game.assassin.getHealth() > 1) {
          LD40Game.assassin.hit();
        }
        this.canGrab = false;
        this.invisible = true;
        pickupsound.play();
      }
    }
  }
  
  public void draw()
  {
    if (this.invisible) {
      return;
    }
    LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)this.x, (float)this.y, 32, 32, 0, 0, 32, 32, false, false);
  }
  
  public double getZ()
  {
    return this.z;
  }
}

