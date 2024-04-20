package l3.tpjeudelavie.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import l3.tpjeudelavie.AppContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Objects;

@Controller

public class ModeDeJeu {
    @FXML
    public Button playButton;
    public Button buttonClassique;
    public Button buttonDayNight;
    public Button buttonHighLife;
    public Button buttonModeCanon;
    @FXML
    private Button RetourButton;

    public void handlePlay(ActionEvent actionEvent) {
        Button sourceButton = (Button) actionEvent.getSource();

        if (sourceButton == buttonClassique) {

        } else if (sourceButton == buttonDayNight) {
            // Code for the DayNight button
        } else if (sourceButton == buttonHighLife) {
            // Code for the HighLife button
        } else if (sourceButton == buttonModeCanon) {
            // Code for the ModeCanon button
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
