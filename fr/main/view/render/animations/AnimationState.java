package fr.main.view.render.animations;

import java.awt.Graphics;
import java.awt.Image;

import fr.main.view.render.sprites.SpriteList;
import fr.main.view.MainFrame;

public class AnimationState {

  private SpriteList images;
  private int frameRate;

  public AnimationState (SpriteList images, int frameRate) {
    this.images    = images;
    this.frameRate = frameRate;
  }

  public void draw(Graphics g, int x, int y) {
    if (MainFrame.getTimer() % frameRate == 0) nextFrame(); 

    g.drawImage(images.element(), x, y, null);
  }

  private void nextFrame () {
    Image tmp = images.poll();
    images.addLast(tmp);
  }

}
