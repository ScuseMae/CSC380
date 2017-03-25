//import Main.FileManager;
//import org.junit.*;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.io.*;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Random;
//
//
///**
// * Created by Bohdan Yevdokymov
// */
//
//public class FileMenegerTest {
//
//    private static FileManager file;
//    private static int keyLegth;
//    private static byte[] key;
//    private static File dbFile;
//
//
//
//    @BeforeClass
//    public static void initFileManager(){
//        file = new FileManager();
//        keyLegth = file.getKeyLength();
//        key = new byte[keyLegth];
//
//        for(int i =0; i < keyLegth; i ++){
//
//            key[i] = (byte) (keyLegth - i);
//        }
//        //file.setKey(key);
//
//        dbFile = new File(System.getProperty("user.dir") + "/" + "testFile2" + ".db");
//
//        try{
//            file.createNewDB("testFile2", key, System.getProperty("user.dir"));
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @AfterClass
//    public static void finishTest(){
//        dbFile.delete();
//    }
//
//    @Before
//    public void resetKey(){
//        key = new byte[keyLegth];
//
//        for(int i =0; i < keyLegth; i ++){
//
//            key[i] = (byte) (keyLegth - i);
//        }
//        file.setKey(key);
//    }
//
//
//    @Test
//    public void testEncrypt() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
//
//        String send     = "testmessage123";
//        String encoded  = file.encrypt(send);
//        Assert.assertEquals("ZbfLENSt/qUaYwJSrDO02g==",encoded);
//    }
//
//    @Test
//    public void testDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
//
//        String received = file.decrypt("ZbfLENSt/qUaYwJSrDO02g==");
//
//        Assert.assertEquals("testmessage123", received);
//    }
//
//    @Test
//    public void testCreateNewDB() throws IOException {
//
//        String fileName = "testFile";
//        String path     =  System.getProperty("user.dir");
//
//        file.createNewDB(fileName, key, path);
//
//            File f = new File(path + "/" + fileName + ".db");
//            FileReader fr = new FileReader(f);
//            BufferedReader br = new BufferedReader(fr);
//            br.readLine();
//            br.close();
//            fr.close();
//            f.delete();
//
//    }
//
//    @Test
//    public void testTryOpenPositive(){
//
//        Assert.assertTrue(file.tryOpen(dbFile, key));
//
//    }
//
//    @Test
//    public void testTryOpenNegative(){
//        byte []newKey = new byte[keyLegth];
//        Random rand = new Random();
//
//        for(int i =0; i < keyLegth; i ++){
//
//            newKey[i] = (byte) rand.nextInt(10);
//        }
//
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
