package l3.tpjeudelavie.Visiteur;

import l3.tpjeudelavie.Cellule.Cellule;
import l3.tpjeudelavie.JeuDeLaVie;

public abstract class Visiteur {
    
    protected JeuDeLaVie jeu;

    public Visiteur(JeuDeLaVie jeu){
        this.jeu = jeu;
    }

    public abstract void visiteCelluleVivante(Cellule cellule);

    public abstract void visiteCelluleMorte(Cellule cellule);
}
