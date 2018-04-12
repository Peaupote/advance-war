package fr.main.view.views;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.GridLayout;
import java.awt.Component;

import fr.main.view.components.*;
import fr.main.view.controllers.OptionController;
import fr.main.view.MainFrame;
import fr.main.view.controllers.GameController;
import fr.main.view.controllers.MenuController;


/**
 * Rendering main menu
 */
public class OptionView extends View {
    /**
     * Add OptionView UID
     */
    private static final long serialVersionUID = 5275297581165400242L;

    private final ControllPanel[] panels;

    public OptionView(OptionController controller){
        super(controller);


        GameController.Controls[] controls = GameController.Controls.values();
        panels = new ControllPanel[controls.length];
        this.setLayout(new GridLayout(controls.length + 1,1));

        for (int i = 0; i < controls.length; i++){
            panels[i] = new ControllPanel(controls[i]);
            add(panels[i]);
        }

        JPanel p = new JPanel();
        JButton c;

        c = new JButton("Quit");
        c.addActionListener(controller.quit);
        p.add(c);

        c = new JButton("Cancel");
        c.addActionListener(e -> {
            for (ControllPanel co : panels){
                co.reset();
            }
        });
        p.add(c);

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

        c = new JButton("Default");
        c.addActionListener(e -> {
            GameController.Controls.defaultValues();
            for (ControllPanel co :  panels)
                co.reset();
        });
        p.add(c);

        add(p);
    }

    class KeyDialog extends JDialog {
        /**
         * Add KeyDialog UID
         */
        private static final long serialVersionUID = 5465407553192600784L;

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

    class ControllPanel extends JPanel{
        /**
         * Add Unit UID
         */
        private static final long serialVersionUID = -5901234581123466220L;

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

        class CustomButton extends JButton {
            /**
             * Add Unit UID
             */
            private static final long serialVersionUID = 1835667581197532450L;

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