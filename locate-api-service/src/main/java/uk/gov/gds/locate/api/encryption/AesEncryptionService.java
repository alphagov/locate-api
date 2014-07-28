package uk.gov.gds.locate.api.encryption;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

public abstract class AesEncryptionService {

    private static final String algorithm = "AES/CBC/PKCS5Padding";

    private static final SecureRandom random = new SecureRandom();

    static {
        random.setSeed(DateTime.now().getMillis());
    }

    public static SecretKeySpec aesKeyFromBase64EncodedString(String key) {
        return new SecretKeySpec(Base64.decodeBase64(key), "AES");
    }

    private static byte[] decodeBase64(String content) {
        return Base64.decodeBase64(content);
    }

    private static IvParameterSpec generateInitializationVector() {
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static AesEncryptionProduct encrypt(String content, Key aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        IvParameterSpec ivSpec = generateInitializationVector();
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
        return new AesEncryptionProduct(cipher.doFinal(content.getBytes()), ivSpec.getIV());
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