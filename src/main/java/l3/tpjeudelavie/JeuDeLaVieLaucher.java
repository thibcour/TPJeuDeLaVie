package l3.tpjeudelavie;

import l3.tpjeudelavie.Observateur.ObservateurStats;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class JeuDeLaVieLaucher {
    public static void main(String[] args){
        JeuDeLaVie vie = new JeuDeLaVie(85, 85);
        JeuDeLaVieGame vieGame = new JeuDeLaVieGame(vie);
        ObservateurStats stats = new ObservateurStats(vie);
        vie.attacheObservateur(vieGame);
        vie.attacheObservateur(stats);

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1200;
        config.height = 800;
        config.resizable = true;
        new LwjglApplication(vieGame, config);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(vie::calculerGenerationSuivante, 0, 70, TimeUnit.MILLISECONDS);

        vie.restartExecutor();
    }
}