package l3.tpjeudelavie.Observateur;

public interface Observable{
    void attacheObservateur(Observateur o);
    void detacheObservateur(Observateur o);
    void notifieObservateurs();
}