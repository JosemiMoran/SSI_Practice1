import org.bouncycastle.jcajce.provider.symmetric.DES;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.Charset;
import java.security.Security;
import java.security.*;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import java.io.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedList;
import java.util.List;

public class DesempaquetarExamen {
    public static void main(String args[]) throws Exception {
        Paquete readPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        Security.addProvider(new BouncyCastleProvider());
//
        Cipher DESCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");

        File teacherPrivateKeyFile = new File(args[2]);
        int privateKeyFileLength = (int) teacherPrivateKeyFile.length();
        byte[] bufferPrivate = new byte[privateKeyFileLength];
        FileInputStream in = new FileInputStream(teacherPrivateKeyFile);
        in.read(bufferPrivate, 0, privateKeyFileLength);
        in.close();

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bufferPrivate);
        PrivateKey privateKey = keyFactoryRSA.generatePrivate(privateKeySpec);


        File publicKeyFile = new File(args[3]);
        int publicKeyFileLength = (int) publicKeyFile.length();
        byte[] bufferPublic = new byte[publicKeyFileLength];
        in = new FileInputStream(publicKeyFile);
        in.read(bufferPublic, 0, publicKeyFileLength);
        in.close();

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bufferPublic);
        PublicKey StudentpublicKey = keyFactoryRSA.generatePublic(publicKeySpec);

    }

}
