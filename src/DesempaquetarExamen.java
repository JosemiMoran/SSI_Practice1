import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import java.security.Security;
import java.security.*;
import javax.crypto.*;
import java.util.ArrayList;


public class DesempaquetarExamen {
    public static void main(@NotNull String args[]) throws Exception {
        Paquete readPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        Security.addProvider(new BouncyCastleProvider());
        ArrayList<byte[]> toValidateStudentSign = new ArrayList<>();
        ArrayList<byte[]> toValidateAuthoritySign = new ArrayList<>();
            if(args.length != 5){
                ShowSyntaxArgs.showTemplateArgs("Teacher");
                System.exit(1);
            }



        //Getting blocks of the received package
        byte[] cipheredSecretKey = readPack.getContenidoBloque("SECRETKEY");
        byte[] studentExam = readPack.getContenidoBloque("CIPHEREXAM");
        byte[] studentSign = readPack.getContenidoBloque("STUDENTSIGNATURE");
        byte[] dateArray = readPack.getContenidoBloque("SEALINGDATE");
        byte[] authoritySign = readPack.getContenidoBloque("AUTHORITYSEALINGSIGN");


        //Getting all needed keys
        PrivateKey teacherPrivateKey = KeysOperations.getPrivateKey(args[2]);
        PublicKey studentPublicKey = KeysOperations.getPublicKey(args[3]);
        PublicKey authorityPublicKey = KeysOperations.getPublicKey(args[4]);

        //Validating Authority hash
        toValidateAuthoritySign.add(studentExam);
        toValidateAuthoritySign.add(cipheredSecretKey);
        toValidateAuthoritySign.add(studentSign);
        toValidateAuthoritySign.add(dateArray);

        //Validating Student hash
        toValidateStudentSign.add(studentExam);
        toValidateStudentSign.add(cipheredSecretKey);

        if (DecryptMethods.verifyHash(toValidateAuthoritySign, authoritySign ,authorityPublicKey)) { // AuthorityHash = ReceivedAuthorityHash
            IOUtilities.message("Correct Authority signature");
            if (DecryptMethods.verifyHash(toValidateStudentSign ,studentSign , studentPublicKey)){ // StudentHash = ReceivedStudentHash
                IOUtilities.message("Correct Student signature");
                //Decrypting Secret Key
                byte[] cipheredDESKey = DecryptMethods.RSADecrypt(teacherPrivateKey, cipheredSecretKey);
                SecretKey DESKey = KeysOperations.generateSecretKey(cipheredDESKey);
                //Decrypting Exam
                byte[] receivedExam = DecryptMethods.DESDecrypt(DESKey, studentExam);
                //Writing Received Exam
                IOUtilities.writeFile("receivedExam.txt" , receivedExam);
                //Showing exam on screen
                IOUtilities.message("****************");
                IOUtilities.showBytes(receivedExam);
                IOUtilities.message("****************");

            }else IOUtilities.message("Incorrect Student signature"); // End StudentHash = ReceivedStudentHash
        }else IOUtilities.message("Incorrect Authority Signature"); // End AuthorityHash = ReceivedAuthorityHash

    } //End Main
} // End class
