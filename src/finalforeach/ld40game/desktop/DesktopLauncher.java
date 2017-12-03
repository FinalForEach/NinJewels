package com.finalforeach.ld40game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.finalforeach.ld40game.LD40Game;

public class DesktopLauncher
{
  public static void main(String[] arg)
  {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "Ninjewels!";
    config.width = 800;
    config.height = 600;
    new LwjglApplication(new LD40Game(), config);
  }
}

