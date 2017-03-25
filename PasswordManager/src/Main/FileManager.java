package Main;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * Class for managing db files, reading, writing, encrypting, decrypting
 *
 */
public class FileManager {

    private static String ALGO = "AES";
    private static int keyLength = 16;
    private File dbFile;
    private Key key;
    private String uniqueId;

    /**
     * Default constructor for file manager, key has to be checked
     * first using static tryOpen method
     * @param dbFile file with encrypted data
     * @param key correct encryption key for the file
     */
    public FileManager(File dbFile, Key key){
        this.dbFile = dbFile;
        this.key = key;
    }

    /**
     * Tries to decrypt given file with given password
     * if successful - initializes Main.fileManager object using given parameters
     *  and loads accounts into Main.accountTable after
     * @param file db encrypted file
     * @param pas byte array formatted password
     * @return
     */
    public static boolean tryOpen(File file, byte[] pas){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String control = br.readLine();
            br.close();

            byte[] password = formatPassword(pas);
            Key key = generateKey(password);

            //if this line doesn't throw an exception - password is correct
            tryDecrypt(key, control);

            //at this point we know that password is correct
            Main.fileManager = new FileManager(file, key);
            Main.fileManager.load();


            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates new DB file using path, file name and password passed in parameters
     * @param name name of db
     * @param pas unformatted password in byte array
     * @param path path to create file in (Same as application folder by default)
     * @return true if file was successfully created, false if error occurred
     */
    public static boolean createNewDB(String name, byte[] pas, String path){
        byte[] password =formatPassword(pas);
        Key key = generateKey(password);
        SecureRandom random = new SecureRandom();
        String uniqueId = new BigInteger(160, random).toString(32);

        try{
            File file = new File(path + "/" + name + ".db");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String encryptedUID = tryEncrypt(key, uniqueId);
            bw.write(encryptedUID);
            bw.close();
        }catch (Exception e ) {
            System.err.println(e);
        }

        return true;
    }

    /**
     * For AES 128 bit encryption 16 character password is needed
     * If password is less then 16 characters - this method repeats it
     * If password is more then 16 characters - password is cut at 16
     * @param pas password to format
     * @return formatted password
     */
    private static byte[] formatPassword(byte[] pas){
        byte[] formatedPassword = new byte[keyLength];
        int c = 0;
        for(int i = 0; i < keyLength; i ++)
        {
            if(pas.length > c){
                formatedPassword[i] = pas[c];
                c++;
            } else {
                c = 0;
            }
        }
        return formatedPassword;
    }

    /**
     * Public helper method for encryption, can only be used when
     * Main.fileManager was initialized, therefore has key initialized
     * @param data string to encrypt using existing key
     * @return encrypted String
     */
    public String encrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return tryEncrypt(this.key, data);
    }

