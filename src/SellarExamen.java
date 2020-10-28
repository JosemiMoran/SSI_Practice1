import org.bouncycastle.asn1.cms.OriginatorPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SellarExamen {
    public static void main(String args[]) throws Exception{
        KeysOperations keysOperations = new KeysOperations();
        Security.addProvider(new BouncyCastleProvider());
        Paquete receivedPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        ArrayList<byte[]> toVerifySign = new ArrayList<>();
        DecryptMethods decrypt = new DecryptMethods();
        EncryptMethods encrypt = new EncryptMethods();

        if (args.length != 3) {
            showTemplateArgs();
            System.exit(1);
        }

        byte[] cipheredSecretKey = receivedPack.getContenidoBloque("SECRETKEY");
        byte[] cipheredStudentExam = receivedPack.getContenidoBloque("CIPHEREXAM");
        byte[] cipheredStudentHash = receivedPack.getContenidoBloque("STUDENTSIGNATURE");

        toVerifySign.add(cipheredStudentExam);
        toVerifySign.add(cipheredSecretKey);

        PublicKey studentPublicKey = keysOperations.getPublicKey(args[1]);
        PrivateKey authorityPrivateKey = keysOperations.getPrivateKey(args[2]);

        byte[] studentSign = decrypt.RSADecrypt(studentPublicKey , cipheredStudentHash);
        byte[] hashReceived = encrypt.generateHash(toVerifySign);


        //boolean signVerified = signingMethods.verifySignature(studentPublicKey, studentSign );
        if (Arrays.equals(studentSign, hashReceived)) {
            System.out.println("Correct signature");
            //TODO
            // ADD TIMESTAMP IN PACKAGE
            // GENERATE HASH FROM TIMESTAMP , RECEIVED CIPHERED HASH, RECEIVED CIPHERED EXAM AND DES KEY
            // ADD TO PACKAGE
            // WRITE PACKAGE
        }else System.out.println("Incorrect signature");
    }

    private static void showTemplateArgs() {
        System.out.println("");
        System.out.println("\tArgs Syntax:");
        System.out.println();
    }

}
