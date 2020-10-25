import org.bouncycastle.asn1.cms.OriginatorPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;

public class SellarExamen {
    public static void main(String args[]) throws Exception{
        KeysOperations keysOperations = new KeysOperations();
        SigningMethods signingMethods = new SigningMethods();
        Security.addProvider(new BouncyCastleProvider());
        Paquete receivedPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        Paquete sealedPack = new Paquete();
        ArrayList<byte[]> toSign = new ArrayList<>();

        if (args.length != 3) {
            showTemplateArgs();
            System.exit(1);
        }


        //Date object
        Date date= new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        byte[] timeStamp = buffer.putLong(time).array();


        byte[] cipheredSecretKey = receivedPack.getContenidoBloque("SECRETKEY");
        byte[] studentExam = receivedPack.getContenidoBloque("CIPHEREXAM");
        byte[] studentSign = receivedPack.getContenidoBloque("STUDENTSIGNATURE");

        PublicKey studentPublicKey = keysOperations.getPublicKey(args[1]);
        PrivateKey authorityPrivateKey = keysOperations.getPrivateKey(args[2]);

        boolean signVerified = signingMethods.verifySignature(studentPublicKey, studentSign );
        if (signVerified) {
            System.out.println("Correct signature");
            sealedPack.anadirBloque("SECRETKEY", cipheredSecretKey);
            sealedPack.anadirBloque("CIPHEREXAM" , studentExam);
            sealedPack.anadirBloque("STUDENTSIGNATURE", studentSign);
            sealedPack.anadirBloque("TIMESTAMP" , timeStamp);

            toSign.add(cipheredSecretKey);
            toSign.add(studentExam);
            toSign.add(studentSign);
            toSign.add(timeStamp);

            byte[] sign = signingMethods.generateSignature(authorityPrivateKey , toSign);

            sealedPack.anadirBloque("AUTHORITYSIGN" , sign);
            String packName = args[1].toString();
            packName += ".paquete";
            PaqueteDAO.escribirPaquete(packName, sealedPack);

        }else System.out.println("incorrect sign");
    }

    private static void showTemplateArgs() {
        System.out.println("");
        System.out.println("\tArgs Syntax:");
        System.out.println();
    }

}
