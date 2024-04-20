package l3.tpjeudelavie;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TpJeuDeLaVieApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpJeuDeLaVieApplication.class, args);
        Application.launch(JeuDeLaVieStart.class, args);
    }
}