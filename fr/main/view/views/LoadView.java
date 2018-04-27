package fr.main.view.views;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.Random;

import fr.main.view.controllers.LoadController;
import fr.main.view.MainFrame;

/**
 * View for the loading page
 */
@SuppressWarnings("serial")
public class LoadView extends View {

    private LoadController controller;
    @SuppressWarnings("unused")
    private static int width  = 350,
                       height = 50,
                       x      = (MainFrame.width() - width) / 2,
                       y      = (MainFrame.height() - height) / 2,
                       x2     = (MainFrame.width() - (width * 3 / 2)) / 2,
                       y2     = y + height + 50;

    private static String[] sentences = new String[]{
        "Connais ton adversaire, connais-toi, et tu ne mettras pas ta victoire en danger.<br>" + 
            "Connais le ciel et connais la terre, et ta victoire sera totale.",
        "L’art de la guerre, c’est de soumettre l’ennemi sans combat.",
        "Toute campagne guerrière doit être réglée sur le semblant ; feignez le désordre,<br>" + 
            "ne manquez jamais d’offrir un appât à l’ennemi pour le leurrer, <br>" + 
            "simulez l’infériorité pour encourager son arrogance, sachez attiser son courroux<br>" +
            "pour mieux le plonger dans la confusion : sa convoitise le lancera sur vous pour s’y briser.",
        "La guerre, c’est la guerre des hommes ; la paix, c’est la guerre des idées.",
        "Les Armes ont tu leurs ordres en attendant<br>" +
            "De vibrer à nouveau dans des mains admirables<br>" +
            "Ou scélérates, et, tristes, le bras pendant,<br>" + 
            "Nous allons, mal rêveurs, dans le vague des Fables.",
        "Mieux vaut une bonne guerre qu'une mauvaise paix.",
        "Qui lance une guerre en devient la poule",
        "Les chevaux de guerre naissent sur les frontières.",
        "Faites fondre le beurre à feu très doux,<br>" +
            "dans une casserole ou au bain-marie.<br>" +
            "Puis, laissez refroidir."
    };

    private static Font font = new Font ("Hevetica", Font.PLAIN, 16);

    private Image bkg;

    @SuppressWarnings("serial")
    private static class AdvicePanel extends JLabel {

        public AdvicePanel (Random rand) {
            super("<html><em style='text-align:center'>" + 
                   sentences[rand.nextInt(sentences.length)] +
                  "</em></html>");
            setFont(font);
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setBackground(Color.black);
            setForeground(Color.white);
            setOpaque(true);
        }
    }

    public LoadView(LoadController controller) {
        super(controller);
        this.controller = controller;
        bkg = null;
        Random rand = new Random();

        try {
            File[] screens = (new File("./assets/screens/")).listFiles();
            File screen = screens[rand.nextInt(screens.length)];
            bkg = ImageIO.read(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        setLayout(new BorderLayout());
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createEmptyBorder(0, x2, 100, x2));
        container.add(new AdvicePanel(rand));
        container.setOpaque(false);
        add(container, BorderLayout.SOUTH);
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setFont(font);
        g.drawImage(bkg, 0, 0, MainFrame.width(), MainFrame.height(), Color.black, null);

        g.setColor(Color.black);
        g.drawRect (x, y, width, height);
        g.setColor(Color.green);
        g.fillRect (x + 1, y + 1, controller.getLoad() * width / 100, height - 1);
    }
}