    /**
     * Public helper method for decryption, can only be used when
     * Main.fileManager was initialized, therefore has key initialized
     * @param encryptedData string with encrypted data
     * @return decrypted String
     */
    public String decrypt(String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {

        return tryDecrypt(key, encryptedData);
    }

    /**
     * Unlike "decrypt" method, this method is static and can be used even
     * if Main.fileManager is not initialized (in tryOpen method to check if password is correct)
     * @param key formatted and generated Key to decrypt with
     * @param encryptedData string with encrypted data
     * @throws BadPaddingException this exception is usually thrown if password is incorrect
     * @throws InvalidKeyException probably incorrect key as well
     * @return decrypted string if password was correct
     */
    private static String tryDecrypt (Key key, String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    /**
     * Static method for encryption with specified Key, can be used
     * even if Main.fileManager is not initialized
     * (if long lines are passed in parameter, then output will be divided into
     * multiple lines, so you should send line by pieces (of 30 prefferd) and then gluing encrypted output)
     * @param key formatted and generated key to encrypt with
     * @param data String to encrypt
     * @return
     * @throws BadPaddingException this exception is usually thrown if password is incorrect
     * @throws InvalidKeyException probably incorrect key as well
     */
    private static String tryEncrypt(Key key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException{
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    /**
     * We use "generateKey()" method to generate a secret key for AES algorithm with a given key.
     * before generating a Key it has to be formatted using "formatPassword" method, because key has to be
     * exactly 16 characters long
     * @param keyValue formatted password
     * @return Key that is ready to use for encryption or decryption
     */
    public static Key generateKey(byte[] keyValue) {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    /**
     * Getter fot default key Length
     * @return default key length
     */
    public int getKeyLength(){
        return keyLength;
    }

    /**
     * Saving accounts to the file
     * first line unique identifier
     * every other lines -> accounts formatted "title/userName/note/type/url/password/date"
     * @return true if saving was successful, if not - false
     */
    public boolean save(){
        try {
            FileWriter fw = new FileWriter(dbFile);
            BufferedWriter out = new BufferedWriter(fw);
            //System.out.println("Writess: " + uniqueId);
            out.write(encrypt(uniqueId));
            out.newLine();

            for(Account ac : Main.accountTable.values()){
                String s = "/title=" +ac.getTitle() + "/username=" + ac.getUserName() + "/comment=" + ac.getComment() + "/type=" + ac.getType();
                s += "/url=" + ac.getURL() + "/password=" + ac.getPassword() + "/time=" + ac.getLastModified() + "/";

                //this part encrypts string by pieces (read in encrypt description why)
                //probably should be created in a separate method and called in public "encrypt" method
                if(s.length() > 30){
                    String t ="";
                    int c = s.length() / 30;
                    for(int i =0; i < c; i++){
                            t += encrypt(s.substring(i * 30, (i + 1) * 30));
                    }
                    t += encrypt(s.substring(c * 30));
                    s = t;

                }else {
                    s = encrypt(s);
                }
                //System.out.println("Writes: " + s);
                out.write(s);
                out.newLine();
            }

            out.close();
            fw.close();
            //System.out.println("Saved");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reads accounts from the file and puts them into Main.accountTable
     * every other lines -> accounts formatted "title/userName/note/type/url/password/date"
     * @return true if successfully read the file
     */
    public boolean load(){
        try {
            FileReader fr = new FileReader(dbFile);
            BufferedReader in = new BufferedReader(fr);

            this.uniqueId = decrypt(in.readLine());

            String line = in.readLine();

            while(line != null){
                String s = line;
                s = decrypt(s);
                String t ="";
                for(int i = 0; i < s.length(); i++){
                    if((int)s.charAt(i) != 2){
                        t += s.charAt(i);
                    }
                }

                s = t;


                //System.out.println("s:"+ s);
                //System.out.println(s.lastIndexOf("title=") + ", " + s.indexOf("/", s.lastIndexOf("title=")));
                String title = s.substring(s.lastIndexOf("title=") + 6,s.indexOf("/", s.lastIndexOf("title=")));


                String userName = s.substring(s.lastIndexOf("username=") + 9,s.indexOf("/", s.lastIndexOf("username=")));

                String note  = s.substring(s.lastIndexOf("comment=") + 8,s.indexOf("/", s.lastIndexOf("comment=")));

                String type = s.substring(s.lastIndexOf("type=") + 5,s.indexOf("/", s.lastIndexOf("type=")));

                String url = s.substring(s.lastIndexOf("url=") + 4,s.indexOf("/", s.lastIndexOf("url=")));

                String password = s.substring(s.lastIndexOf("password=") + 9, s.indexOf("/", s.lastIndexOf("password=")));

                long lastModified = Long.valueOf(s.substring(s.lastIndexOf("time=") + 5, s.indexOf("/",s.lastIndexOf("time=") )));

                Account ac = new Account(title, userName, password, note, type, url, lastModified);

                Main.accountTable.put(title, ac);

                line = in.readLine();
            }
            in.close();
            fr.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Clears the Key file, for security purpose
     */
    public void resetPassword(){
        key = null;
    }

    /**
     * Return current DB file that is used
     * @return .db file
     */
    public File getDbFile(){
        return dbFile;
    }

}

