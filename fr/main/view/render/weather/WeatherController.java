package fr.main.view.render.weather;

import java.awt.Graphics;

import fr.main.model.Universe;
import fr.main.model.Weather;

public class WeatherController {

  private Snow snow;
  private Rain rain;
  private WeatherRender current;

  public WeatherController () {
    this.snow = new Snow(75);
    this.rain = new Rain(100);
  }

  public void render (Graphics g) {
    Weather w = Universe.get().getWeather();
    if (w == Weather.RAINY) current = rain;
    else if (w == Weather.SNOWY) current = snow;
    else current = null;

    if (current != null) current.render(g);
  }

}
