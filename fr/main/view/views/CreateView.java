package fr.main.view.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.main.model.commanders.BasicCommander;
import fr.main.model.commanders.ContactCommander;
import fr.main.model.commanders.DestroyCommander;
import fr.main.model.commanders.MoneyCommander;
import fr.main.model.commanders.RangedCommander;
import fr.main.model.commanders.RepairCommander;
import fr.main.model.players.AIPlayer;
import fr.main.model.players.Player;
import fr.main.view.MainFrame;
import fr.main.view.components.MenuButton;
import fr.main.view.controllers.CreateController;
import fr.main.view.render.sprites.Sprite;

/**
 * View of commanders selection
 */
@SuppressWarnings("serial")
public class CreateView extends View {

	protected CreateController controller;

    /**
     * Panels with all commanders
     */
    @SuppressWarnings("serial")
    public class CommandersPanel extends JPanel {

		private final JButton[] btns;
        private final JButton   startGame;
        @SuppressWarnings("unused")
		private final int width, height;

        /**
         * Index of which commander is selected
         */
        private int focus;
        private Image backgroundImage;

        public CommandersPanel () throws IOException {
            btns = new JButton[6];
            width = 140;
            height = 156;
            backgroundImage = new ImageIcon("./assets/screens/acc.jpg").getImage();
            
            setLayout(null);
            setPreferredSize(new Dimension(MainFrame.width(), MainFrame.height() / 2 + 100));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            btns[0] = new MenuButton(new ImageIcon(Sprite.get("./assets/commanders/contact.png").getImage(0,0,140,160)), 300, 20);
            btns[1] = new MenuButton(new ImageIcon(Sprite.get("./assets/commanders/destroy.png").getImage(0,0,140,160)), 300, 200);
            btns[2] = new MenuButton(new ImageIcon(Sprite.get("./assets/commanders/money.png")  .getImage(0,0,140,160)), 550, 20);
            btns[3] = new MenuButton(new ImageIcon(Sprite.get("./assets/commanders/repair.png") .getImage(0,0,140,160)), 550, 200);
            btns[4] = new MenuButton(new ImageIcon(Sprite.get("./assets/commanders/basic.png")  .getImage(0,0,140,160)), 800, 20);
            btns[5] = new MenuButton(new ImageIcon(Sprite.get("./assets/commanders/ranged.png") .getImage(0,0,140,160)), 800, 200);
            
            startGame = new MenuButton("CHOOSE MAP","./assets/button/startGame.png", 350, 450);

            for (int i = 0; i < btns.length; i++) {
                final int target = i;
                btns[i].addActionListener(controller.select);
                btns[i].addActionListener(e -> focus = target);
                add(btns[i]);
            }
            
            startGame.addActionListener(controller.play);
            startGame.setEnabled(false);
            add(startGame);
        }

        public int getFocus () {
            return focus;
        }
        
        protected void paintComponent(Graphics g) { 
            super.paintComponent(g);
            g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    /**
     * Panel of all players
     */
    @SuppressWarnings("serial")
    public class PlayersPanel extends JPanel {

		private final int width, height;

        /**
         * Index of focused player
         */
        private int focus;

        private final JButton[]   btns;
        private final JCheckBox[] checks;
        private final JTextField[] labels;

        /**
         * Representing the choosed commander
         * selected[i] = k ~ player i choosed commander k
         * -1 meaning no commander choosed (default)
         */
        private final int[] selected;

        public PlayersPanel () throws IOException {
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(MainFrame.width()/2, MainFrame.height() * 4 / 5));

            setOpaque(false);
            setBorder(null);

            GridBagConstraints gc = new GridBagConstraints();
            gc.ipady = gc.anchor = GridBagConstraints.CENTER;
            gc.weightx = 2;
            gc.weighty = 8;

            width = 150;
            height = 150;
            selected = new int[4];
            btns = new JButton[4];
            checks = new JCheckBox[4];
            labels = new JTextField[4];

            for (int i = 0; i < btns.length; i++) {
                gc.gridx = 0;
                gc.gridy = 2 * i;
                gc.gridheight = 2;

                selected[i] = -1;
                btns[i] = new JButton();
                btns[i].setPreferredSize(new Dimension(width, height));
                btns[i].setContentAreaFilled(false);
                btns[i].setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                final int target = i;
                btns[i].addActionListener(e -> {
                    btns[focus].setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                    focus = target;
                    btns[focus].setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                    selected[focus] = -1;
                    btns[focus].setIcon(null);

                    int nb = 0;
                    for (int n : selected) if (n != -1) nb ++;
                    CreateView.this.commanders.startGame.setEnabled(nb >= 2);
                });
                add(btns[i], gc);

                gc.gridheight = 1;
                gc.gridx = 1;
                labels[i] = new JTextField("Player " + (i+1));
                labels[i].setOpaque(true);
                labels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                labels[i].setBackground(new Color(100, 100, 100, 200));
                labels[i].setForeground(Color.WHITE);
                labels[i].setPreferredSize(new Dimension(70, 10));
                add(labels[i], gc);
                gc.gridy = 2 * i + 1;
                checks[i] = new JCheckBox("AI");
                checks[i].setContentAreaFilled(false);
                add(checks[i], gc);
            }
            btns[0].setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        }

        /**
         * Change focused player's commander
         */
        public void updateCommander () {
            btns[focus].setIcon(commanders.btns[commanders.focus].getIcon());
            selected[focus] = commanders.focus;
            btns[focus].revalidate();

            int nb = 0;
            for (int n : selected) if (n != -1) nb ++;
            CreateView.this.commanders.startGame.setEnabled(nb >= 2);
        }

        /**
         * @return list of all players in the game
         */
        public Player[] getPlayers() {
            Player.increment_id = 0;
            ArrayList<Player> ps = new ArrayList<>();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i] == -1) continue;
                Player p;
                if (checks[i].isSelected())
                    p = new AIPlayer(labels[i].getText());
                else
                    p = new Player("Player " + (i + 1)); 
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
        players.setBounds(50, 20, 250, 680);
        commanders.add(players);
        select.add(commanders);
        add(select, BorderLayout.CENTER);
    }
}
