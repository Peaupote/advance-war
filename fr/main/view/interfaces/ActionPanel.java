package fr.main.view.interfaces;

import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionListener;

import fr.main.view.MainFrame;

public class ActionPanel extends InterfaceUI {

  protected HashMap<Index, ActionListener> actions;
  protected int selected;
  protected static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  protected static final Color FOREGROUNDCOLOR = Color.white;

  protected int x, y;

  public static class Index {
    // TODO: add image for each index

    protected static int incr = 0;
    final int id;
    final String name;

    public Index (String name) {
      this.name = name;
      id = ++incr;
    }

    public boolean equals (Object o) {
      if (!(o instanceof Index)) return false;
      return ((Index)o).id == id;
    }

  }

  public ActionPanel (HashMap<Index, ActionListener> actions) {
    super(false);
    this.actions = actions;
    selected = 1;
  }

  @Override
  protected void draw (Graphics g) {
    g.setColor(BACKGROUNDCOLOR);
    g.fillRect (x, y, MainFrame.WIDTH - x - 10, 80);

    g.setColor (FOREGROUNDCOLOR);
    for (Index opt : actions.keySet())
      g.drawString(opt.name, x + 30, y + opt.id * 20);

    // TODO: find a nice image for the arrow
    g.setColor(Color.red);
    g.drawString ("->", x + 10, y + selected * 20);
  }

  public void moveTop () {
    selected = 1;
  }

  public void goUp () {
    selected = Math.max(1, selected - 1);
  }

  public void goDown () {
    selected = Math.min (Index.incr, selected + 1);
  }

  @Override
  public void onOpen () {
    selected = 1;
  }

  public void perform () {
    for (Index i : actions.keySet())
      if (i.id == selected) {
        actions.get(i).actionPerformed(null);
        setVisible (false);
        return;
      }
  }

}
