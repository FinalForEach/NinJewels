package com.finalforeach.ld40game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class LD40Game extends ApplicationAdapter
{
	public static final double PIXELS_PER_METER = 16;
	public static final double METERS_PER_PIXEL = 1/PIXELS_PER_METER;
	static SpriteBatch batch;
	static OrthographicCamera camera;
	static Viewport viewport;
	static Texture winimg;
	static Texture gameoverimg;
	static Texture tutorial;
	static Texture backgroundimg;
	static Texture backgroundimgparallax0;
	static Texture backgroundimgparallax1;
	static Texture backgroundimgparallax2;
	static Texture backgroundimgparallax3;
	static Assassin assassin;
	static NinJewel uijewel;
	static UIHeartCount uiheart;
	static Compass uicompass;
	static NinJewel[] ninjewels;
	static Turret[] turrets;
	static Mine[] mines;
	boolean started = false;
	private static ArrayList<Explosion> deadexplosions = new ArrayList<Explosion>();
	static ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	static ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	public static void addNewExplosion(Explosion e)
	{
		explosions.add(e);
		drawables.add(e);
	}

	public void resize(int width, int height)
	{
		viewport.update(width, height);
		viewport.setCamera(camera);
		viewport.apply();
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
	}

	public void create()
	{
		camera = new OrthographicCamera();
		viewport = new StretchViewport(800, 600, camera);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		setup();
		assassin.load();
		turrets[0].load();
		mines[0].load();
		new Explosion().load();
		uijewel.load();
		uiheart.load();
		uicompass.load();
		winimg = new Texture("you-win!.png");
		gameoverimg = new Texture("game-over.png");
		tutorial = new Texture("tutorial.png");
		backgroundimg = new Texture("background.png");
		backgroundimgparallax0 = new Texture("background-parallax0.png");
		backgroundimgparallax1 = new Texture("background-parallax1.png");
		backgroundimgparallax2 = new Texture("background-parallax2.png");
		backgroundimgparallax3 = new Texture("background-parallax3.png");
		centerOnAssassin();
	}

	public void setup()
	{
		this.started = false;
		drawables.clear();
		assassin = new Assassin();
		drawables.add(assassin);
		uijewel = new UINinJewelCount();
		uiheart = new UIHeartCount();
		uicompass = new Compass();
		ninjewels = new NinJewel[10];
		turrets = new Turret[20];
		mines = new Mine[40];
		NinJewel.numOfJewels = 0;
		Random rand = new Random();
		for (int i = 0; i < ninjewels.length; i++)
		{
			ninjewels[i] = new NinJewel();
			ninjewels[i].x = ((int)((rand.nextFloat() - 0.5) * 10000) + 100 * (rand.nextBoolean() ? 1 : -1));
			ninjewels[i].y = 100;
			drawables.add(ninjewels[i]);
		}
		for (int i = 0; i < turrets.length; i++)
		{
			turrets[i] = new Turret();
			turrets[i].x = ((int)((rand.nextFloat() - 0.5) * 10000) + 100 * (rand.nextBoolean() ? 1 : -1));
			turrets[i].y = 0;
			turrets[i].z = (rand.nextFloat() * 12 + 20);
			for (int j = 0; j < i; j++)
			{
				if (Math.abs(turrets[i].x - turrets[j].x) < 100)
				{
					i--;
				}
			}
			drawables.add(turrets[i]);
		}
		for (int i = 0; i < mines.length; i++)
		{
			mines[i] = new Mine();
			mines[i].x = ((int)((rand.nextFloat() - 0.5D) * 10000) + 100 * (rand.nextBoolean() ? 1 : -1));
			mines[i].y = 0;
			mines[i].z = (rand.nextFloat() * 12 + 20);
		}
	}

	public void centerOnScene()
	{
		camera.zoom = 0.5f;
		camera.translate(0, 100);
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
	}

	public void centerOnAssassin()
	{
		camera.zoom = 0.5f;
		camera.position.set((float)assassin.x, (float)(assassin.y - assassin.z), 0);

		camera.translate((float)assassin.walkScroll, 0);
		camera.translate(16, 16);
		camera.translate(0, 100);
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
	}

	public void render()
	{
		double deltaTime = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0.43137255f, 0.30980393f, 0.53333336f, 1);
		Gdx.gl.glClear(16384);

		camera.zoom = 0.5f;
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		Vector3 mouseVec;
		if (!this.started)
		{
			camera.zoom = 0.5f;
			camera.position.set((float)assassin.x, (float)(assassin.y - assassin.z), 0);
			camera.update();
			batch.setProjectionMatrix(viewport.getCamera().combined);
			float rx = camera.position.x - camera.viewportWidth / 4;
			float ry = camera.position.y - 150;
			Rectangle startrect = new Rectangle(rx + 4, ry - 100 - camera.viewportHeight / 4 + 255, 
					390, 41);
			mouseVec = new Vector3(Gdx.input.getX(), 
					Gdx.input.getY(), 0);

			mouseVec = camera.unproject(mouseVec, viewport.getScreenX(), viewport.getScreenY(), 
					viewport.getScreenWidth(), viewport.getScreenHeight());
			if ((startrect.contains(mouseVec.x, mouseVec.y) & Gdx.input.justTouched())) 
			{
				this.started = true;
			}
			batch.begin();
			batch.setColor(1, 1, 1, 1);
			batch.draw(tutorial, rx, ry);
			batch.end();
			return;
		}
		assassin.update(deltaTime);
		for (int i = 0; i < ninjewels.length; i++)
		{
			ninjewels[i].update(deltaTime);
		}
		for (int i = 0; i < turrets.length; i++)
		{
			turrets[i].update(deltaTime);
		}
		for (Mine m : mines)
		{
			m.update(deltaTime);
		}
		for (Explosion e : explosions)
		{
			e.update(deltaTime);
			if (e.dead) {
				deadexplosions.add(e);
			}
		}
		explosions.removeAll(deadexplosions);
		deadexplosions.clear();
		uijewel.x = (camera.position.x - camera.viewportWidth / 4);
		uijewel.y = (camera.position.y + camera.viewportHeight / 4 - 32);
		uijewel.update(deltaTime);
		uiheart.x = (camera.position.x - camera.viewportWidth / 4);
		uiheart.y = (camera.position.y + camera.viewportHeight / 4 - 16);
		uiheart.update(deltaTime);

		uicompass.update(deltaTime);
		if (uicompass.flipped) {
			uicompass.x = (camera.position.x - camera.viewportWidth / 4 + 32);
		} else {
			uicompass.x = (camera.position.x + camera.viewportWidth / 4 - 32);
		}
		uicompass.y = (camera.position.y - 12);
		drawables.sort(new Comparator<Drawable>()
		{
			public int compare(Drawable o1, Drawable o2)
			{
				return (int)(o1.getZ() - o2.getZ());
			}
		});
		batch.begin();
		drawBackground();
		for (Mine m : mines)
		{
			m.draw();
		}
		for (Drawable d : drawables) {
			d.draw();
		}
		uijewel.draw();
		uiheart.draw();
		uicompass.draw();
		if (NinJewel.numOfJewels >= 10)
		{
			batch.draw(winimg, 
					camera.position.x - camera.viewportWidth / 4 + 100, 
					camera.position.y - 75);
			if (Gdx.input.justTouched())
			{
				batch.end();
				setup();
			}
		}
		else if (assassin.getHealth() <= 0)
		{
			batch.draw(gameoverimg, 
					camera.position.x - camera.viewportWidth / 4 + 100, 
					camera.position.y - 75);
			if (Gdx.input.justTouched())
			{
				batch.end();
				setup();
				return;
			}
		}
		centerOnAssassin();
		batch.end();
	}

	public void drawBackground()
	{
		float bgX = camera.position.x - camera.position.x % 800;
		float pSpd = (float)assassin.x / 60;
		pSpd = 3 * pSpd % 2400;
		pSpd /= 3;
		batch.draw(backgroundimg, bgX, -84);
		batch.draw(backgroundimg, bgX + 800, -84);
		batch.draw(backgroundimg, bgX + 1600, -84);
		batch.draw(backgroundimg, bgX - 800, -84);
		batch.draw(backgroundimg, bgX - 1600, -84);

		camera.translate(3 * pSpd, 0);
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.draw(backgroundimgparallax3, bgX, -84);
		batch.draw(backgroundimgparallax3, bgX + 800, -84);
		batch.draw(backgroundimgparallax3, bgX + 1600, -84);
		batch.draw(backgroundimgparallax3, bgX - 800, -84);
		batch.draw(backgroundimgparallax3, bgX - 1600, -84);

		camera.translate(-pSpd, 0);
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.draw(backgroundimgparallax2, bgX, -84);
		batch.draw(backgroundimgparallax2, bgX + 800, -84);
		batch.draw(backgroundimgparallax2, bgX + 1600, -84);
		batch.draw(backgroundimgparallax2, bgX - 800, -84);
		batch.draw(backgroundimgparallax2, bgX - 1600, -84);

		camera.translate(-pSpd, 0);
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.draw(backgroundimgparallax1, bgX, -84);
		batch.draw(backgroundimgparallax1, bgX + 800, -84);
		batch.draw(backgroundimgparallax1, bgX + 1600, -84);
		batch.draw(backgroundimgparallax1, bgX - 800, -84);
		batch.draw(backgroundimgparallax1, bgX - 1600, -84);

		camera.translate(-pSpd, 0);
		camera.update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.draw(backgroundimgparallax0, bgX, -84);
		batch.draw(backgroundimgparallax0, bgX + 800, -84);
		batch.draw(backgroundimgparallax0, bgX + 1600, -84);
		batch.draw(backgroundimgparallax0, bgX - 800, -84);
		batch.draw(backgroundimgparallax0, bgX - 1600, -84);
	}

	public void dispose()
	{
		batch.dispose();
		assassin.dispose();
	}

	public static SpriteBatch getSpriteBatch()
	{
		return batch;
	}
}

