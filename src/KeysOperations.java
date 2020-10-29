import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeysOperations {


    /**
     * @param file
     * @return privateKey
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String file) throws Exception {
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(IOUtilities.readFile(file));
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey = keyFactoryRSA.generatePrivate(privateKeySpec);
        return privateKey;
    }


    /**
     * @param file
     * @return publicKey
     * @throws Exception
     */
    public static PublicKey getPublicKey(String file) throws Exception {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(IOUtilities.readFile(file));
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicKey = keyFactoryRSA.generatePublic(publicKeySpec);
        return publicKey;
    }

    /**
     * @return SecretKey keyDES
     * @throws Exception
     */
    public static SecretKey generateSecretKeyDES() throws Exception {
        KeyGenerator DESGenerator = KeyGenerator.getInstance("DES", "BC");
        DESGenerator.init(56);
        SecretKey keyDES = DESGenerator.generateKey();

        return keyDES;
    }

    public static SecretKey generateSecretKey(byte[] secretKey) throws Exception {
        DESKeySpec DESSpec = new DESKeySpec(secretKey);
        SecretKeyFactory secretKeyFactoryDES = SecretKeyFactory.getInstance("DES");
        return secretKeyFactoryDES.generateSecret(DESSpec);
    }
}
