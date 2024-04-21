package l3.tpjeudelavie.Visiteur;

import l3.tpjeudelavie.Cellule.Cellule;
import l3.tpjeudelavie.Commande.CommandeMeurt;
import l3.tpjeudelavie.Commande.CommandeVit;
import l3.tpjeudelavie.JeuDeLaVie;

public class VisiteurLife34 extends Visiteur {

    public VisiteurLife34(JeuDeLaVie jeu) {
        super(jeu);
    }

    @Override
    public void visiteCelluleVivante(Cellule cellule) {
        int nbVoisins = cellule.nombreVoisinesVivantes(jeu);
        if (nbVoisins < 3 || nbVoisins > 4) {
            jeu.ajouteCommande(new CommandeMeurt(cellule));
        }
    }

    @Override
    public void visiteCelluleMorte(Cellule cellule) {
        int nbVoisins = cellule.nombreVoisinesVivantes(jeu);
        if (nbVoisins == 3) {
            jeu.ajouteCommande(new CommandeVit(cellule));
        }
    }
}