import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.PrivateKey;

public class DecryptMethods{



    public byte[] DESDecrypt(SecretKey secretKey, byte[] toDecrypt) throws Exception {
        Cipher decipherDES = Cipher.getInstance("DES");
        decipherDES.init(Cipher.DECRYPT_MODE, secretKey);
        return decipherDES.doFinal(toDecrypt);
    }


    public byte[] RSADecrypt(PrivateKey privateKey, byte[] toDecrypt) throws Exception {
        Cipher decipherRSA = Cipher.getInstance("RSA", "BC");
        decipherRSA.init(Cipher.DECRYPT_MODE, privateKey);
        return  decipherRSA.doFinal(toDecrypt);

    }
}
