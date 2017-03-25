package Main;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class LoginController {

    @FXML private PasswordField passField;
    @FXML private Label invalidLabel1;
    @FXML private Label invalidLabel2;
    @FXML private Button signInBtn;
    @FXML private TextField userNameField;

    //New account window
    @FXML private TextField createName;
    @FXML private PasswordField createPass;
    @FXML private PasswordField repeatPass;
    @FXML private Button createBtn;
    @FXML private Label passShortLabel;
    @FXML private Label passDontMatchLabel;
    @FXML private Label userNameTooShort;
    //New account window


    private Stage newFileWindow;
    private Scene createPanel;
    private Stage MainWindow;
    private File  file;

    @FXML
    public void initialize(){
        if(Main.fileManager != null) {
            File selectedFile = Main.fileManager.getDbFile();
            this.file = selectedFile;
            String file = Main.fileManager.getDbFile().toString();
            String name = file.substring(file.lastIndexOf(System.getProperty("file.separator")) + 1, file.indexOf(".db"));
            userNameField.setText(name);
            userNameField.setFocusTraversable(false);
            passField.requestFocus();


        }

    }


    @FXML
    private void LogInPressed(){
        File possibleFile = new File(System.getProperty("user.dir") + File.separator + userNameField.getText() + ".db");
        
        if(possibleFile.exists() && Main.fileManager.tryOpen(possibleFile, toByte(passField.getText()))){
            //Close Login Window
            Stage stage = (Stage) signInBtn.getScene().getWindow();
            stage.close();
            //Main.fileManager.populateTable();

            //Open Main Window
            this.MainWindow = new Stage();
            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
                Scene mainScene = new Scene(pane);
                MainWindow.setTitle("Password Manager");
                MainWindow.setScene(mainScene);
                //Save on-Close
                MainWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        Main.fileManager.save();
                    }
                });
                MainWindow.showAndWait();
            } catch(IOException e){
                e.printStackTrace();
            }

        }else{ // file doesn't exist or password is incorrect
            userNameField.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            passField.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            invalidLabel1.setVisible(true);
            invalidLabel2.setVisible(true);
            passField.requestFocus();
            passField.selectAll();
        }

    }


    private void setTitleToFileName() {
        if (file != null) {
            Stage s = (Stage) signInBtn.getScene().getWindow();
            String fileName = file.toString().substring(file.toString().lastIndexOf("/") + 1);
            s.setTitle(fileName);
        }
    }

    @FXML
    private void registerPressed(){
        this.newFileWindow = new Stage();
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("CreateDB.fxml"));
            createPanel = new Scene(pane);

            newFileWindow.setTitle("Register an account");
            newFileWindow.initModality(Modality.APPLICATION_MODAL);
            newFileWindow.setScene(createPanel);
            newFileWindow.showAndWait();

        } catch (Exception e) {System.err.println(e);
            e.printStackTrace();}


    }


    @FXML
    private void createBtnPressed(){
        //Resetting all warnings
        passShortLabel.setVisible(false);
        passShortLabel.setBorder(null);
        passDontMatchLabel.setVisible(false);
        passDontMatchLabel.setBorder(null);
        userNameTooShort.setVisible(false);
        userNameTooShort.setBorder(null);

        createName.setBorder(null);
        createPass.setBorder(null);
        repeatPass.setBorder(null);
        createPass.setBorder(null);


        if(createName.getText().length() < 5){
            userNameTooShort.setVisible(true);
            //userNameTooShort.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            createName.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        if(createPass.getText().length() < 5){
            createPass.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            passShortLabel.setVisible(true);
            //passShortLabel.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        if(!createPass.getText().equals(repeatPass.getText())){
            passDontMatchLabel.setVisible(true);
            //passDontMatchLabel.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            createPass.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            repeatPass.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        }
        if(createName.getText().length() >= 5 && createPass.getText().length() >= 5 && createPass.getText().equals(repeatPass.getText())) {
            Main.fileManager.createNewDB(createName.getText(), toByte(createPass.getText()), System.getProperty("user.dir"));
            Stage stage = (Stage) createBtn.getScene().getWindow();
            stage.close();
        }

    }



    @FXML
    private void setSignInBtn(){
        if(passField.getText().length() > 5 && userNameField.getText().length() > 4){
            signInBtn.setDisable(false);
        } else {
            signInBtn.setDisable(true);
        }
    }


    private byte[] toByte(String s){
        byte[] ar;
        ar = s.getBytes();
        return ar;
    }

}