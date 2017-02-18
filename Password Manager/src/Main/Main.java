
package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Main class for setting up and starting an application,
 * display login window
 *
 * Authors: Bohdan Yevdokymov
 */
public class Main extends Application {
    private Stage loginWindow;
    private Scene loginScene;
    public static FileManager fileManager;

    /**
     * Method that runs javaFX first login window
     * @param primaryStage initial window
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
        loginWindow = primaryStage;


        //pane.getChildren().add(dir);

        loginScene = new  Scene(pane);

        loginWindow.setTitle("Password Manager");
        loginWindow.setResizable(false);
        loginWindow.setScene(loginScene);
        loginWindow.show();
    }

    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        fileManager = new FileManager();
        launch(args);
    }
}
