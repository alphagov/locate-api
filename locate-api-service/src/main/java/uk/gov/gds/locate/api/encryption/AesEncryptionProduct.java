package uk.gov.gds.locate.api.encryption;


public class AesEncryptionProduct {
    private final byte[] encryptedContent;

    private final byte[] initializationVector;

    public AesEncryptionProduct(byte[] encryptedContent, byte[] initializationVector) {
        this.encryptedContent = encryptedContent;
        this.initializationVector = initializationVector;
    }

    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    public byte[] getInitializationVector() {
        return initializationVector;
    }
}
