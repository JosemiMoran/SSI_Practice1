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

        ArrayList<byte[]> examBuffer = new ArrayList<>();
        for (String blockName : readPack.getNombresBloque()) {
            byte[] block = readPack.getContenidoBloque(blockName);
            String blockContent = new String(block, Charset.forName("UTF-8"));
            System.out.println("\t"+block+": "+ blockContent.replace("\n", " "));
        }

        byte[] cipheredSecretKey = readPack.getContenidoBloque("SECRETKEY");
        byte[] studentExam = readPack.getContenidoBloque("CIPHEREXAM");



        PrivateKey teacherPrivateKey = keysOperations.getPrivateKey(args[2]);
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
