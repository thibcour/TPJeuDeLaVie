package l3.tpjeudelavie.Visiteur;

import l3.tpjeudelavie.Cellule.Cellule;
import l3.tpjeudelavie.Commande.CommandeMeurt;
import l3.tpjeudelavie.Commande.CommandeVit;
import l3.tpjeudelavie.JeuDeLaVie;

public class VisiteurCanon extends Visiteur{

    public VisiteurCanon(JeuDeLaVie jeu){
        super(jeu);
        System.out.println("--------------------------------VisiteurCanon--------------------------------");
    }

    public void visiteCelluleVivante(Cellule cellule){
        int nbVoisins = cellule.nombreVoisinesVivantes(jeu);
        if(nbVoisins < 2 || nbVoisins > 3){
            jeu.ajouteCommande(new CommandeMeurt(cellule));
        }
    }

    public void visiteCelluleMorte(Cellule cellule){
        int nbVoisins = cellule.nombreVoisinesVivantes(jeu);
        if(nbVoisins == 3){
            jeu.ajouteCommande(new CommandeVit(cellule));
        }
    }
}