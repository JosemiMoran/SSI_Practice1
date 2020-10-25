import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;

public class SigningMethods {

    public byte[] generateSignature(PrivateKey privateKey, ArrayList<byte[]> toSign) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA", "BC");
        signature.initSign(privateKey);
        for (byte[] toSignObject : toSign) {
            signature.update(toSignObject);
        }
        return signature.sign();
    }

    public boolean verifySignature(PublicKey publicKey, byte[] sign) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA", "BC");
        signature.initVerify(publicKey);
        signature.update(sign);
        boolean toret = signature.verify(sign);

        return toret;
    }

}
