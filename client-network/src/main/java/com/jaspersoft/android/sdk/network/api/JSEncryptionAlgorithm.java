package com.jaspersoft.android.sdk.network.api;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class JSEncryptionAlgorithm {
    private static final String UTF_8 = "UTF-8";
    private static final int RADIX_16 = 16;

    private final Provider mProvider;

    private JSEncryptionAlgorithm(Provider provider) {
        mProvider = provider;
    }

    public static JSEncryptionAlgorithm create() {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        return create(provider);
    }

    public static JSEncryptionAlgorithm create(Provider provider) {
        return new JSEncryptionAlgorithm(provider);
    }

    public String encrypt(String modulus, String exponent, String text) {
        try {
            PublicKey publicKey = createPublicKey(modulus, exponent);

            Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding", mProvider);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            String utfPass = URLEncoder.encode(text, UTF_8);
            byte[] encryptedUtfPass = cipher.doFinal(utfPass.getBytes());

            return byteArrayToHexString(encryptedUtfPass);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private PublicKey createPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger m = new BigInteger(modulus, RADIX_16);
        BigInteger e = new BigInteger(exponent, RADIX_16);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA", mProvider);
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(m, e);
        return keyFactory.generatePublic(pubKeySpec);
    }

    /**
     * Convert byteArr to hex sting.
     *
     * @param byteArr
     * @return
     */
    private static String byteArrayToHexString(byte[] byteArr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArr.length; i++) {
            byte b = byteArr[i];
            int high = (b & 0xF0) >> 4;
            int low = b & 0x0F;

            sb.append(Character.forDigit(high, RADIX_16));
            sb.append(Character.forDigit(low, RADIX_16));
        }
        return sb.toString();
    }
}
