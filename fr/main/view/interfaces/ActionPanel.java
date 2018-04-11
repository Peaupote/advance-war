package fr.main.view.interfaces;

import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Runnable;

import fr.main.view.MainFrame;

/**
 * Game panel to select actions
 */
public class ActionPanel extends InterfaceUI {

  /**
   * All existing actions
   */
  protected HashMap<Integer, Index> actions;

  /**
   * Index of selected action
   */
  protected int selected;

  protected static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  protected static final Color FOREGROUNDCOLOR = Color.white;

  protected int x, y;

  /**
   * Number of actives actions
   */
  private int actives;

  /**
   * Representing a single action
   */
  public class Index {
    // TODO: add image for each index

    public final int id;
    public final String name;
    public final Runnable action;

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

    public int hashCode(){
      return super.hashCode();
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
    g.fillRect (x, y, MainFrame.width() - x - 10, 20 + actives * 20);

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

  /**
   * Select first action
   */
  public void moveTop () {
    selected = 1;
  }

  /**
   * Select the action above the current selected
   */
  public void goUp () {
    selected = Math.max(1, selected - 1);
  }

  /**
   * Select the action under the current selected
   */
  public void goDown () {
    selected = Math.min (actives, selected + 1);
  }

  /**
   * Call when setVisible(true)
   */
  public void onOpen () {
    selected = 1;
    for (InterfaceUI com: InterfaceUI.components())
      if (!(com instanceof ActionPanel) && hideOnOpen(com)) com.setVisible(false);
  }

  /**
   * Call when setVisible(false)
   */
  public void onClose () {
    for (InterfaceUI com: InterfaceUI.components())
      if (!(com instanceof ActionPanel) && showOnClose(com)) com.setVisible(true);
  }

  public boolean showOnClose(InterfaceUI com) { return true; }
  public boolean hideOnOpen(InterfaceUI com) { return true; }

  /**
   * Run the selected action
   */
  public void perform () {
    int j = 1;
    for (Index i : actions.values())
      if (i.active && j == selected){
        new Thread(i.action::run).start();
        break;
      } else if (i.active) j++;
    setVisible (false);
  }

}
