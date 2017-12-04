package com.finalforeach.ld40game;

public class UINinJewelCount extends NinJewel
{
	public UINinJewelCount()
	{
		super(false);
	}

	public void draw()
	{
		LD40Game.getSpriteBatch().setColor(0.1f, 0.1f, 0.1f, 1);
		for (int i = 0; i < 10; i++)
		{
			LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)this.x + 8 * i, (float)this.y, 16, 16, 0, 0, 32, 32, false, false);
		}
		LD40Game.getSpriteBatch().setColor(0.7f, 0.7f, 0.7f, 1);
		for (int i = 0; i < numOfJewels; i++) 
		{
			LD40Game.getSpriteBatch().draw(img[this.texIndex], (float)this.x + 8 * i, (float)this.y, 16, 16, 0, 0, 32, 32, false, false);
		}
		LD40Game.getSpriteBatch().setColor(1, 1, 1, 1);
	}
}

