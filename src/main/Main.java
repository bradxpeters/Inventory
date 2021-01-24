package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The type Main.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1400, 500));
        primaryStage.show();
    }

    /**
     * The entry point of application.
     *
     * FUTURE ENHANCEMENTS:
     * - Make the value editable straight from the table without having to open a modal.
     * - Add a drop down of popular brands and parts so the user can select from a list rather than typing.
     * - Save the state of the app to a database so it can be persistent.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
