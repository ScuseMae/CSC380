package Tests;
import java.security.SecureRandom;
import java.math.BigInteger;


/**
 * Created by bohdan on 3/1/17.
 */
public class testing {
    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());

        SecureRandom random = new SecureRandom();

        System.out.println(new BigInteger(160, random).toString(32).length());
        System.out.println(new BigInteger(160, random).toString(32));

        System.out.println(new BigInteger(130, random).toString(32));

        System.out.println(new BigInteger(130, random).toString(32));



    }
}
