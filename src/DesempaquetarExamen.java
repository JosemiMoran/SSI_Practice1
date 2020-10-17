import org.bouncycastle.jcajce.provider.symmetric.DES;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.security.Security;
import java.security.*;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import java.io.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DesempaquetarExamen {
    public static void main(String args[]) throws Exception {
        Paquete readPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        Security.addProvider(new BouncyCastleProvider());

        ArrayList<byte[]> examBuffer = new ArrayList<>();
        for (String blockName : readPack.getNombresBloque()) {
            byte[] block = readPack.getContenidoBloque(blockName);
            String blockContent = new String(block, Charset.forName("UTF-8"));
            System.out.println("\t"+block+": "+ blockContent.replace("\n", " "));
        }

        byte[] cipheredSecretKeyBuffer = readPack.getContenidoBloque("SECRETKEY");
        byte[] studentExam = readPack.getContenidoBloque("CIPHEREXAM");

        Cipher decipherRSA = Cipher.getInstance("RSA", "BC");
        PrivateKey teacherPrivateKey = getPrivateKey(args[2]);
        decipherRSA.init(Cipher.DECRYPT_MODE, teacherPrivateKey);
        byte[] DESKeyBuffer = decipherRSA.doFinal(cipheredSecretKeyBuffer);

        DESKeySpec DESSpec = new DESKeySpec(DESKeyBuffer);
        SecretKeyFactory secretKeyFactoryDES = SecretKeyFactory.getInstance("DES");
        SecretKey DESKey = secretKeyFactoryDES.generateSecret(DESSpec);

        Cipher decipherDES = Cipher.getInstance("DES");
        decipherDES.init(Cipher.DECRYPT_MODE, DESKey);
        byte[] decipheredExamBuffer = decipherDES.doFinal(studentExam);

        FileOutputStream out = new FileOutputStream("receivedExam.txt");
        out.write(decipheredExamBuffer);
        out.close();
        showBytes(decipheredExamBuffer);
        System.out.println();

    }

    private static PrivateKey getPrivateKey(String file) throws Exception{
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(EmpaquetarExamen.readFile(file));
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey = keyFactoryRSA.generatePrivate(privateKeySpec);
        return privateKey;
    }

    private static PublicKey getPublicKey(String file) throws Exception{
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(EmpaquetarExamen.readFile(file));
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicKey = keyFactoryRSA.generatePublic(publicKeySpec);
        return publicKey;
    }

    public static void showBytes(byte [] buffer) {
        System.out.write(buffer, 0, buffer.length);
        System.out.println("\n-----\n");
    }

}
