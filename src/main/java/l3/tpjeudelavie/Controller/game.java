package l3.tpjeudelavie.Controller;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import l3.tpjeudelavie.JeuDeLaVie;
import l3.tpjeudelavie.JeuDeLaVieUI;
import l3.tpjeudelavie.Observateur.ObservateurStats;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class game {
    @FXML
    private Canvas gameCanvas;

    private ScheduledExecutorService executorService;

    public void initialize() {
        System.out.println("Initialisation du contrôleur");
        JeuDeLaVie jeu = new JeuDeLaVie(100, 100);
        JeuDeLaVieUI jeuUI = new JeuDeLaVieUI(jeu, gameCanvas);
        ObservateurStats stats = new ObservateurStats(jeu);
        jeu.attacheObservateur(stats);

        // Créer un ScheduledExecutorService pour actualiser le jeu à intervalles réguliers
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            jeu.calculerGenerationSuivante();
            jeuUI.draw();
        }, 0, 70, TimeUnit.MILLISECONDS); // Actualiser le jeu toutes les 70 millisecondes

        // Ajouter un écouteur à la propriété onCloseRequest de la scène
        Platform.runLater(() -> {
            gameCanvas.getScene().getWindow().setOnCloseRequest(event -> {
                stop();
            });
        });
    }

    // Assurez-vous d'arrêter l'executorService lorsque vous n'en avez plus besoin
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}