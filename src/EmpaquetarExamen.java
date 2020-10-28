import com.sun.istack.internal.NotNull;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.*;
import java.security.*;
import java.util.ArrayList;



public class EmpaquetarExamen {
    public static void main(@NotNull String args[]) throws Exception {
        Security.addProvider(new BouncyCastleProvider()); //Loading security provider ("BC")
        KeysOperations keysOperations = new KeysOperations();
        Paquete pack = new Paquete();
        ArrayList<byte[]> toSign = new ArrayList<>();

        EncryptMethods encrypt = new EncryptMethods();


        if (args.length != 4) {
            showTemplateArgs();
            System.exit(1);
        }

        SecretKey keyDES = keysOperations.generateSecretKeyDES();//Generating secret key for DES algorithm. Needed to cipher the Student's exam file

        byte[] cipheredExamDES = encrypt.DESEncrypting(args[0], keyDES);// DES process to cipher student's exam.
        toSign.add(cipheredExamDES);

        PublicKey teacherPublicKey = keysOperations.getPublicKey(args[3]);//Getting public teacher key to cipher secret key.
        byte[] cipheredDESKey = encrypt.RSAEncrypting(keyDES.getEncoded(), teacherPublicKey); //RSA process to cipher DES key.
        toSign.add(cipheredDESKey);

        PrivateKey studentPrivateKey = keysOperations.getPrivateKey(args[2]);
        byte[] hash = encrypt.generateHash(toSign) ;
        byte[] studentSign = encrypt.RSAEncrypting(hash , studentPrivateKey);


        pack.anadirBloque("CipherExam", cipheredExamDES);
        pack.anadirBloque("SecretKey", cipheredDESKey);
        pack.anadirBloque("StudentSignature", studentSign);

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
