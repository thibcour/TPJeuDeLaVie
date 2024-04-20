package l3.tpjeudelavie;

import l3.tpjeudelavie.Cellule.Cellule;
import l3.tpjeudelavie.Cellule.CelluleEtatMort;
import l3.tpjeudelavie.Cellule.CelluleEtatVivant;
import l3.tpjeudelavie.Commande.Commande;
import l3.tpjeudelavie.Observateur.Observable;
import l3.tpjeudelavie.Observateur.Observateur;
import l3.tpjeudelavie.Visiteur.Visiteur;
import l3.tpjeudelavie.Visiteur.VisiteurClassique;

import java.util.List;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JeuDeLaVie implements Observable {
    public boolean running = true;
    private final List<Observateur> observateurs;
    private final List<Commande> commandes;
    private Visiteur visiteur;
    private Cellule[][] grille;
    private final int xMax;
    private final int yMax;
    private int delay = 70;
    private ScheduledExecutorService executor;
    private double density = 0.5;
    private ScheduledExecutorService executorService;

    public JeuDeLaVie(int xMax, int yMax){
        this.xMax = xMax;
        this.yMax = yMax;
        this.visiteur = new VisiteurClassique(this);
        observateurs = new ArrayList<>();
        commandes = new ArrayList<>();
        initializeGrille();
    }

    public void restartExecutor() {
        if (executor != null) {
            executor.shutdownNow();
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (running) {
                calculerGenerationSuivante();
            }
        }, 0, delay, TimeUnit.MILLISECONDS);
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setVisiteur(Visiteur visiteur){
        this.visiteur = visiteur;
    }

    public int getXMax(){
        return this.xMax;
    }

    public int getYMax(){
        return this.yMax;
    }

    public void initializeGrille(){
        this.grille = new Cellule[xMax][yMax];
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                if(Math.random() < this.density){
                    this.grille[i][j] = new Cellule(i,j, CelluleEtatVivant.getInstance());
                }
                else{
                    this.grille[i][j] = new Cellule(i,j, CelluleEtatMort.getInstance());
                }
            }
        }
    }


    public Cellule getGrilleXY(int x, int y){
        if(x < 0 || x >= this.xMax || y < 0 || y >= this.yMax) {
            return null;
        }
        return grille[x][y];
    }

    @Override
    public void attacheObservateur(Observateur o){
        if(!observateurs.contains(o)){
            observateurs.add(o);
        }
    }

    @Override
    public void detacheObservateur(Observateur o){
        observateurs.remove(o);
    }

    @Override
    public void notifieObservateurs(){
        for (Observateur o : observateurs) {
            o.actualise();
        }
    }

    public void ajouteCommande(Commande c){
        commandes.add(c);
    }

    public void executeCommandes(){
        for (Commande commande : commandes) {
            commande.executer();
        }
        commandes.clear();
    }

    public void distribuerVisiteur(){
        for (int i = 0; i < xMax; i++) {
            for (int j = 0; j < yMax; j++) {
                grille[i][j].accepte(visiteur);
            }
        }
    }

    public void calculerGenerationSuivante(){
        distribuerVisiteur();
        executeCommandes();
        notifieObservateurs();
    }

    public void start() {
        this.running = true;
        restartExecutor();
    }

    public void pause() {
        this.running = false;
        System.out.println("Pause");
    }

    public synchronized void restart() {
        this.running = false;
        initializeGrille();
        this.running = true;
    }
}