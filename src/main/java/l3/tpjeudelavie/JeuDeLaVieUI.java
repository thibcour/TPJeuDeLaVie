package l3.tpjeudelavie;

import l3.tpjeudelavie.Observateur.Observateur;
import l3.tpjeudelavie.Observateur.ObservateurStats;
import l3.tpjeudelavie.Visiteur.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JeuDeLaVieUI extends JPanel implements Observateur {
    private final JeuDeLaVie jeu;
    private final JLabel infoLabel;
    private final ObservateurStats stats;
    private final JComboBox<String> visitorSelector;
    private final JLabel densityLabel;
    private final JLabel speedDisplayLabel;
    public JeuDeLaVieUI(JeuDeLaVie jeu){
        this.jeu = jeu;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.GRAY);
        infoLabel = new JLabel(); // Initialize infoLabel here



        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(infoLabel);
        try {
            InputStream is = getClass().getResourceAsStream("/Font/Miology.otf");
            if (is == null) {
                throw new FileNotFoundException("Le fichier de police n'a pas été trouvé");
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            infoLabel.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Erreur de chargement de la police", e);
        }
        this.add(infoPanel, BorderLayout.NORTH);

        stats = new ObservateurStats(jeu);
        jeu.attacheObservateur(stats);

        JPanel buttonPanel = new JPanel();

        visitorSelector = new JComboBox<>(new String[]{"VisiteurClassique", "VisiteurDayNight", "VisiteurHighLight"});
        visitorSelector.addActionListener(e -> {
            String selectedVisitor = (String) visitorSelector.getSelectedItem();
            switch (Objects.requireNonNull(selectedVisitor)) {
                case "VisiteurClassique":
                    jeu.setVisiteur(new VisiteurClassique(jeu));
                    break;
                case "VisiteurDayNight":
                    jeu.setVisiteur(new VisiteurDayNight(jeu));
                    break;
                case "VisiteurHighLife":
                    jeu.setVisiteur(new VisiteurHighLife(jeu));
                    break;
            }
        });
        buttonPanel.add(visitorSelector);

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            if (jeu.running) {
                jeu.pause();
                pauseButton.setText("Reprendre");
            } else {
                jeu.start();
                pauseButton.setText("Pause");
            }
            repaint();
        });
        buttonPanel.add(pauseButton);

        JButton nextGenButton = new JButton("Avancer d’une génération");
        nextGenButton.addActionListener(e -> {
            if (!jeu.running) {
                jeu.calculerGenerationSuivante();
                repaint();
            }
        });
        buttonPanel.add(nextGenButton);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            jeu.restart();
            stats.reset();
            pauseButton.setText("Pause"); // Add this line to change the text of pauseButton to "Pause" when the game is restarted
            repaint();
        });
        buttonPanel.add(restartButton);

        JLabel speedLabel = new JLabel();
        buttonPanel.add(speedLabel);

        speedDisplayLabel = new JLabel(); // Initialize speedDisplayLabel here

        JButton increaseSpeedButton = new JButton("Vitesse +");
        increaseSpeedButton.addActionListener(e -> {
            int currentDelay = jeu.getDelay();
            jeu.setDelay(Math.max(10, currentDelay - 10)); // Decrease delay to increase speed, with a minimum of 10ms
            jeu.restartExecutor(); // Restart executor here
            speedDisplayLabel.setText("Delay : " + jeu.getDelay()); // Update speedDisplayLabel here
            repaint();
        });
        buttonPanel.add(increaseSpeedButton);
        buttonPanel.add(speedDisplayLabel);
        speedDisplayLabel.setText("Delay : " + jeu.getDelay());
        JButton decreaseSpeedButton = new JButton("Vitesse -");
        decreaseSpeedButton.addActionListener(e -> {
            int currentDelay = jeu.getDelay();
            jeu.setDelay(currentDelay + 10); // Increase delay to decrease speed
            jeu.restartExecutor(); // Restart executor here
            speedDisplayLabel.setText("Delay : " + jeu.getDelay()); // Update speedDisplayLabel here
            repaint();
        });
        buttonPanel.add(decreaseSpeedButton);

        densityLabel = new JLabel();
        buttonPanel.add(densityLabel);

        JSlider densitySlider = new JSlider(0, 50, 25);
        AtomicReference<Double> density = new AtomicReference<>(densitySlider.getValue() / 100.0);
        densityLabel.setText("Densité : " + density);
        densitySlider.addChangeListener(e -> {
             density.set(densitySlider.getValue() / 100.0);
            jeu.setDensity(density.get());
            jeu.restart();
            densityLabel.setText("Densité : " + density); // mise à jour de la valeur de la densité
            repaint();
        });
        buttonPanel.add(densitySlider);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actualise(){
        infoLabel.setText("Generation n°" + stats.num_generation + "   Nombre de cellules vivantes : " + stats.compterCellulesVivantes());
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int startX = (this.getWidth() - jeu.getXMax() * 10) / 2;
        int startY = (this.getHeight() - jeu.getYMax() * 10) / 2;
        for (int i = 0; i < jeu.getXMax(); i++) {
            for (int j = 0; j < jeu.getYMax(); j++) {
                if(jeu.getGrilleXY(i, j).estVivante()){
                    g.setColor(Color.pink); // Set color to green
                    g.fillRoundRect(startX + i*10, startY + j*10, 10, 10, 5, 5); // Draw rounded rectangle
                }
            }
        }
    }
}