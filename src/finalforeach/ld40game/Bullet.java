package com.finalforeach.ld40game;

import com.badlogic.gdx.math.Vector3;

class Bullet implements Drawable
{
	Vector3 pos;
	boolean dead = false;
	double angle;
	double xvel;
	double yvel;
	double xaccel;
	double yaccel;
	double timeSpeed = 0.1;

	Bullet(double x, double y, double z, double dirAngle)
	{
		this.pos = new Vector3((float)(x + 16), (float)y, (float)z);
		this.angle = dirAngle;
		this.xvel = (1000 * Math.cos(this.angle * Math.PI / 180));
		this.yvel = (1000 * Math.sin(this.angle * Math.PI / 180));
	}

	public void update(double deltaTime)
	{
		if (LD40Game.assassin.bounds.contains(this.pos.x + 16, this.pos.y + 16)) {
			if (Math.abs(this.pos.z - LD40Game.assassin.z) < 16)
			{
				LD40Game.assassin.hitForwards(this.xvel);
				this.dead = true;
			}
		}
		updatePhysics(deltaTime);
	}

	public void updatePhysics(double deltaTime)
	{
		Vector3 tmp4_1 = this.pos;tmp4_1.x = ((float)(tmp4_1.x + this.xvel * deltaTime * this.timeSpeed)); Vector3 
		tmp29_26 = this.pos;tmp29_26.y = ((float)(tmp29_26.y + this.yvel * deltaTime * this.timeSpeed));
		this.xvel += this.xaccel * deltaTime * this.timeSpeed;
		this.yvel += this.yaccel * deltaTime * this.timeSpeed;
		if (this.pos.y != -16) {
			this.yaccel -= LD40Game.METERS_PER_PIXEL*9.8;
		}
		if (this.pos.y < -16)
		{
			this.pos.y = ((float)(this.yvel = this.yaccel = 0));
			this.dead = true;
		}
	}

	public void draw()
	{
		LD40Game.getSpriteBatch().draw(Turret.bulletimg, this.pos.x, this.pos.y - this.pos.z, 32, 32);
	}

	public double getZ()
	{
		return this.pos.z;
	}
}

