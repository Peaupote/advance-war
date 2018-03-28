package fr.main.view.views;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.IOException;

import fr.main.model.Player;
import fr.main.model.IAPlayer;
import fr.main.model.commanders.*;
import fr.main.view.MainFrame;
import fr.main.view.render.sprites.*;
import fr.main.view.controllers.CreateController;

public class CreateView extends View {

  protected CreateController controller;

  public class CommandersPanel extends JPanel {

    private final JButton[] btns;
    private final int width, height;
    private int focus;

    public CommandersPanel () {
      btns = new JButton[6];
      width = 140;
      height = 156;

      setLayout(new GridLayout(2, 3, 10, 10));
      setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT / 2 + 100));
      setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      btns[0] = new JButton(new ImageIcon(
                        Sprite.get("./assets/commanders/contact.png")
                        .getImage(0, 0, width, height)));
      btns[1] = new JButton(new ImageIcon(
                        Sprite.get("./assets/commanders/destroy.png")
                        .getImage(0, 0, width, height)));
      btns[2] = new JButton(new ImageIcon(
                        Sprite.get("./assets/commanders/money.png")
                        .getImage(0, 0, width, height)));
      btns[3] = new JButton(new ImageIcon(
                        Sprite.get("./assets/commanders/repair.png")
                        .getImage(0, 0, width, height)));
      btns[4] = new JButton(new ImageIcon(
                        Sprite.get("./assets/commanders/basic.png")
                        .getImage(0, 0, width, height)));
      btns[5] = new JButton(new ImageIcon(
                        Sprite.get("./assets/commanders/ranged.png")
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

  public class PlayersPanel extends JPanel {

    private final int width, height;
    private int focus;
    private JButton[] btns;

    public PlayersPanel () {
      setLayout(new GridLayout(1, 4, 20, 50));
      setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT / 2 - 125));
      setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

      width = 60;
      height = 100;

      btns = new JButton[4];
      for (int i = 0; i < btns.length; i++) {
        btns[i] = new JButton("Player " + (i + 1));
        final int target = i;
        btns[i].addActionListener(e -> focus = target);
        btns[i].setVerticalAlignment(SwingConstants.TOP);
        add(btns[i]);
      }
    }

    public void updateCommander () {
      btns[focus].setIcon(new ImageIcon(
          ((ImageIcon)commanders.btns[commanders.focus]
          .getIcon()).getImage().getScaledInstance(70, 78, Image.SCALE_DEFAULT)));
      btns[focus].revalidate();
    }

    public Player[] getPlayers() {
      Player[] ps = new Player[4];
      ps[0] = new Player("Player 1");
      new RepairCommander(ps[0]);
      ps[1] = new IAPlayer();
      new RepairCommander(ps[1]);
      ps[2] = new IAPlayer();
      new RepairCommander(ps[2]);
      ps[3] = new IAPlayer();
      new RepairCommander(ps[3]);
      return ps;
    }

  }

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
