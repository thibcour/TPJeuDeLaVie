package l3.tpjeudelavie.Visiteur;

import l3.tpjeudelavie.Cellule.Cellule;
import l3.tpjeudelavie.Commande.CommandeMeurt;
import l3.tpjeudelavie.Commande.CommandeVit;
import l3.tpjeudelavie.JeuDeLaVie;

public class VisiteurDayNight extends Visiteur{
    public VisiteurDayNight(JeuDeLaVie jeu){
        super(jeu);
        System.out.println("--------------------------------VisiteurDayNight--------------------------------");
    }

    public void visiteCelluleVivante(Cellule cellule){
        int nbVoisins = cellule.nombreVoisinesVivantes(jeu);
        if(nbVoisins != 3 && nbVoisins != 4 && nbVoisins != 6 && nbVoisins != 7 && nbVoisins != 8){
            jeu.ajouteCommande(new CommandeMeurt(cellule));
        }
    }

    public void visiteCelluleMorte(Cellule cellule){
        int nbVoisins = cellule.nombreVoisinesVivantes(jeu);
        if(nbVoisins == 3 || nbVoisins == 6 || nbVoisins == 7 || nbVoisins == 8){
            jeu.ajouteCommande(new CommandeVit(cellule));
        }
    }
}