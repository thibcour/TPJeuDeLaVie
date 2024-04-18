package l3.tpjeudelavie.Commande;

import l3.tpjeudelavie.Cellule.Cellule;

public class CommandeVit extends Commande {
    
    public CommandeVit(Cellule cellule){
        super.cellule = cellule;
    }

    @Override
    public void executer(){
        cellule.vit();
    }
}
