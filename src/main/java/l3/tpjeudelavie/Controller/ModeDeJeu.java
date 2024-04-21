package l3.tpjeudelavie.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import l3.tpjeudelavie.AppContext;
import org.springframework.stereotype.Controller;
import l3.tpjeudelavie.Visiteur.VisiteurDayNight;
import l3.tpjeudelavie.Visiteur.VisiteurHighLife;
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
    @FXML
    private Button RetourButton;

    public void handlePlay(ActionEvent actionEvent) {
        Button sourceButton = (Button) actionEvent.getSource();

        if (sourceButton == VisiteurClassique) {
            // Code for the Classique button
        } else if (sourceButton == VisiteurDayNight) {
            // Code for the DayNight button
            AppContext.getJeuDeLaVie().setVisiteur(new VisiteurDayNight(AppContext.getJeuDeLaVie()));
        } else if (sourceButton == VisiteurHighLife) {
            AppContext.getJeuDeLaVie().setVisiteur(new VisiteurHighLife(AppContext.getJeuDeLaVie()));
        } else if (sourceButton == buttonModeCanon) {

        }
        try {
            // Charger le nouveau fichier FXML
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
