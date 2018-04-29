package fr.main.view.views;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

import fr.main.view.components.MenuButton;
import fr.main.view.controllers.MapController;
import fr.main.view.MainFrame;

@SuppressWarnings("serial")
public class MapView extends View {

  // TODO: change for tree
  public final JList<String> list;
  public final JButton start;
  public Image bkg;

  public MapView (MapController controller) {
    super (controller);

    try {
      bkg = ImageIO.read(new File("./assets/screens/701912.jpg"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    JPanel listPanel = new JPanel ();
    listPanel.setLayout(new BorderLayout());
    listPanel.add(new JLabel("Choose a map"), BorderLayout.NORTH);
    list = new JList<String>(controller.listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    list.addListSelectionListener(controller.list);
    list.setMinimumSize(new Dimension(200, 400));

    JScrollPane scrollPane = new JScrollPane(list);
    scrollPane.setPreferredSize(new Dimension(230, 300));
    listPanel.setOpaque(false);
    listPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100));
    listPanel.add(list, BorderLayout.CENTER);

    setLayout(new BorderLayout());
    add(listPanel, BorderLayout.CENTER);

    JPanel controlPanel = new JPanel ();
    start = new MenuButton("START GAME","./assets/button/startGame.png", 350, 450);
    start.setEnabled(false);
    start.addActionListener(controller.start);
    controlPanel.add(start);
    controlPanel.setOpaque(false);
    add(controlPanel, BorderLayout.SOUTH);
  }

  @Override
  public void paintComponent (Graphics g) {
    super.paintComponent(g);

    g.drawImage(bkg, 0, 0, MainFrame.width(), MainFrame.height(), null);
  }

}