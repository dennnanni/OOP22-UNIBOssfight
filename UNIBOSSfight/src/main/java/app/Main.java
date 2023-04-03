package app;

import app.game.Prova;
import javafx.application.Application;
import app.ui.MainMenu;

/**
 * Main class of the project.
 */
public final class Main {

    private Main() { }

    /**
     * Entry point of the application.
     * @param args ...
     */
    public static void main(final String[] args) {
        Application.launch(Prova.class, args);
        //MainMenu.run(args);

    }
}