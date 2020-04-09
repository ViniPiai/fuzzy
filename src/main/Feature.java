package main;

import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Some feature to make funny.
 *
 * @author andre | vinicius
 */
public abstract class Feature {

    public static final String DEATH = "vai_morrer";
    public static final String LIVE = "vai_viver";
    public static final String AUDIO_PATH = "/audio/";
    public static final String IMAGE_PATH = "/image/";
    public static final String IMAGE_DEATH = Main.REPOSITORY_PATH + IMAGE_PATH + DEATH + ".png";
    public static final String IMAGE_LIVE = Main.REPOSITORY_PATH + IMAGE_PATH + LIVE + ".png";
    public static final String AUDIO_DEATH = Main.REPOSITORY_PATH + AUDIO_PATH + DEATH + ".wav";
    public static final String AUDIO_LIVE = Main.REPOSITORY_PATH + AUDIO_PATH + LIVE + ".wav";

    public static void execute(double tax) {
        ImageIcon icon = getImage(tax > 50.0);
        showImage(icon, tax);

    }

    private static void showImage(ImageIcon icon, double tax) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(icon);
        panel.add(getLabel(tax));
        panel.add(label);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JLabel getLabel(double tax) {
        JLabel temp = new JLabel(String.format("Score: %.2f", tax) + '%');
        temp.setFont(new Font("FreeMono", Font.PLAIN,38));
        return temp;
    }

    private static ImageIcon getImage(boolean live) {
        return live ? new ImageIcon(IMAGE_LIVE) : new ImageIcon(IMAGE_DEATH);
    }

}
