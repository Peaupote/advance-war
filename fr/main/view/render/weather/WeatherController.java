package fr.main.view.render.weather;

import java.awt.Graphics;

import fr.main.model.Universe;
import fr.main.model.Weather;
import fr.main.view.render.terrains.TerrainRenderer;

public class WeatherController {

  private Snow snow;
  private Rain rain;
  private WeatherRender current;

  public WeatherController () {
    this.snow = new Snow(75);
    this.rain = new Rain(100);
  }

  public void update(Weather old){
    Weather w = Universe.get().getWeather();
    if (old != w){
      if (w == Weather.RAINY) current = rain;
      else if (w == Weather.SNOWY) current = snow;
      else current = null;

      if (old == Weather.SNOWY ? w != Weather.SNOWY : w == Weather.SNOWY)
        TerrainRenderer.updateAll();
    }
  }

  public void render (Graphics g) {
    if (current != null) current.render(g);
  }

}
