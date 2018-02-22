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

  public class Index {
    // TODO: add image for each index

    final int id;
    final String name;
    final Runnable action;

    public Index (String name, Runnable action) {
      this.name = name;
      this.action = action;
      id = actions.size() + 1;
      actions.put(id, this);
    }

    public boolean equals (Object o) {
      if (!(o instanceof Index)) return false;
      return ((Index)o).id == id;
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
    g.fillRect (x, y, MainFrame.WIDTH - x - 10, 20 + actions.size() * 20);

    g.setColor (FOREGROUNDCOLOR);
    for (int i : actions.keySet()) {
      Index index = actions.get(i);
      g.drawString(index.name, x + 30, y + index.id * 20);
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
    selected = Math.min (actions.size(), selected + 1);
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
    actions.get(selected).action.run();
    setVisible (false);
  }

}
