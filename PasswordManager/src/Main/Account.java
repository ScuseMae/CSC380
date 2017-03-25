package Main;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Bohdan on 2/21/17.
 */

public class Account implements Serializable {


    private String title;
    private String userName;
    private String password;
    private String maskedPassword;
    private String comment;
    private String type;
    private String URL;
    private long lastModified;

    /**
     * Contrunctor for new account
     * @param title
     * @param userName
     * @param password
     * @param comment
     * @param type
     * @param URL
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public Account(String title, String userName, String password, String comment, String type, String URL) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        this.title = title;
        this.userName = userName;
        this.password = Main.fileManager.encrypt(password);
        maskedPassword = maskPass(password);
        this.comment = comment;
        if(type == null){
            type = "Other";
        }
        this.type = type;
        this.URL = URL;
        this.lastModified = System.currentTimeMillis();
    }

    /**
     * Constructor for loading account
     * @param title
     * @param userName
     * @param password
     * @param comment
     * @param type
     * @param URL
     * @param lastModified
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public Account(String title, String userName, String password, String comment, String type, String URL, long lastModified) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        this(title, userName, password,comment, type, URL);
        this.lastModified = lastModified;
    }

    private String maskPass(String password) {
        String output = "";

        if(password.length() > 15) {
            for (int i = 0; i < 15; i++) {
                output += "*";
            }
        } else {
            for (int i = 0; i < password.length(); i++) {
                output += "*";
            }
        }
        return output;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        return Main.fileManager.decrypt(password);
    }

    public void setPassword(String password) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        this.password = Main.fileManager.encrypt(password);
        lastModified = System.currentTimeMillis();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        lastModified = System.currentTimeMillis();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        lastModified = System.currentTimeMillis();

    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
        lastModified = System.currentTimeMillis();

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        lastModified = System.currentTimeMillis();

    }

    public String getMaskedPassword() {
        return maskedPassword;
    }

    public void setMaskedPassword(String maskedPassword) {
        this.maskedPassword = maskedPassword;
    }
    public String getEncryptedPassword(){
        return this.password;
    }
    public long getLastModified(){
        return lastModified;
    }
}
