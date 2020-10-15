import com.sun.istack.internal.NotNull;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class EmpaquetarExamen {
    public static void main(@NotNull String args[]) throws Exception {

        Paquete pack = new Paquete();

        if (args.length != 4) {
            showTemplateArgs();
            System.exit(1);
        }
//        Loading security provider ("BC")
        Security.addProvider(new BouncyCastleProvider());

//     Generating secret key for DES algorithm. Needed to cipher the Student's exam file
        KeyGenerator DESGenerator = KeyGenerator.getInstance("DES", "BC");
        DESGenerator.init(56);
        SecretKey keyDES = DESGenerator.generateKey();

//      Creating cipher.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");


// Init cipher to cipher mode

        cipher.init(Cipher.ENCRYPT_MODE, keyDES);


// Reading Student's exam file
        byte[] buffer = Files.readAllBytes(Paths.get(args[0]));

//Cipher exam.
        String cipheredBufferDES = cipher.doFinal(buffer).toString();

        pack.anadirBloque("CipherExam", cipheredBufferDES.getBytes(Charset.forName("UTF-8")));

//Cipher secret key with RSA
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
//Getting Student's private key
        File privateKeyFile = new File(args[2] );
        int privateKeyFileLength = (int) privateKeyFile.length();
        byte[] bufferPrivate = new byte[privateKeyFileLength];
        FileInputStream in = new FileInputStream(privateKeyFile);
        in.read(bufferPrivate, 0, privateKeyFileLength);
        in.close();

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bufferPrivate);
        PrivateKey privateKey = keyFactoryRSA.generatePrivate(privateKeySpec);

        //Getting Teacher's public key
        File publicKeyFile = new File(args[3] );
        int publicKeyFileLength = (int) publicKeyFile.length();
        byte[] bufferPublic = new byte[privateKeyFileLength];
        in = new FileInputStream(publicKeyFile);
        in.read(bufferPublic, 0, publicKeyFileLength);
        in.close();

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bufferPrivate);
        PublicKey publicKey = keyFactoryRSA.generatePublic(publicKeySpec);

        Cipher cipherRSA = Cipher.getInstance("RSA", "BC");
        cipherRSA.init(Cipher.ENCRYPT_MODE, publicKey);

        String cipheredDESKey = cipherRSA.doFinal(keyDES.getEncoded()).toString();

        pack.anadirBloque("CipherKey", cipheredDESKey.getBytes(Charset.forName("UTF-8")));
        String packName = args[1].toString();
        packName += ".paquete";
        PaqueteDAO.escribirPaquete(packName, pack);

    }

    private static void showTemplateArgs() {
        System.out.println("EmpaquetarExamen");
        System.out.println("\tArgs Syntax: ExamFile PackageName Student.private Teacher.public ");
        System.out.println();
    }
}
