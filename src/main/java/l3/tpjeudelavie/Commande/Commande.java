package l3.tpjeudelavie.Commande;

import l3.tpjeudelavie.Cellule.Cellule;

public abstract class Commande {
    protected Cellule cellule;

    public abstract void executer();
}
