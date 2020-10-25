import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class IOUtilities {

    /**
     * @param f
     * @return fileBuffer
     * @throws Exception
     */
    public static byte[] readFile(String f) throws Exception {
        File file = new File(f);
        int fileLength = (int) file.length();
        byte[] fileBuffer = new byte[fileLength];
        FileInputStream in = new FileInputStream(file);
        in.read(fileBuffer, 0, fileLength);
        in.close();

        return fileBuffer;
    }

    public static void showBytes(byte [] buffer) {
        System.out.write(buffer, 0, buffer.length);
        System.out.println("\n-----\n");
    }

    public static void UnpackExamTests(){

    }




}
