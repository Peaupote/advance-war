package fr.main.view.views;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

import fr.main.view.MainFrame;
import fr.main.view.controllers.GameController;
import fr.main.view.controllers.OptionController;


/**
 * Rendering main menu
 */
@SuppressWarnings("serial")
public class OptionView extends View {

    private final ControllPanel[] panels;
    private Image bkg;

    public OptionView(OptionController controller){
        super(controller);

        bkg = null;
        try {
          bkg = ImageIO.read(new File("./assets/screens/aaa.jpg"));
        } catch (IOException e) {
          System.err.println(e);
        }

        // for each control, create a panel to display control's name & associated keys
        GameController.Controls[] controls = GameController.Controls.values();
        panels = new ControllPanel[controls.length];
        this.setLayout(new GridLayout(controls.length + 1,1));

        for (int i = 0; i < controls.length; i++){
            panels[i] = new ControllPanel(controls[i]);
            add(panels[i]);
        }

        JPanel p = new JPanel();
        JButton c;

        p.setOpaque(false);

        // Quit button
        c = new JButton("Quit");
        c.addActionListener(controller.quit);
        p.add(c);

        // Cancel button (to go back to saved configuration)
        c = new JButton("Cancel");
        c.addActionListener(e -> {
            for (ControllPanel co : panels){
                co.reset();
            }
        });
        p.add(c);

        // Save button (to save the current configuration)
        c = new JButton("Save");
        c.addActionListener(e -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(GameController.Controls.parameters))) {
                bw.write("");
                for (ControllPanel cp : panels){
                    bw.append(cp.control.toString() + " ");
                    for (Integer i : cp.commands)
                        bw.append(i + " ");
                    bw.append("\n");
                }
            }catch(IOException ioException){
                System.out.println(ioException);
                System.out.println("Error when saving parameters");
            }finally{
                GameController.Controls.updateAll();
            }
        });
        p.add(c);

        // Default button (to go back to default settings)
        c = new JButton("Default");
        c.addActionListener(e -> {
            GameController.Controls.defaultValues();
            for (ControllPanel co :  panels)
                co.reset();
        });
        p.add(c);

        add(p);
    }

    @Override
    public void paintComponent (Graphics g) {
      super.paintComponent(g);

      if (bkg != null)
        g.drawImage(bkg, 0, 0, MainFrame.width(), MainFrame.height(), null);
    }

    /**
     * Class used to get a key pressed, used to add a control
     */
    @SuppressWarnings("serial")
    class KeyDialog extends JDialog {

        public KeyDialog(final Consumer<KeyEvent> r){
            super(MainFrame.instance, "Enter a key");
            pack();
            setLocationRelativeTo(null);
            setVisible(true);

            addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    r.accept(e);
                    setVisible(false);
                }
            });
        }
    }

    /**
     * A control panel is a panel associated to a control in which there is the name of the control, an "add" button and the list of existing keys associated to the control
     */
    @SuppressWarnings("serial")
    class ControllPanel extends JPanel{

        public final GameController.Controls control;
        private final LinkedList<Integer> commands;
        private final JLabel label;
        private final JButton add;

        public ControllPanel(GameController.Controls control){
            this.control = control;
            commands = new LinkedList<Integer>();

            label = new JLabel(control.toString() + " : ");
            add = new JButton("+");
            add.addActionListener(act -> {
                new KeyDialog(e -> {
                    if (! commands.contains(e.getKeyCode())){
                        this.add(new CustomButton(e.getKeyCode()));
                        revalidate();
                        repaint();
                    }
                });
            });

            setOpaque(false);

            reset();
        }

        public void reset(){
            removeAll();
            commands.clear();

            add(label);
            add(add);

            for (final int i : control.keys())
                this.add(new CustomButton(i));

            revalidate();
            repaint();
        }

        public LinkedList<Integer> getCommandList(){
            return commands;
        }

        @SuppressWarnings("serial")
        class CustomButton extends JButton {

            public CustomButton(int i){
                super(KeyEvent.getKeyText(i));
                ControllPanel.this.commands.add((Integer)i);
                addActionListener(a -> {
                    ControllPanel.this.remove(ControllPanel.this.getComponentZOrder(this));
                    ControllPanel.this.commands.remove((Integer)i);
                    ControllPanel.this.revalidate();
                    ControllPanel.this.repaint();
                });
                setVisible(true);
            }
        }
    }
}