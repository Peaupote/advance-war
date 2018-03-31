package fr.main.view.views;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.IOException;

import fr.main.model.Player;
import fr.main.model.AIPlayer;
import fr.main.model.commanders.*;
import fr.main.view.MainFrame;
import fr.main.view.render.sprites.*;
import fr.main.view.controllers.CreateController;

/**
 * View of commanders selection
 */
public class CreateView extends View {

  protected CreateController controller;

  /**
   * Panels with all commanders
   */
  public class CommandersPanel extends JPanel {

    private final JButton[] btns;
    private final int width, height;

    /**
     * Index of which commander is selected
     */
    private int focus;

    public CommandersPanel () {
      btns = new JButton[6];
      width = 140;
      height = 156;

      setLayout(new GridLayout(2, 3, 10, 10));
      setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT / 2 + 100));
      setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      String[] names = new String[]{
        "contact", "destroy", "money", "repair", "basic", "ranged"
      };

      for (int i = 0; i < names.length; i++)
        btns[i] = new JButton(new ImageIcon(
                        Sprite.get("./assets/commanders/"+names[i]+".png")
                        .getImage(0, 0, width, height)));

      for (int i = 0; i < btns.length; i++) {
        final int target = i;
        btns[i].addActionListener(controller.select);
        btns[i].addActionListener(e -> focus = target);
        add(btns[i]);
      }
    }

    public int getFocus () {
      return focus;
    }

  }

  /**
   * Panel of all players
   */
  public class PlayersPanel extends JPanel {

    private final int width, height;

    /**
     * Index of focused player
     */
    private int focus;

    private JButton[] btns;

    /**
     * Representing the choosed commander
     * selected[i] = k ~ player i choosed commander k
     * -1 meaning no commander choosed (default)
     */
    private int[] selected;

    public PlayersPanel () {
      setLayout(new GridLayout(1, 4, 20, 50));
      setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT / 2 - 125));
      setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

      width = 60;
      height = 100;
      selected = new int[4];
      btns = new JButton[4];

      for (int i = 0; i < btns.length; i++) {
        selected[i] = -1;
        btns[i] = new JButton("Player " + (i + 1));
        final int target = i;
        btns[i].addActionListener(e -> focus = target);
        btns[i].setVerticalAlignment(SwingConstants.TOP);
        add(btns[i]);
      }
    }

    /**
     * Change focused player's commander
     */
    public void updateCommander () {
      btns[focus].setIcon(new ImageIcon(
          ((ImageIcon)commanders.btns[commanders.focus]
          .getIcon()).getImage().getScaledInstance(70, 78, Image.SCALE_DEFAULT)));
      selected[focus] = commanders.focus;
      btns[focus].revalidate();
    }

    /**
     * @return list of all players in the game
     */
    public Player[] getPlayers() {
      ArrayList<Player> ps = new ArrayList<>();
      for (int i = 0; i < selected.length; i++) {
        if (selected[i] == -1) continue;
        Player p;
        if (i == 0)
          p = new Player("Player " + (i + 1)); 
        else
          p = new AIPlayer();
        ps.add(p);
        switch (selected[i]) {
          case 0: new ContactCommander(p); break;
          case 1: new DestroyCommander(p); break;
          case 2: new MoneyCommander(p);   break;
          case 3: new RepairCommander(p);  break;
          case 4: new BasicCommander(p);   break;
          case 5: new RangedCommander(p);  break;
        }
      }
      return ps.toArray(new Player[ps.size()]);
    }

  }

  /**
   * Panel with the single button play.
   */
  public class PlayPanel extends JPanel {
    
    private final JButton play;

    public PlayPanel () {
      setBorder(BorderFactory.createLineBorder(Color.black));
      play = new JButton("Play");

      add(play);
      play.addActionListener(controller.play);
    }
  }

  public final CommandersPanel commanders;
  public final PlayersPanel players;
  public final PlayPanel play;

  public CreateView (CreateController controller) {
    super(controller);

    this.controller = controller;
    setLayout(new BorderLayout());

    commanders = new CommandersPanel();
    players    = new PlayersPanel();
    play       = new PlayPanel();

    JPanel select = new JPanel(new BorderLayout());
    select.add(commanders, BorderLayout.NORTH);
    select.add(players, BorderLayout.SOUTH);
    add(select, BorderLayout.CENTER);

    add(play, BorderLayout.SOUTH);
  }

}
