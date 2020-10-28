import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class EncryptMethods {

    public byte[] DESEncrypting(String toEncrypt, SecretKey keyDES) throws Exception{
        //Creating cipher.
        Cipher cipherDES = Cipher.getInstance("DES/ECB/PKCS5Padding");
        //Init cipher to cipher mode
        cipherDES.init(Cipher.ENCRYPT_MODE, keyDES);
        //Cipher exam.
        return cipherDES.doFinal(IOUtilities.readFile(toEncrypt));
    }
    public byte[] RSAEncrypting(byte[] toEncrypt , PublicKey publicKey) throws Exception{
        //Cipher secret key with RSA
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        //Creating cipher.
        Cipher cipherRSA = Cipher.getInstance("RSA", "BC");
        //Init cipher to cipher mode
        cipherRSA.init(Cipher.ENCRYPT_MODE, publicKey);
        //Cipher exam.

        return cipherRSA.doFinal(toEncrypt);
    }

    public byte[] RSAEncrypting(byte[] toEncrypt , PrivateKey privateKey) throws Exception{
        //Cipher secret key with RSA
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        //Creating cipher.
        Cipher cipherRSA = Cipher.getInstance("RSA", "BC");
        //Init cipher to cipher mode
        cipherRSA.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipherRSA.doFinal(toEncrypt);
    }

    public byte[] generateHash(ArrayList<byte[]> toHash) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        for (byte[] toSignObject : toHash) {
            messageDigest.digest(toSignObject);
        }
        return messageDigest.digest();
    }

}
