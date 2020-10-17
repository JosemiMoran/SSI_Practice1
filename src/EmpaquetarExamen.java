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
//////////////////////////////// DES PROCESS TO CIPHER STUDENT'S EXAM ////////////////////////////////////////////////////
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
        byte[] cipheredBufferDES = cipher.doFinal(buffer);


//////////////////////////////// END DES PROCESS TO CIPHER STUDENT'S EXAM ////////////////////////////////////////////////////


//////////////////////////////// RSA PROCESS TO CIPHER DES SECRET KEY ////////////////////////////////////////////////////
//Cipher secret key with RSA
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        PublicKey teacherPublicKey = getPublicKey(args[3]);
        Cipher cipherRSA = Cipher.getInstance("RSA", "BC");
        cipherRSA.init(Cipher.ENCRYPT_MODE, teacherPublicKey);
        byte[] cipheredDESKey = cipherRSA.doFinal(keyDES.getEncoded());
//////////////////////////////// END RSA PROCESS TO CIPHER DES SECRET KEY ////////////////////////////////////////////////////

//////////////////////////////// WRITING PACKAGE ////////////////////////////////////////////////////

        pack.anadirBloque("CipherExam", cipheredBufferDES);
        pack.anadirBloque("SecretKey", cipheredDESKey);
        String packName = args[1].toString();
        packName += ".paquete";
        PaqueteDAO.escribirPaquete(packName, pack);
//////////////////////////////// END WRITING PACKAGE ////////////////////////////////////////////////////
    }

    /**
     * @param file
     * @return privateKey
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String file) throws Exception{
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(readFile(file));
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey = keyFactoryRSA.generatePrivate(privateKeySpec);
        return privateKey;
    }


    /**
     * @param file
     * @return publicKey
     * @throws Exception
     */
    private static PublicKey getPublicKey(String file) throws Exception{
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(readFile(file));
        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicKey = keyFactoryRSA.generatePublic(publicKeySpec);
        return publicKey;
    }


    /**
     * @param f
     * @return fileBuffer
     * @throws Exception
     */
    public static byte[] readFile(String f) throws Exception{
        File file = new File(f);
        int fileLength = (int) file.length();
        byte[] fileBuffer = new byte[fileLength];
        FileInputStream in = new FileInputStream(file);
        in.read(fileBuffer, 0, fileLength);
        in.close();

        return fileBuffer;
    }

    /**
     *
     */
    private static void showTemplateArgs() {
        System.out.println("EmpaquetarExamen");
        System.out.println("\tArgs Syntax: ExamFile PackageName Student.private Teacher.public ");
        System.out.println();
    }
}
