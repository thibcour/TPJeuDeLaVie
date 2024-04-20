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

public class JeuDeLaVieUI extends BorderPane implements Observateur {
    private final JeuDeLaVie jeu;
    private final Label infoLabel;
    private final ObservateurStats stats;
    private final ComboBox<String> visitorSelector;
    private final Canvas canvas;
    private final game gameController;

    public JeuDeLaVieUI(JeuDeLaVie jeu, Canvas canvas, game gameController){ // Modify this line
        this.jeu = jeu;
        this.canvas = canvas;
        this.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        this.gameController = gameController;
        infoLabel = new Label(); // Initialize infoLabel here

        VBox infoPanel = new VBox();
        infoPanel.getChildren().add(infoLabel);
        this.setTop(infoPanel);

        stats = new ObservateurStats(jeu, gameController);
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


    public void draw(){
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            double cellSize = Math.min(canvas.getWidth() / jeu.getXMax(), canvas.getHeight() / jeu.getYMax()); // Calculate cell size
            for (int i = 0; i < jeu.getXMax(); i++) {
                for (int j = 0; j < jeu.getYMax(); j++) {
                    if(jeu.getGrilleXY(i, j).estVivante()){
                        gc.setFill(Color.PINK);
                        gc.fillArc(i * cellSize, j * cellSize, cellSize, cellSize, 0, 360, ArcType.ROUND);
                    }
                }
            }
        });
    }
}