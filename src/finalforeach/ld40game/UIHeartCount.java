package com.finalforeach.ld40game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UIHeartCount extends Heart
{
  public void draw()
  {
    double xoff = 0;
    if (LD40Game.assassin.hitCooldown > 0) {
      xoff = Math.sin(LD40Game.assassin.hitCooldown * 2 * 3.141592653589793D * 50);
    }
    LD40Game.getSpriteBatch().setColor(0.1F, 0.1F, 0.1F, 1);
    for (int i = 0; i < 10; i++) {
      LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)(this.x + 8 * i + xoff), (float)this.y, 16, 16, 0, 0, 32, 32, false, false);
    }
    LD40Game.getSpriteBatch().setColor(0.7F, 0.7F, 0.7F, 1);
    for (int i = 0; i < LD40Game.assassin.getHealth(); i++) {
      LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)(this.x + 8 * i + xoff), (float)this.y, 16, 16, 0, 0, 32, 32, false, false);
    }
    LD40Game.getSpriteBatch().setColor(1, 1, 1, 1);
  }
}

