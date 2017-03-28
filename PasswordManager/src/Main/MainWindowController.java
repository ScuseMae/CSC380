package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by Bohdan on 2/22/17.
 */
public class MainWindowController {

    @FXML private TableView table;
    @FXML private FlowPane statusPane;
    @FXML private ProgressBar loadBar;
          private ObservableList<Account> data;
    @FXML private TableColumn titleColumn;
    @FXML private TableColumn userColumn;
    @FXML private TableColumn passColumn;
    @FXML private TableColumn urlColumn;
    @FXML private TableColumn noteColumn;
    @FXML private ListView<String> typeList;
    //Edit account window
    @FXML
    //Edit account window
          private Stage addWindow;
          private HashMap<String, Integer> listItems;

          public static Account accountToAdd;

          Timer timer = new Timer();
          
    //Constructor
    @FXML
    public void initialize() throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        table.setEditable(false);

        final ContextMenu contextMenu = new ContextMenu();
        /*contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                //System.out.println("showing");
            }
        });
        contextMenu.setOnShown(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                //System.out.println("shown");
            }
        });
        */

        MenuItem item1 = new MenuItem("Copy Username");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                copyUsernamePressed();
            }
        });
        MenuItem item2 = new MenuItem("Copy Password");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    copyPassPressed();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        MenuItem item3 = new MenuItem("Edit");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    if(table.getSelectionModel().getSelectedItem() != null) {
                        editRequested((Account) table.getSelectionModel().getSelectedItem());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        contextMenu.getItems().addAll(item1, item2, item3);

        table.setContextMenu(contextMenu);

        statusPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //Defining Table Data in an Observable List
        data  = FXCollections.observableArrayList();

        /*Account testAccount = new Account("gmail1","username1", "password1", "comment1", "type1","gmail.com1");
        Account testAccount2 = new Account("gmail2","username2", "password2", "comment2", "type","gmail.com2");
        data.add(testAccount);
        data.add(testAccount2);
        */
        populateTypeList();
        typeList.getSelectionModel().select(0);

    }
    private void editRequested(Account ac){
        this.addWindow = new Stage();
        try {
            AddAccountController.editAccount = ac;
            AnchorPane pane = FXMLLoader.load(getClass().getResource("AddAccountWindow.fxml"));
            Scene mainScene = new Scene(pane);
            addWindow.setTitle("Edit Account");
            addWindow.setScene(mainScene);
            addWindow.showAndWait();

            if(accountToAdd != null){
                Main.accountTable.remove(ac.getTitle());
                Main.accountTable.put(accountToAdd.getTitle(), accountToAdd);
                accountToAdd = null;
                populateTypeList();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        AddAccountController.editAccount = null;
    }

    private void populateTypeList(){
        listItems = new HashMap<>();
        data  = FXCollections.observableArrayList();

        listItems.put("All", 0);
        for(Account ac : Main.accountTable.values()){

            data.add(ac);
            listItems.put("All", listItems.get("All") + 1);
            if(ac.getType() != null && !ac.getType().equals("null") && !ac.getType().equals("")){
                if(listItems.containsKey(ac.getType())){
                    listItems.put(ac.getType(), listItems.get(ac.getType()) + 1);
                } else {
                    listItems.put(ac.getType(), 1);
                }
            }
        }
        ObservableList<String> list = FXCollections.observableArrayList ();
        for(String key : listItems.keySet()){
            list.add(key + " (" + listItems.get(key) + ")");
        }
        typeList.setItems(list);
        table.setItems(data);


    }

    @FXML
    private void addAccountPressed() {
        this.addWindow = new Stage();
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("AddAccountWindow.fxml"));
            Scene mainScene = new Scene(pane);
            addWindow.setTitle("Add Account");
            addWindow.setScene(mainScene);
            addWindow.showAndWait();

            if(accountToAdd != null){
                Main.accountTable.put(accountToAdd.getTitle(), accountToAdd);
                accountToAdd = null;
                populateTypeList();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void copyUsernamePressed(){
        Account selected = (Account)table.getSelectionModel().getSelectedItem();
        loadBar.setProgress(0.6);

        //copying username to clipboard
        if (selected != null)
        {
        	String copyUsername = selected.getUserName();
        	final Clipboard clipboard = Clipboard.getSystemClipboard();
        	final ClipboardContent content = new ClipboardContent();
        	content.putString(copyUsername);
        	clipboard.setContent(content);
        }
        else
        {
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("No User Name Selected");
        	alert.setHeaderText(null);
        	alert.setContentText("Sorry, No User Name Has Been Selected, Try Again.");
        	alert.showAndWait();
        }

    }

    @FXML
    private void copyPassPressed() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Account selected = (Account)table.getSelectionModel().getSelectedItem();

        //copying password to clipboard
        if (selected != null)
        {
        	String copyPassword = selected.getPassword();
        	Clipboard clipboard = Clipboard.getSystemClipboard();
        	ClipboardContent content = new ClipboardContent();
        	content.putString(copyPassword);
        	clipboard.setContent(content);
        	
			//clear clipboard after 30 seconds
            clipboardTimer();
        }
        else
        {
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("No Password Selected");
        	alert.setHeaderText(null);
        	alert.setContentText("Sorry, No Password Has Been Selected, Try Again.");
        	alert.showAndWait();
        }
    }
    
    private void clipboardTimer()
    {
    	Timer timer = new Timer();
    	TimerTask task = new TimerTask()
    	{
    		int secondsPassed = 0;
    		
    		public void run()
    		{
    			secondsPassed++;
    			System.out.println("Seconds Passed: " + secondsPassed);
    			
    			if (secondsPassed == 30)
    			{
    				timer.cancel();
    				clearClipboard();
    			}
    		}
    	};
    	
    	timer.scheduleAtFixedRate(task, 0, 1000);
    }
    
    private void clearClipboard()
    {
    	timer.purge();
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
    	java.awt.datatransfer.Clipboard clipboard = toolkit.getSystemClipboard();
    	StringSelection strClear = new StringSelection(" ");
    	clipboard.setContents(strClear, null);
    }

    @FXML
    private void saveFile(){
        Main.fileManager.save();
    }

    @FXML
    private void lockButtonPressed(){
    	clearClipboard();
        try {
            Main.fileManager.save();
            Main.fileManager.resetPassword();

            Stage stage = (Stage) table.getScene().getWindow();
            stage.close();

            Stage loginWindow = new Stage();
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));

            Scene loginScene = new Scene(pane);
            //When login screen is loaded back up - sets title to last used .db file
            loginWindow.addEventHandler(WindowEvent.WINDOW_SHOWING, new  EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent window)
                {
                    String file = Main.fileManager.getDbFile().toString();
                    String fileName = file.substring(file.lastIndexOf("/") + 1);
                    loginWindow.setTitle(fileName);
                }
            });

            loginWindow.setTitle("Password Manager");
            loginWindow.setResizable(false);
            loginWindow.setScene(loginScene);
            loginWindow.show();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    private void typeSwitched(){
        String selected = typeList.getSelectionModel().getSelectedItem();
        data  = FXCollections.observableArrayList();


        if(selected != null){
            for(Account ac : Main.accountTable.values()){
                if(selected.contains(ac.getType()) || selected.contains("All")){
                    data.add(ac);
                }
            }
        }
        table.setItems(data);
    }

    @FXML
    private void removeAccountPressed(){
        Account acToRemove = (Account)table.getSelectionModel().getSelectedItem();
        if(acToRemove != null) {
            Main.accountTable.remove(acToRemove.getTitle());
            populateTypeList();
        }
    }


}


