package l3.tpjeudelavie.Cellule;

import l3.tpjeudelavie.Visiteur.Visiteur;

public class CelluleEtatVivant implements CelluleEtat {

    protected static CelluleEtatVivant celluleVivante;

    private CelluleEtatVivant(){}

    public static CelluleEtatVivant getInstance(){
        if(celluleVivante==null){
            celluleVivante = new CelluleEtatVivant();
        }
        return celluleVivante; 
    }

    @Override
    public CelluleEtat vit(){
        return this;
    }

    @Override
    public CelluleEtat meurt(){
        return CelluleEtatMort.getInstance();
    }

    @Override
    public boolean estVivante(){
        return true;
    }

    @Override
    public void accepte(Visiteur visiteur, Cellule cellule){
        visiteur.visiteCelluleVivante(cellule);
    }
}