package l3.tpjeudelavie.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import l3.tpjeudelavie.AppContext;
import l3.tpjeudelavie.JeuDeLaVie;
import l3.tpjeudelavie.JeuDeLaVieUI;
import l3.tpjeudelavie.Observateur.ObservateurStats;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class game {
    public Pane canvasContainer;
    private Point lastDragPoint;
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

    public Button resetZoomButton;

    public JeuDeLaVie jeu;

    private JeuDeLaVieUI jeuUI;
    @FXML
    private BorderPane borderPane;
    public void updateLabels() {
        Platform.runLater(() -> {
            generationLabel.setText("Generation n°" + ObservateurStats.num_generation);
            cellCountLabel.setText("Nombre de cellules vivantes : " + ObservateurStats.compterCellulesVivantes());
        });
    }

    public void initialize() {
        System.out.println("Initialisation du contrôleur");
        gameCanvas.widthProperty().bind(borderPane.widthProperty());
        gameCanvas.heightProperty().bind(borderPane.heightProperty());
        this.jeu = AppContext.getJeuDeLaVie();
        jeuUI = new JeuDeLaVieUI(jeu, gameCanvas, this);
        ObservateurStats stats = new ObservateurStats(jeu, this);
        jeu.attacheObservateur(stats);


        Runnable gameUpdateTask = () -> {
            jeu.calculerGenerationSuivante();
            jeuUI.draw();
        };

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(gameUpdateTask, 0, 70, TimeUnit.MILLISECONDS);

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (executorService != null) {
                executorService.shutdownNow();
            }
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(gameUpdateTask, 0, newValue.longValue(), TimeUnit.MILLISECONDS);
            speedLabel.setText("Vitesse : " + newValue.intValue() + " ms");
        });

        gameCanvas.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                jeuUI.setZoomFactor(jeuUI.getZoomFactor() * 1.1);
            } else {
                jeuUI.setZoomFactor(jeuUI.getZoomFactor() / 1.1);
            }
        });

        gameCanvas.setOnMousePressed(event -> lastDragPoint = new Point((int) event.getX(), (int) event.getY()));

        gameCanvas.setOnMouseDragged(event -> {
            int deltaX = (int) event.getX() - lastDragPoint.x;
            int deltaY = (int) event.getY() - lastDragPoint.y;

            Point zoomPoint = jeuUI.getZoomPoint();
            zoomPoint.translate(-deltaX, -deltaY);

            int minX = (int) (jeu.getXMax() * 0.5 * (1 - 1 / jeuUI.getZoomFactor()));
            int maxX = (int) (jeu.getXMax() * 0.5 * (1 + 1 / jeuUI.getZoomFactor()));
            int minY = (int) (jeu.getYMax() * 0.5 * (1 - 1 / jeuUI.getZoomFactor()));
            int maxY = (int) (jeu.getYMax() * 0.5 * (1 + 1 / jeuUI.getZoomFactor()));

            if (zoomPoint.x < minX) zoomPoint.x = minX;
            if (zoomPoint.x > maxX) zoomPoint.x = maxX;
            if (zoomPoint.y < minY) zoomPoint.y = minY;
            if (zoomPoint.y > maxY) zoomPoint.y = maxY;

            jeuUI.setZoomPoint(zoomPoint);

            lastDragPoint = new Point((int) event.getX(), (int) event.getY());
        });

        pauseButton.setOnAction(this::handlePauseButtonAction);
        resetButton.setOnAction(this::handleResetButtonAction);

    }

    public void handlePauseButtonAction(ActionEvent actionEvent) {
        if (pauseButton.getText().equals("Pause")) {
            executorService.shutdownNow();
            pauseButton.setText("Reprendre");
        } else {
            Runnable gameUpdateTask = () -> {
                jeu.calculerGenerationSuivante();
                jeuUI.draw();
            };
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(gameUpdateTask, 0, jeu.getDelay(), TimeUnit.MILLISECONDS);
            pauseButton.setText("Pause");
        }
    }

    public void handleResetButtonAction(ActionEvent actionEvent) {
        String currentMode = AppContext.getJeuDeLaVie().getMode(); // Ajoutez cette ligne pour obtenir le mode actuel
        switch (currentMode) {
            case  "Puffeur":
                jeu.initializeGrilleWithPuffer();
                break;
            case "Gosper Glider Gun":
                jeu.initializeGrilleWithCanons();
                break;
            case "Pulsar":
                jeu.initializeGrilleWithPulsar();
                break;
            case "Mode Libre":
                jeu.initializeGrilleModeLibre();
                break;
            default:
                jeu.initializeGrille();
                break;
        }
        jeuUI.draw();
        ObservateurStats.reset();
        updateLabels();
    }

    public void handleQuitButtonAction(ActionEvent actionEvent) {
        executorService.shutdownNow();
        Platform.exit();
    }

    public void handleResetZoomButtonAction(ActionEvent actionEvent) {
        jeuUI.setZoomFactor(1.0);
        jeuUI.setZoomPoint(new Point(jeu.getXMax() / 2, jeu.getYMax() / 2));
    }

    public void ButtonActionRetour(ActionEvent actionEvent) {
        handlePauseButtonAction(actionEvent); // Ajoutez cette ligne pour mettre le jeu en pause
        try {
            // Charger le nouveau fichier FXML
            Parent gameModeRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/ModeDeJeu.fxml")));

            // Utiliser la scène principale et remplacer le root par le nouveau root
            AppContext.mainScene.setRoot(gameModeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}