package l3.tpjeudelavie.Cellule;

import l3.tpjeudelavie.Visiteur.Visiteur;

public class CelluleEtatMort implements CelluleEtat {

    protected static CelluleEtatMort celluleMorte;

    private CelluleEtatMort(){}

    public static CelluleEtatMort getInstance(){
        if(celluleMorte==null){
            celluleMorte = new CelluleEtatMort();
        }
        return celluleMorte; 
    }


    @Override
    public CelluleEtat vit(){
        return CelluleEtatVivant.getInstance();
    }

    @Override
    public CelluleEtat meurt(){
        return this;
    }

    @Override
    public boolean estVivante(){
        return false;
    }

    @Override
    public void accepte(Visiteur visiteur, Cellule cellule){
        visiteur.visiteCelluleMorte(cellule);
    }
}