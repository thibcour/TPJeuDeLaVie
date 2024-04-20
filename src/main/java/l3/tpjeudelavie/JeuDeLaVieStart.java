package l3.tpjeudelavie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class JeuDeLaVieStart extends Application {

    public JeuDeLaVieStart() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le nouveau fichier FXML
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/main.fxml")));

        // Initialize AppContext.mainScene before setting the root
        AppContext.mainScene = new Scene(root, 1200, 800);

        // Utiliser la scène principale et remplacer le root par le nouveau root
        AppContext.mainScene.setRoot(root);

        primaryStage.setTitle("Jeu de la vie");
        AppContext.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.setScene(AppContext.mainScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}