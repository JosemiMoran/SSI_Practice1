import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;

public class SigningMethods {

        public byte[] generateSignature(PrivateKey privateKey, ArrayList<byte[]> toSign) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA", "BC");
        signature.initSign(privateKey);
        for (byte[] toSignObject: toSign) {
            signature.update(toSignObject);
        }
        return  signature.sign();
    }

}
