import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;

public class SellarExamen {
    public static void main(@NotNull String args[]) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Paquete receivedPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        ArrayList<byte[]> toVerifySign = new ArrayList<>();
        ArrayList<byte[]> toSign = new ArrayList<>();

        //Validating args syntax
        if (args.length != 3) {
            ShowSyntaxArgs.showTemplateArgs("SealingAuthority");
            System.exit(1);
        }

        //Getting needed keys
        PublicKey studentPublicKey = KeysOperations.getPublicKey(args[1]);
        PrivateKey authorityPrivateKey = KeysOperations.getPrivateKey(args[2]);

        //Getting block from the received package
        byte[] cipheredSecretKey = receivedPack.getContenidoBloque("SECRETKEY");
        byte[] cipheredStudentExam = receivedPack.getContenidoBloque("CIPHEREXAM");
        byte[] cipheredStudentHash = receivedPack.getContenidoBloque("STUDENTSIGNATURE");

        //Adding student exam and ciphered secret key to validate hashes.
        toVerifySign.add(cipheredStudentExam);
        toVerifySign.add(cipheredSecretKey);


        if (DecryptMethods.verifyHash(toVerifySign , cipheredStudentHash , studentPublicKey)) {
            IOUtilities.message("Correct signature"); // The Student Signature is correct.

            byte[] dateArray = java.time.LocalDateTime.now().toString().getBytes(); //Current local timestamp.
            receivedPack.anadirBloque("SealingDate", dateArray); //Writing block for sealing date.

            // Adding Student package to the Sealing Authority signature
            toSign.add(cipheredStudentExam);
            toSign.add(cipheredSecretKey);
            toSign.add(cipheredStudentHash);
            toSign.add(dateArray);
            byte[] authoritySealHash = EncryptMethods.generateHash(toSign); // Generate hash.
            byte[] authoritySign = EncryptMethods.RSAEncrypting(authoritySealHash, authorityPrivateKey); // RSA encrypting of generated hash.

            receivedPack.anadirBloque("AuthoritySealingSign", authoritySign); //Writing block for Authority signature.
            PaqueteDAO.escribirPaquete(args[0] + ".paquete", receivedPack); // Rewriting package.

        } else IOUtilities.message("Incorrect signature");
        IOUtilities.message("Sealed package");
    }
}
