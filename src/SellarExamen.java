import org.bouncycastle.asn1.cms.OriginatorPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

public class SellarExamen {
    public static void main(String args[]) throws Exception{
        KeysOperations keysOperations = new KeysOperations();
        Security.addProvider(new BouncyCastleProvider());
        Paquete receivedPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        ArrayList<byte[]> toVerifySign = new ArrayList<>();
        DecryptMethods decrypt = new DecryptMethods();
        EncryptMethods encrypt = new EncryptMethods();
        ArrayList<byte[]> toSign = new ArrayList<>();

        if (args.length != 3) {
            showTemplateArgs();
            System.exit(1);
        }

        byte[] cipheredSecretKey = receivedPack.getContenidoBloque("SECRETKEY");
        byte[] cipheredStudentExam = receivedPack.getContenidoBloque("CIPHEREXAM");
        byte[] cipheredStudentHash = receivedPack.getContenidoBloque("STUDENTSIGNATURE");


        toVerifySign.add(cipheredStudentExam);
        toVerifySign.add(cipheredSecretKey);

        // Adding Student package to the Sealing Authority signature
        toSign.add(cipheredStudentExam);
        toSign.add(cipheredSecretKey);
        toSign.add(cipheredStudentHash);

        PublicKey studentPublicKey = keysOperations.getPublicKey(args[1]);
        PrivateKey authorityPrivateKey = keysOperations.getPrivateKey(args[2]);

        byte[] studentSign = decrypt.RSADecrypt(studentPublicKey , cipheredStudentHash);
        byte[] hashReceived = encrypt.generateHash(toVerifySign);

        if (Arrays.equals(studentSign, hashReceived)) {
            System.out.println("Correct signature"); // The Student Signature is correct.
            byte[] dateArray = java.time.LocalDateTime.now().toString().getBytes(); //Current local timestamp.

           receivedPack.anadirBloque("SealingDate" , dateArray); //Writing block for sealing date.

           toSign.add(dateArray); // Adding date to the Sealing Authority signature.

            byte[] authoritySealHash = encrypt.generateHash(toSign); // Generate hash.
            byte[] authoritySign = encrypt.RSAEncrypting(authoritySealHash,authorityPrivateKey); // RSA encrypting of generated hash.

            receivedPack.anadirBloque("AuthoritySealingSign" , authoritySign); //Writing block for Authority signature.

            PaqueteDAO.escribirPaquete(args[0] + ".paquete" , receivedPack); // Rewriting package.

        }else System.out.println("Incorrect signature");
    }

    private static void showTemplateArgs() {
        System.out.println("");
        System.out.println("\tArgs Syntax:");
        System.out.println();
    }

}
