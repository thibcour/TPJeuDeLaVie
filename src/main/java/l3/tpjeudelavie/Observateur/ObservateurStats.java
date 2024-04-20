package l3.tpjeudelavie.Observateur;

import javafx.application.Platform;
import l3.tpjeudelavie.Controller.game;
import l3.tpjeudelavie.JeuDeLaVie;

public class ObservateurStats implements Observateur{

    public static int num_generation;
    private static JeuDeLaVie jeu;
    private game controller;

    public ObservateurStats(JeuDeLaVie jeu, game controller){
        this.jeu = jeu;
        this.controller = controller;
        num_generation = 0;
    }

    public static int compterCellulesVivantes(){
        int cpt=0;
        for (int i = 0; i < jeu.getXMax(); i++) {
            for (int j = 0; j < jeu.getYMax(); j++) {
                if(jeu.getGrilleXY(i, j).estVivante()) cpt++;
            }
        }
        return cpt;
    }
    public void reset() {
        num_generation = 0;
    }

    private void incrementer_generation(){
        num_generation++;
    }

    @Override
    public void actualise(){
        incrementer_generation();

        System.out.println("Generation n°"+num_generation);
        System.out.println("Nombre de cellules vivantes : "+compterCellulesVivantes());
        controller.updateLabels();
    }
    
}
