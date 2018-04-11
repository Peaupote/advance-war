package fr.main.view.interfaces;

import java.awt.Graphics;
import java.awt.Color;
import java.lang.Thread;
import java.lang.InterruptedException;

import fr.main.model.Universe;
import fr.main.view.MainFrame;
import fr.main.view.sound.Sdfx;

/**
 * Panel showing when a new day start
 */
public class DayPanel extends InterfaceUI {

  public static final int PANEL_TIME = 1500;
  private static final int HEIGHT = 200, MARGINTOP = MainFrame.height() / 2 - HEIGHT;

  public DayPanel () {
    super(false);
  }

  public void draw (Graphics g) {
    g.setColor (Color.black);
    g.fillRect(0, MARGINTOP, MainFrame.width(), HEIGHT);

    g.setColor (Color.white);
    g.drawString ("Day " + Universe.get().getDay(), MainFrame.width() / 2 - 100, MARGINTOP + 50);
  }

  @Override
  public void onOpen () {
    Sdfx.NEW_DAY.play();
    // TODO: make a real animation here instead of a new thread each time
    new Thread(() -> {
      try {
        Thread.sleep(PANEL_TIME);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      setVisible(false);
    }).start();
  }

}

