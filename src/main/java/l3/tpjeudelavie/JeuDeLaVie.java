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
    private final int delay = 70;
    private ScheduledExecutorService executor;


    public JeuDeLaVie(int xMax, int yMax, String mode){
        this.xMax = xMax;
        this.yMax = yMax;
        this.visiteur = new VisiteurClassique(this);
        observateurs = new ArrayList<>();
        commandes = new ArrayList<>();
        if (mode.equals("Gosper Glider Gun")) {
            initializeGrilleWithCanons();
        } else if (mode.equals("Pulsar")) {
            initializeGrilleWithPulsar();
        } if (mode.equals("Langton's Ant")) {
            initializeGrilleWithLangtonAnt();
        }if (mode.equals("Mode Libre")) {
            initializeGrilleModeLibre();
        }
        else {
            initializeGrille();
        }
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

    public void initializeGrilleModeLibre(){
        System.out.println("--------------------------------initializeGrilleModeLibre--------------------------------");
        this.grille = new Cellule[xMax][yMax];
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                this.grille[i][j] = new Cellule(i,j, CelluleEtatMort.getInstance());
            }
        }
    }

    public void initializeGrille(){
        this.grille = new Cellule[xMax][yMax];
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                double density = 0.5;
                if(Math.random() < density){
                    this.grille[i][j] = new Cellule(i,j, CelluleEtatVivant.getInstance());
                }
                else{
                    this.grille[i][j] = new Cellule(i,j, CelluleEtatMort.getInstance());
                }
            }
        }
    }

    public void initializeGrilleWithCanons(){
        System.out.println("--------------------------------Gosper Glider Gun--------------------------------");
        this.grille = new Cellule[xMax][yMax];
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                this.grille[i][j] = new Cellule(i,j, CelluleEtatMort.getInstance());
            }
        }

        // Configuration d'un Gosper Glider Gun
        int[][] gosperGliderGun = {
                {1, 5}, {1, 6}, {2, 5}, {2, 6}, {11, 5}, {11, 6}, {11, 7}, {12, 4}, {12, 8}, {13, 3}, {13, 9}, {14, 3}, {14, 9},
                {15, 6}, {16, 4}, {16, 8}, {17, 5}, {17, 6}, {17, 7}, {18, 6}, {21, 3}, {21, 4}, {21, 5}, {22, 3}, {22, 4}, {22, 5},
                {23, 2}, {23, 6}, {25, 1}, {25, 2}, {25, 6}, {25, 7}, {35, 3}, {35, 4}, {36, 3}, {36, 4}
        };

        for (int[] point : gosperGliderGun) {
            int x = point[0];
            int y = point[1];
            this.grille[x][y] = new Cellule(x, y, CelluleEtatVivant.getInstance());
        }
    }

    public void initializeGrilleWithPulsar(){
        System.out.println("--------------------------------Pulsar--------------------------------");
        this.grille = new Cellule[xMax][yMax];
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                this.grille[i][j] = new Cellule(i,j, CelluleEtatMort.getInstance());
            }
        }

        // Configuration d'un Pulsar
        int[][] pulsar = {
                {2,4}, {2,5}, {2,6}, {2,10}, {2,11}, {2,12},
                {4,2}, {4,7}, {4,9}, {4,14},
                {5,2}, {5,7}, {5,9}, {5,14},
                {6,2}, {6,7}, {6,9}, {6,14},
                {7,4}, {7,5}, {7,6}, {7,10}, {7,11}, {7,12},
                {9,4}, {9,5}, {9,6}, {9,10}, {9,11}, {9,12},
                {10,2}, {10,7}, {10,9}, {10,14},
                {11,2}, {11,7}, {11,9}, {11,14},
                {12,2}, {12,7}, {12,9}, {12,14},
                {14,4}, {14,5}, {14,6}, {14,10}, {14,11}, {14,12}
        };

        for (int[] point : pulsar) {
            int x = point[0];
            int y = point[1];
            this.grille[x][y] = new Cellule(x, y, CelluleEtatVivant.getInstance());
        }
    }

    public void initializeGrilleWithLangtonAnt(){
        System.out.println("--------------------------------Langton's Ant--------------------------------");
        this.grille = new Cellule[xMax][yMax];
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                this.grille[i][j] = new Cellule(i,j, CelluleEtatMort.getInstance());
            }
        }

        // Configuration d'une Fourmi de Langton
        int x = xMax / 2;
        int y = yMax / 2;
        this.grille[x][y] = new Cellule(x, y, CelluleEtatVivant.getInstance());
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