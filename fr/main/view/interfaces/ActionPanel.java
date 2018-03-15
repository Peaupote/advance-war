package fr.main.view.interfaces;

import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Runnable;

import fr.main.view.MainFrame;

public class ActionPanel extends InterfaceUI {

  protected HashMap<Integer, Index> actions;
  protected int selected;
  protected static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  protected static final Color FOREGROUNDCOLOR = Color.white;

  protected int x, y;

  private int actives;

  public class Index {
    // TODO: add image for each index

    final int id;
    final String name;
    final Runnable action;

    private boolean active;

    public Index (String name, Runnable action) {
      this.name = name;
      this.action = action;
      id = actions.size() + 1;
      actions.put(id, this);

      active = true;
      actives++;
    }

    public boolean equals (Object o) {
      if (!(o instanceof Index)) return false;
      return ((Index)o).id == id;
    }

    public void setActive (boolean active) {
      if (active && !this.active) actives++;
      else if (!active && this.active) actives--;
      this.active = active;
    }

    public boolean isActive () {
      return active;
    }

  }

  public ActionPanel (HashMap<Integer, Index> actions) {
    super(false);
    this.actions = actions;
    selected = 1;
  }

  public ActionPanel () {
    super(false);
    selected = 1;
    actions = new HashMap<>();
  }

  @Override
  protected void draw (Graphics g) {
    g.setColor(BACKGROUNDCOLOR);
    g.fillRect (x, y, MainFrame.WIDTH - x - 10, 20 + actives * 20);

    g.setColor (FOREGROUNDCOLOR);
    int j = 0;
    for (Index index : actions.values())
      if (index.active) {
        j++;
        g.drawString(index.name, x + 30, y + j * 20);
      }

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
    selected = Math.min (actives, selected + 1);
  }

  public void onOpen () {
    selected = 1;
    for (InterfaceUI com: InterfaceUI.components())
      if (!(com instanceof ActionPanel) && hideOnOpen(com)) com.setVisible(false);
  }

  public void onClose () {
    for (InterfaceUI com: InterfaceUI.components())
      if (!(com instanceof ActionPanel) && showOnClose(com)) com.setVisible(true);
  }

  public boolean showOnClose(InterfaceUI com) { return true; }
  public boolean hideOnOpen(InterfaceUI com) { return true; }

  public void perform () {
    int j = 1;
    for (Index i : actions.values())
      if (i.active && j == selected){
        new Thread(i.action::run).start();
        break;
      }
      else if (i.active)
        j++;
    setVisible (false);
  }

}
