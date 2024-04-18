package l3.tpjeudelavie.Cellule;

import l3.tpjeudelavie.Visiteur.Visiteur;

public interface CelluleEtat{
    CelluleEtat vit();
    CelluleEtat meurt();
    boolean estVivante();

    public void accepte(Visiteur visiteur, Cellule cellule);
}