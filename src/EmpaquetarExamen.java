import com.sun.istack.internal.NotNull;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;


public class EmpaquetarExamen {
    public static void main(@NotNull String args[]) {
//        Loading security provider ("BC")
        Security.addProvider(new BouncyCastleProvider());
//
        if (args.length != 4) {
            showTemplateArgs();
            System.exit(1);
        }
//      Generating secret key for DES algorithm. Needed to cipher the Student's exam file
        KeyGenerator DESGenerator = null;
        try {
            DESGenerator = KeyGenerator.getInstance("DES", "BC");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getStackTrace());
        } catch (NoSuchProviderException e) {
            System.out.println(e.getStackTrace());
        }
        DESGenerator.init(56);
        SecretKey key = DESGenerator.generateKey();

//      Creating cipher.
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getStackTrace());
        } catch (NoSuchPaddingException e) {
            System.out.println(e.getStackTrace());
        }

// Init cipher to cipher mode
        try {
            cipher.init(Cipher.ENCRYPT_MODE,key);
        } catch (InvalidKeyException e) {
            System.out.println(e.getStackTrace());
        }

// Reading Student's exam file
        byte[] buffer = null;
        try {
             buffer = Files.readAllBytes(Paths.get(args[0]));
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

        try {
            cipher.doFinal(buffer); //Cipher exam.
        } catch (IllegalBlockSizeException e) {
            System.out.println(e.getStackTrace());
        } catch (BadPaddingException e) {
            System.out.println(e.getStackTrace());
        }


    }
        private static void showTemplateArgs() {
        System.out.println("EmpaquetarExamen");
        System.out.println("\tArgs Syntax: ExamFile PackageName Student.private Teacher.public ");
        System.out.println();
    }
}
