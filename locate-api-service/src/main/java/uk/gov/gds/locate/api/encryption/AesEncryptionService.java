package uk.gov.gds.locate.api.encryption;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class AesEncryptionService {

    private static final String algorithm = "AES/CBC/PKCS5Padding";

    private static SecretKeySpec aesKeyFromBase64EncodedString(String key) {
        return new SecretKeySpec(Base64.decodeBase64(key), "AES");
    }

    private static byte[] decodeBase64(String content) {
        return Base64.decodeBase64(content);
    }

    public static String decrypt(String encryptedContent, String aesKey, String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, aesKeyFromBase64EncodedString(aesKey), new IvParameterSpec(decodeBase64(iv)));
            return new String(cipher.doFinal(decodeBase64(encryptedContent)));

        } catch (BadPaddingException ex) {
            throw new Exception("AES decryption failed. Possible cause: incorrect private key", ex);
        } catch (Exception ex) {
            throw new Exception("AES decryption failed", ex);
        }
    }
}