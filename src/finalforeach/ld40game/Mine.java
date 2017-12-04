package com.finalforeach.ld40game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Mine implements Drawable
{
  static Texture img;
  static Sound sound;
  double x;
  double y;
  double z;
  boolean dead;
  
  public void load()
  {
    sound = Gdx.audio.newSound(Gdx.files.internal("mine-explode.wav"));
    img = new Texture("mine.png");
  }
  
  public double getZ()
  {
    return this.z;
  }
  
  public void update(double deltaTime)
  {
    if (this.dead) {
      return;
    }
    if (LD40Game.assassin.bounds.contains((float)this.x + 16, (float)this.y + 16)) {
      if (Math.abs(this.z - LD40Game.assassin.z) < 16)
      {
        LD40Game.assassin.hitBackwards(this.x);
        this.dead = true;
        LD40Game.addNewExplosion(new Explosion(this.x, this.y, this.z));
        sound.play();
      }
    }
  }
  
  public void draw()
  {
    if (this.dead) {
      return;
    }
    LD40Game.getSpriteBatch().draw(img, (float)this.x, (float)(this.y - this.z));
  }
}

