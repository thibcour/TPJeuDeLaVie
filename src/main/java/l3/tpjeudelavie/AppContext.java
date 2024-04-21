package l3.tpjeudelavie;

import javafx.scene.Scene;

public class AppContext {
    public static Scene mainScene;
    public static Scene previousScene;

    public static JeuDeLaVie jeuDeLaVie = new JeuDeLaVie(50, 50);

    public static JeuDeLaVie getJeuDeLaVie() {
        return jeuDeLaVie;
    }

}