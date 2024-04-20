package l3.tpjeudelavie.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import l3.tpjeudelavie.JeuDeLaVie;
import l3.tpjeudelavie.JeuDeLaVieUI;
import l3.tpjeudelavie.Observateur.ObservateurStats;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class game {
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

    private JeuDeLaVie jeu;

    private JeuDeLaVieUI jeuUI;

    public void updateLabels() {
        Platform.runLater(() -> {
            generationLabel.setText("Generation n°" + ObservateurStats.num_generation);
            cellCountLabel.setText("Nombre de cellules vivantes : " + ObservateurStats.compterCellulesVivantes());
        });
    }

    public void initialize() {
        System.out.println("Initialisation du contrôleur");
        this.jeu = new JeuDeLaVie(200, 200);
        jeuUI = new JeuDeLaVieUI(jeu, gameCanvas, this);
        ObservateurStats stats = new ObservateurStats(jeu, this);
        jeu.attacheObservateur(stats);

        Runnable gameUpdateTask = new Runnable() {
            @Override
            public void run() {
                jeu.calculerGenerationSuivante();
                jeuUI.draw();
            }
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

        gameCanvas.setOnMousePressed(event -> {
            lastDragPoint = new Point((int) event.getX(), (int) event.getY());
        });

        gameCanvas.setOnMouseDragged(event -> {
            int deltaX = (int) event.getX() - lastDragPoint.x;
            int deltaY = (int) event.getY() - lastDragPoint.y;

            Point zoomPoint = jeuUI.getZoomPoint();
            zoomPoint.translate(-deltaX, -deltaY);

            // Limit the zoom point to the bounds of the game grid
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

    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void handlePauseButtonAction(ActionEvent actionEvent) {
        if (this.jeu.running) { // Access 'jeu' using 'this'
            this.jeu.pause();
            pauseButton.setText("Resume");
        } else {
            this.jeu.start();
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

    public void handleResetZoomButtonAction(ActionEvent actionEvent) {
        jeuUI.setZoomFactor(1.0);
        jeuUI.setZoomPoint(new Point(jeu.getXMax() / 2, jeu.getYMax() / 2));
    }
}