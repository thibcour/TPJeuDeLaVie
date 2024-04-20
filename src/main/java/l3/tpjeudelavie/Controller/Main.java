package l3.tpjeudelavie.Controller;

import javafx.application.Platform;
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
public class Main {
    @FXML
    private Button playButton;

    @FXML
    private Button quitButton;

    @FXML
    protected void handlePlayButtonAction(ActionEvent event) {
        System.out.println("Jouer a été cliqué");

        try {
            // Charger le nouveau fichier FXML
            Parent gameModeRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/ModeDeJeu.fxml")));

            // Utiliser la scène principale et remplacer le root par le nouveau root
            AppContext.mainScene.setRoot(gameModeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleQuitButtonAction(ActionEvent event) {
        System.out.println("Quitter a été cliqué");
        Platform.exit();
    }

    @FXML
    public void initialize() {
        System.out.println("Initialisation du contrôleur");
    }
}