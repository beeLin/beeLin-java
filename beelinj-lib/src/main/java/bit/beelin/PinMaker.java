package bit.beelin;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * Convert colon-separated hex fingerprint to format expected by OKHttp
 */
public class PinMaker {

    /**
     * Convert colon-separated hex fingerprint to format expected by OKHttp
     */
    public static String colonHexToBase64Pin(String colonHex) throws DecoderException {
        String hex = colonHex.replace(":", "");             // Strip colons from colon-separated fingerprint
        byte[] bytes = Hex.decodeHex(hex.toCharArray());    // Convert to Hex
        String base64 = Base64.encodeBase64String(bytes);   // Convert to Base64
        return "sha1/" + base64;                            // Add prefix and return
    }
}
