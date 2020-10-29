import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

public class DecryptMethods{



    public static byte[] DESDecrypt(SecretKey secretKey, byte[] toDecrypt) throws Exception {
        Cipher decipherDES = Cipher.getInstance("DES");
        decipherDES.init(Cipher.DECRYPT_MODE, secretKey);
        return decipherDES.doFinal(toDecrypt);
    }


    public static byte[] RSADecrypt(PrivateKey privateKey, byte[] toDecrypt) throws Exception {
        Cipher decipherRSA = Cipher.getInstance("RSA", "BC");
        decipherRSA.init(Cipher.DECRYPT_MODE, privateKey);
        return  decipherRSA.doFinal(toDecrypt);
    }

    public static byte[] RSADecrypt(PublicKey publicKey, byte[] toDecrypt) throws Exception {
        Cipher decipherRSA = Cipher.getInstance("RSA", "BC");
        decipherRSA.init(Cipher.DECRYPT_MODE, publicKey);
        return  decipherRSA.doFinal(toDecrypt);
    }

    public static boolean verifyHash(ArrayList<byte[]> hash1, byte[] hash2 , PublicKey publicKey) throws Exception{
        return Arrays.equals(EncryptMethods.generateHash(hash1),
                DecryptMethods.RSADecrypt(publicKey,hash2));

    }
}
