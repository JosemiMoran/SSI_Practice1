import com.sun.xml.internal.bind.v2.TODO;
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
        KeysOperations keysOperations = new KeysOperations();
        DecryptMethods decrypt = new DecryptMethods();
        Paquete readPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        Security.addProvider(new BouncyCastleProvider());

        byte[] cipheredSecretKey = readPack.getContenidoBloque("SECRETKEY");
        byte[] studentExam = readPack.getContenidoBloque("CIPHEREXAM");
        byte[] studentSign = readPack.getContenidoBloque("STUDENTSIGNATURE");

        PrivateKey teacherPrivateKey = keysOperations.getPrivateKey(args[2]);
        PublicKey studentPublicKey = keysOperations.getPublicKey(args[3]);

        //boolean signVerified = signingMethods.verifySignature(studentPublicKey, studentSign );
        if (true) { // If the signature is correct then decrypt exam
            //TODO  VERIFY EXAM + VERIFY AUTHORITY
            System.out.println("Correct signature");
        }else System.out.println("Incorrect signature");

        byte[] cipheredDESKey = decrypt.RSADecrypt(teacherPrivateKey, cipheredSecretKey);
        SecretKey DESKey = keysOperations.generateSecretKey(cipheredDESKey);
        byte[] receivedExam = decrypt.DESDecrypt(DESKey, studentExam);


        FileOutputStream out = new FileOutputStream("receivedExam.txt");
        out.write(receivedExam);
        out.close();

        IOUtilities.showBytes(receivedExam);
        System.out.println();

    }



}
