package l3.tpjeudelavie;

import l3.tpjeudelavie.Observateur.ObservateurStats;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class JeuDeLaVieLaucher {

    public static void main(String[] args){
        JeuDeLaVie vie = new JeuDeLaVie(85, 85);
        JeuDeLaVieUI vieUI = new JeuDeLaVieUI(vie);
        ObservateurStats stats = new ObservateurStats(vie);
        vie.attacheObservateur(vieUI);
        vie.attacheObservateur(stats);

        JFrame frame = new JFrame("Jeu de la vie");
        frame.setSize(1200, 800);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(vieUI, BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        vieUI.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(vie::calculerGenerationSuivante, 0, 70, TimeUnit.MILLISECONDS);

        vie.restartExecutor();
    }
}