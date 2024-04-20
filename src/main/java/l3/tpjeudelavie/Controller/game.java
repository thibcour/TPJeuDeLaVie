package l3.tpjeudelavie.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    public Button pauseButton;
    public Button resetButton;
    public Button quitButton;
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label generationLabel;

    @FXML
    private Label cellCountLabel;

    private ScheduledExecutorService executorService;

    @FXML
    private Slider speedSlider;

    @FXML
    private Label speedLabel;

    private JeuDeLaVie jeu;

    public void updateLabels() {
        Platform.runLater(() -> {
            generationLabel.setText("Generation n°" + ObservateurStats.num_generation);
            cellCountLabel.setText("Nombre de cellules vivantes : " + ObservateurStats.compterCellulesVivantes());
        });
    }

    public void initialize() {
        System.out.println("Initialisation du contrôleur");
        JeuDeLaVie jeu = new JeuDeLaVie(200, 200);
        JeuDeLaVieUI jeuUI = new JeuDeLaVieUI(jeu, gameCanvas, this);
        ObservateurStats stats = stats = new ObservateurStats(jeu, this);
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

        // Mettre à jour les labels avec les informations de génération et le nombre de cellules vivantes
        generationLabel.setText("Generation n°" + stats.num_generation);
        cellCountLabel.setText("Nombre de cellules vivantes : " + stats.compterCellulesVivantes());

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            jeu.setDelay(newValue.intValue());
            speedLabel.setText("Vitesse : " + newValue.intValue() + " ms");
        });

    }

    // Assurez-vous d'arrêter l'executorService lorsque vous n'en avez plus besoin
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void handlePauseButtonAction(ActionEvent actionEvent) {
        if (jeu.running) {
            jeu.pause();
            pauseButton.setText("Resume");
        } else {
            jeu.start();
            pauseButton.setText("Pause");
        }
    }

    public void handleResetButtonAction(ActionEvent actionEvent) {
        jeu.restart();
    }

    public void handleQuitButtonAction(ActionEvent actionEvent) {
        executorService.shutdownNow();
        Platform.exit();
    }
}