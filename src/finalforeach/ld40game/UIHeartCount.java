package com.finalforeach.ld40game;


public class UIHeartCount extends Heart
{
	public void draw()
	{
		double xoff = 0;
		if (LD40Game.assassin.hitCooldown > 0) {
			xoff = Math.sin(LD40Game.assassin.hitCooldown * 2 * Math.PI * 50);
		}
		LD40Game.getSpriteBatch().setColor(0.1f, 0.1f, 0.1f, 1);
		for (int i = 0; i < 10; i++) 
		{
			LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)(this.x + 8 * i + xoff), (float)this.y, 16, 16, 0, 0, 32, 32, false, false);
		}
		LD40Game.getSpriteBatch().setColor(0.7f, 0.7f, 0.7f, 1);
		for (int i = 0; i < LD40Game.assassin.getHealth(); i++) 
		{
			LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)(this.x + 8 * i + xoff), (float)this.y, 16, 16, 0, 0, 32, 32, false, false);
		}
		LD40Game.getSpriteBatch().setColor(1, 1, 1, 1);
	}
}

