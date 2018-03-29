package fr.main.view.interfaces;

import java.awt.Graphics;
import java.awt.Color;
import java.lang.Thread;
import java.lang.InterruptedException;

import fr.main.model.Universe;
import fr.main.view.MainFrame;

/**
 * Panel showing when a new day start
 */
public class DayPanel extends InterfaceUI {

  private static final int HEIGHT = 200, MARGINTOP = MainFrame.HEIGHT / 2 - HEIGHT;

  public DayPanel () {
    super(false);
  }

  public void draw (Graphics g) {
    g.setColor (Color.black);
    g.fillRect(0, MARGINTOP, MainFrame.WIDTH, HEIGHT);

    g.setColor (Color.white);
    g.drawString ("Day " + Universe.get().getDay(), MainFrame.WIDTH / 2 - 100, MARGINTOP + 50);
  }

  @Override
  public void onOpen () {
    // TODO: make a real animation here instead of a new thread each time
    new Thread(() -> {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      setVisible(false);
    }).start();
  }

}

