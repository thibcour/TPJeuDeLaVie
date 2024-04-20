package l3.tpjeudelavie;

import javafx.application.Platform;
import javafx.scene.shape.ArcType;
import l3.tpjeudelavie.Controller.game;
import l3.tpjeudelavie.Observateur.Observateur;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import l3.tpjeudelavie.Observateur.ObservateurStats;
import l3.tpjeudelavie.Visiteur.*;
import java.awt.Point;

public class JeuDeLaVieUI extends BorderPane implements Observateur {
    private final JeuDeLaVie jeu;
    private final Label infoLabel;
    private final ComboBox<String> visitorSelector;
    private final Canvas canvas;
    private double zoomFactor = 1.0;
    private final double zoomMax = 2.0;
    private final double zoomMin = 0.5;
    private Point zoomPoint;

    public JeuDeLaVieUI(JeuDeLaVie jeu, Canvas canvas, game gameController){ // Modify this line
        this.jeu = jeu;
        this.canvas = canvas;
        this.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        infoLabel = new Label(); // Initialize infoLabel here
        zoomPoint = new Point(jeu.getXMax() / 2, jeu.getYMax() / 2);
        VBox infoPanel = new VBox();
        infoPanel.getChildren().add(infoLabel);
        this.setTop(infoPanel);

        ObservateurStats stats = new ObservateurStats(jeu, gameController);
        jeu.attacheObservateur(stats);

        HBox buttonPanel = new HBox();

        visitorSelector = new ComboBox<>();
        visitorSelector.getItems().addAll("VisiteurClassique", "VisiteurDayNight", "VisiteurHighLife");
        visitorSelector.setOnAction(e -> {
            String selectedVisitor = visitorSelector.getValue();
            switch (selectedVisitor) {
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
        buttonPanel.getChildren().add(visitorSelector);
        this.setBottom(buttonPanel);

        canvas = new Canvas();
        this.setCenter(canvas);
    }

    @Override
    public void actualise(){
        infoLabel.setText("Generation n°" + ObservateurStats.num_generation + "   Nombre de cellules vivantes : " + ObservateurStats.compterCellulesVivantes());
        draw();
    }


    public void setZoomFactor(double zoomFactor) {
        if (zoomFactor >= zoomMin && zoomFactor <= zoomMax) {
            this.zoomFactor = zoomFactor;
        }

        // If zooming out beyond a certain threshold, reset the zoom point to the center of the grid
        if (zoomFactor <= 1.0) {
            zoomPoint = new Point(jeu.getXMax() / 2, jeu.getYMax() / 2);
        }
    }

    public void setZoomPoint(Point zoomPoint) {
        this.zoomPoint = zoomPoint;
    }

    public double getZoomFactor() {
        return this.zoomFactor;
    }

    public Point getZoomPoint() {
        return this.zoomPoint;
    }


    public void draw(){
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            double cellSize = Math.min(canvas.getWidth() / jeu.getXMax(), canvas.getHeight() / jeu.getYMax()) * zoomFactor; // Prend en compte le facteur de zoom
            for (int i = 0; i < jeu.getXMax(); i++) {
                for (int j = 0; j < jeu.getYMax(); j++) {
                    if(jeu.getGrilleXY(i, j).estVivante()){
                        gc.setFill(Color.PINK);
                        // Prend en compte le point de zoom
                        double x = (i - zoomPoint.x) * cellSize + canvas.getWidth() / 2;
                        double y = (j - zoomPoint.y) * cellSize + canvas.getHeight() / 2;
                        gc.fillArc(x, y, cellSize, cellSize, 0, 360, ArcType.ROUND);
                    }
                }
            }
        });
    }
}