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

        Runnable gameUpdateTask = new Runnable() {
            @Override
            public void run() {
                jeu.calculerGenerationSuivante();
                jeuUI.draw();
            }
        };


        // Schedule the initial game update task
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(gameUpdateTask, 0, 70, TimeUnit.MILLISECONDS);

        // Add a listener to the slider value
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Stop the current game update task
            if (executorService != null) {
                executorService.shutdownNow();
            }

            // Schedule a new game update task with the updated delay
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(gameUpdateTask, 0, newValue.longValue(), TimeUnit.MILLISECONDS);

            // Update the speed label
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