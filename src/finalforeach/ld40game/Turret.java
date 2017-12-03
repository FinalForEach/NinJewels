package com.finalforeach.ld40game;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;
import java.util.ArrayList;

public class Turret implements Drawable
{
  static Sound shootsound;
  static Texture baseimg;
  static Texture pipeimg;
  static Texture bulletimg;
  ArrayList<Bullet> bullets = new ArrayList();
  private ArrayList<Bullet> deadbullets = new ArrayList();
  double x;
  double y;
  double z;
  double pipeAngle = 0;
  double fireTimer = -1;
  
  public void load()
  {
    shootsound = Gdx.audio.newSound(Gdx.files.internal("turret-shoot.wav"));
    baseimg = new Texture("turret-base.png");
    pipeimg = new Texture("turret-pipe.png");
    bulletimg = new Texture("turret-bullet.png");
  }
  
  public void update(double deltaTime)
  {
    double ax = LD40Game.assassin.x;
    double ay = LD40Game.assassin.y;
    this.pipeAngle = (Math.atan2(ay - (this.y - 40), ax - this.x) / 3.141592653589793D * 180);
    
    this.fireTimer += deltaTime;
    if (this.fireTimer >= 1)
    {
      this.fireTimer = -1;
      this.bullets.add(new Bullet(this.x - 16, this.y, this.z, this.pipeAngle));
      if (LD40Game.camera.frustum.boundsInFrustum((float)this.x, (float)this.y, 0, 16, 16, 0)) {
        shootsound.play();
      }
    }
    for (Bullet bullet : this.bullets)
    {
      bullet.update(deltaTime);
      if (bullet.dead) {
        this.deadbullets.add(bullet);
      }
    }
    this.bullets.removeAll(this.deadbullets);
    this.deadbullets.clear();
  }
  
  public void draw()
  {
    for (Bullet bullet : this.bullets) {
      bullet.draw();
    }
    LD40Game.getSpriteBatch().draw(pipeimg, (float)(this.x + 16), (float)(this.y - this.z), 0, 16, 32, 32, 1, 1, (float)this.pipeAngle, 0, 0, 32, 32, false, (this.pipeAngle > 90 ? 1 : 0) | (this.pipeAngle < -90 ? 1 : 0));
    LD40Game.getSpriteBatch().draw(baseimg, (float)this.x, (float)(this.y - this.z), 32, 32);
  }
  
  public double getZ()
  {
    return this.z;
  }
}

