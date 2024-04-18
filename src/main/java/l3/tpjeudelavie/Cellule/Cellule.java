package l3.tpjeudelavie.Cellule;

import l3.tpjeudelavie.JeuDeLaVie;
import l3.tpjeudelavie.Visiteur.Visiteur;

public class Cellule{
    private CelluleEtat etat;
    private final int x;
    private final int y;

    public Cellule(int x, int y, CelluleEtat etat){
        this.x = x;
        this.y = y;
        this.etat = etat;
    }

    public void vit(){
        etat = etat.vit();
    }

   
    public void meurt(){
        etat = etat.meurt();
    }


    public boolean estVivante(){
        return etat.estVivante();
    }


    public int nombreVoisinesVivantes(JeuDeLaVie jeu){
        int nbVoisins = 0;
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if(i!=x || j!=y){
                    Cellule c = jeu.getGrilleXY(i, j);
                    if(c!=null && c.estVivante()){
                        nbVoisins++;
                    }
                }
            }
        }
        return nbVoisins;

    }


    public void accepte(Visiteur visiteur){
        etat.accepte(visiteur, this);
    }
}