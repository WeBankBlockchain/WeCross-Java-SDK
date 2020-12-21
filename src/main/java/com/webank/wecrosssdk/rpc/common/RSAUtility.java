package com.webank.wecrosssdk.rpc.common;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAUtility {

    private static Logger logger = LoggerFactory.getLogger(RSAUtility.class);

    /**
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        logger.info(
                "RSA pri: {}",
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        logger.info(
                "RSA Pub: {}",
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        return keyPair;
    }

    /**
     * @param encryptedData
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptBase64(String encryptedData, PrivateKey privateKey)
            throws Exception {
        byte[] decodeBase64 = Base64.getDecoder().decode(encryptedData);
        return decrypt(decodeBase64, privateKey);
    }

    /**
     * @param encryptedData
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    /**
     * @param sourceData
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] sourceData, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(sourceData);
    }

    /**
     * @param sourceData
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptBase64(byte[] sourceData, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(sourceData);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public static PublicKey createPublicKey(String content)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509KeySpec =
                new X509EncodedKeySpec(Base64.getDecoder().decode(content));
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        return pubKey;
    }

    /**
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public static PrivateKey createPrivateKey(String content)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        PemReader pemReader = new PemReader(new StringReader(content));
        PemObject pemObject = pemReader.readPemObject();
        byte[] pemContent = pemObject.getContent();
        RSAPrivateKey rsaPrivateKey =
                RSAPrivateKey.getInstance(ASN1Sequence.fromByteArray(pemContent));
        RSAPrivateKeySpec rsaPrivateKeySpec =
                new RSAPrivateKeySpec(
                        rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
        return privateKey;
    }

    public static void main(String[] args)
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String pub =
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAxpO0sDr8aAqdWh/ExsEmwAgQRdglFV2Pz++2ve4n9q83IYTqI1heHI+++wIhclW2Z//tTGJyRiT0cZ2dyiG+KQyO+6h5v4/E7fTrsLKGszaOMvfjl06tDNOI4e52VQ1Gi9zXCePpPnpYLMZY36aeDQi6qGCE9HtFD/Rr2RtByr/SLjdbT1C5Br0HDEGXC8Lr3ht3xkb/xHVR0Egq3ywbxEUEChKM49qj8TA63gFHAmbDy7rDtjtXE8AmhPEgRQ7Ou6IgCSDWnC2AokI/RwCGM+Bz0l+JXjV1iK2kkOlThT36R8CAaAQhwjp2WESeFylx7p99x1fWQ4o7pwpXIQhwZxmMPosqtP9hknxdMDnwQ4LDNwlOkJuwE87dj8ZlArlYGl5lkZBgpAi/RRP4Kbm8hiKVvAPurLtLUfHmn95CS0ZVR69y74PRFQPsioSdc43dPuEBhVwl8TyLsKxJOuR4MEPa2low4IBordvtwbY3Nx5nsjUwUgmg6LVMfKQXasrdr08F8Ixnri2w4olqu55KgfY48Nc7zAZytReUqS/dPVgJwvNqFlFmS98/ihbaj5QqAafZfSYIpgx2wBEbEVyLaNbmVDpZmHbugyZeLPsffCWsQ+OnvWyfJDGLx3ISYuQ+00Ij5SWMC9OUX3aVckRqsr4CbRlp1SIO6GE2Jl9C7eMCAwEAAQ==";
        PublicKey publicKey = createPublicKey(pub);
        System.out.println(publicKey);
    }
}
