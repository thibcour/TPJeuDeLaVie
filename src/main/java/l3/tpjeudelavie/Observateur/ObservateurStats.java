package l3.tpjeudelavie.Observateur;

import l3.tpjeudelavie.JeuDeLaVie;

public class ObservateurStats implements Observateur{

    public int num_generation;
    private JeuDeLaVie jeu;
    
    public ObservateurStats(JeuDeLaVie jeu){
        this.jeu = jeu;
        num_generation = 0;
    }

    public int compterCellulesVivantes(){
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
        this.num_generation++;
    }

    @Override
    public void actualise(){
        incrementer_generation();

        System.out.println("Generation n°"+num_generation);
        System.out.println("Nombre de cellules vivantes : "+compterCellulesVivantes());
        
    }
    
}
