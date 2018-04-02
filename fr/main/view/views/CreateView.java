package fr.main.view.views;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.util.*;
import java.io.IOException;

import fr.main.model.players.Player;
import fr.main.model.players.AIPlayer;
import fr.main.model.commanders.*;
import fr.main.view.MainFrame;
import fr.main.view.components.MenuButton;
import fr.main.view.controllers.CreateController;
import fr.main.view.controllers.LoadController;

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
    private final JButton startGame;
    private final int width, height;

    /**
     * Index of which commander is selected
     */
    private int focus;
	private Image image;

    public CommandersPanel () throws IOException {
      btns = new JButton[6];
      width = 140;
      height = 156;
      image = new ImageIcon("./assets/acc.jpg").getImage();
      
      setLayout(null);
      setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT / 2 + 100));
      setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
      btns[0] = new MenuButton("./assets/commanders/contact01.png", 300, 20);
      btns[1] = new MenuButton("./assets/commanders/destroy01.png", 300, 200);
      btns[2] = new MenuButton("./assets/commanders/money01.png", 550, 20);
      btns[3] = new MenuButton("./assets/commanders/repair01.png", 550, 200);
      btns[4] = new MenuButton("./assets/commanders/basic01.png", 800, 20);
      btns[5] = new MenuButton("./assets/commanders/ranged01.png", 800, 200);
      
      for (int i = 0; i < btns.length; i++) {
        final int target = i;
        btns[i].addActionListener(controller.select);
        btns[i].addActionListener(e -> focus = target);
        add(btns[i]);
      }
      
      startGame = new MenuButton("STARTGAME","./assets/button/startGame.png", 350, 450);
      startGame.addActionListener(controller.play);
      add(startGame);
      
    }

    public int getFocus () {
      return focus;
    }
    
    protected void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);
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

    public PlayersPanel () throws IOException {
      setLayout(null);
      setPreferredSize(new Dimension(MainFrame.WIDTH/2, MainFrame.HEIGHT));
      
	  setOpaque(false);
	  setBorder(null);
      
      width = 160;
      height = 160;
      selected = new int[4];
      btns = new JButton[4];

      for (int i = 0; i < btns.length; i++) {
        selected[i] = -1;
        btns[i] = new MenuButton("","./assets/button/border01.png",0,0);
        final int target = i;
        btns[i].addActionListener(e -> focus = target);
        btns[i].setVerticalAlignment(SwingConstants.TOP);
        btns[i].setBounds(0, 0+150*i, 165, 165);
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

  public final CommandersPanel commanders;
  public final PlayersPanel players;

  public CreateView (CreateController controller) throws IOException {
    super(controller);

    this.controller = controller;
    setLayout(new BorderLayout());

    commanders = new CommandersPanel();
    players    = new PlayersPanel();

    JPanel select = new JPanel(new BorderLayout());
    players.setBounds(50, 20, 200,800);
    commanders.add(players);
    select.add(commanders);
    add(select, BorderLayout.CENTER);
  }
}
