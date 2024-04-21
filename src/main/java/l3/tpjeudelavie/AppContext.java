package l3.tpjeudelavie;

import javafx.scene.Scene;

public class AppContext {
    public static Scene mainScene;
    public static Scene previousScene;

    public static JeuDeLaVie jeuDeLaVie;

    public static JeuDeLaVie getJeuDeLaVie() {
        return jeuDeLaVie;
    }

    public static void setJeuDeLaVie(String mode) {
        jeuDeLaVie = new JeuDeLaVie(200, 200, mode);
    }
}