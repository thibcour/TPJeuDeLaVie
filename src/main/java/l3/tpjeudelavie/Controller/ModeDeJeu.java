package l3.tpjeudelavie.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import l3.tpjeudelavie.AppContext;
import l3.tpjeudelavie.JeuDeLaVie;
import l3.tpjeudelavie.Visiteur.VisiteurCanon;
import org.springframework.stereotype.Controller;
import l3.tpjeudelavie.Visiteur.VisiteurDayNight;
import l3.tpjeudelavie.Visiteur.VisiteurHighLife;
import l3.tpjeudelavie.Visiteur.VisiteurClassique;
import java.io.IOException;
import java.util.Objects;

@Controller

public class ModeDeJeu {
    @FXML
    public Button playButton;
    public Button buttonModeCanon;
    public Button VisiteurClassique;
    public Button VisiteurDayNight;
    public Button VisiteurHighLife;
    public Button buttonModePulsar;
    public Button modeLibreButton;
    private boolean modeLibre = false;
    public void handlePlay(ActionEvent actionEvent) {
        Button sourceButton = (Button) actionEvent.getSource();
        String mode = "";
        modeLibre = false;
        if (sourceButton == VisiteurClassique) {
            mode = "Classique";
        } else if (sourceButton == VisiteurDayNight) {
            mode = "DayNight";
        } else if (sourceButton == VisiteurHighLife) {
            mode = "HighLife";
        } else if (sourceButton == buttonModeCanon) {
            mode = "Gosper Glider Gun";
        } else if (sourceButton == buttonModePulsar) {
            mode = "Pulsar";
        }if (sourceButton == modeLibreButton) {
            mode = "Mode Libre";
        }
        AppContext.setJeuDeLaVie(mode); // Initialize jeuDeLaVie before setting the visitor
        switch (mode) {
            case "DayNight" -> AppContext.getJeuDeLaVie().setVisiteur(new VisiteurDayNight(AppContext.getJeuDeLaVie()));
            case "HighLife" -> AppContext.getJeuDeLaVie().setVisiteur(new VisiteurHighLife(AppContext.getJeuDeLaVie()));
            case "Gosper Glider Gun" -> {
                AppContext.getJeuDeLaVie().initializeGrilleWithCanons();
                AppContext.getJeuDeLaVie().setVisiteur(new VisiteurCanon(AppContext.getJeuDeLaVie()));
            }
            case "Pulsar" -> {
                AppContext.getJeuDeLaVie().initializeGrilleWithPulsar();
                AppContext.getJeuDeLaVie().setVisiteur(new VisiteurClassique(AppContext.getJeuDeLaVie()));
            }
            case "Mode Libre" -> {
                modeLibre = true;
                AppContext.getJeuDeLaVie().initializeGrilleModeLibre();
                AppContext.getJeuDeLaVie().setVisiteur(new VisiteurClassique(AppContext.getJeuDeLaVie()));
            }
            default -> AppContext.getJeuDeLaVie().setVisiteur(new VisiteurClassique(AppContext.getJeuDeLaVie()));
        }
        try {
            Parent gameModeRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/game.fxml")));
            AppContext.mainScene.setRoot(gameModeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ButtonActionRetour(ActionEvent actionEvent) {
        try {
            // Charger le nouveau fichier FXML
            Parent gameModeRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/main.fxml")));

            // Utiliser la scène principale et remplacer le root par le nouveau root
            AppContext.mainScene.setRoot(gameModeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
