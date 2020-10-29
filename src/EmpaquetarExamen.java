import com.sun.istack.internal.NotNull;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.*;
import java.security.*;
import java.util.ArrayList;



public class EmpaquetarExamen{
    public static void main(@NotNull String args[]) throws Exception {

        Security.addProvider(new BouncyCastleProvider()); //Loading security provider ("BC")
        Paquete pack = new Paquete();
        ArrayList<byte[]> toSign = new ArrayList<>();

        if (args.length != 4) {
            ShowSyntaxArgs.showTemplateArgs("Student");
            System.exit(1);
        }

        SecretKey keyDES = KeysOperations.generateSecretKeyDES();//Generating secret key for DES algorithm. Needed to cipher the Student's exam file

        byte[] cipheredExamDES = EncryptMethods.DESEncrypting(args[0], keyDES);// DES process to cipher student's exam.
        toSign.add(cipheredExamDES);

        PublicKey teacherPublicKey = KeysOperations.getPublicKey(args[3]);//Getting public teacher key to cipher secret key.
        byte[] cipheredDESKey = EncryptMethods.RSAEncrypting(keyDES.getEncoded(), teacherPublicKey); //RSA process to cipher DES key.
        toSign.add(cipheredDESKey);

        PrivateKey studentPrivateKey = KeysOperations.getPrivateKey(args[2]);
        byte[] hash = EncryptMethods.generateHash(toSign) ;
        byte[] studentSign = EncryptMethods.RSAEncrypting(hash , studentPrivateKey);

        pack.anadirBloque("CipherExam", cipheredExamDES);
        pack.anadirBloque("SecretKey", cipheredDESKey);
        pack.anadirBloque("StudentSignature", studentSign);

        String packName = args[1].toString();
        packName += ".paquete";
        PaqueteDAO.escribirPaquete(packName, pack);
        IOUtilities.message("Wrote package");
    }




}
