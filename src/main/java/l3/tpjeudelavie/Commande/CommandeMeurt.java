package l3.tpjeudelavie.Commande;

import l3.tpjeudelavie.Cellule.Cellule;

public class CommandeMeurt extends Commande{
    
    public CommandeMeurt(Cellule cellule){
        super.cellule = cellule;
    }

    public void executer(){
        cellule.meurt();
    }
}
