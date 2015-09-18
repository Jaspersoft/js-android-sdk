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
public final class PasswordEncryption {
    private static final String UTF_8 = "UTF-8";
    public static final int RADIX_16 = 16;

    private final BigInteger mModulus;
    private final BigInteger mExponent;
    private final Provider mProvider;

    private PasswordEncryption(Provider provider, String modulus, String exponent) {
        mModulus = new BigInteger(modulus, RADIX_16);
        mExponent = new BigInteger(exponent, RADIX_16);
        mProvider = provider;
    }

    public static PasswordEncryption create(String modulus, String exponent) {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        return create(provider, modulus, exponent);
    }

    public static PasswordEncryption create(Provider provider, String modulus, String exponent) {
        return new PasswordEncryption(provider, modulus, exponent);
    }

    public String encrypt(String text) {
        try {
            PublicKey publicKey = createPublicKey();

            Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding", mProvider);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            String utfPass = URLEncoder.encode(text, UTF_8);
            byte[] encryptedUtfPass = cipher.doFinal(utfPass.getBytes());

            String hashedInput = byteArrayToHexString(encryptedUtfPass);
            return hashedInput;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private PublicKey createPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", mProvider);
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(mModulus, mExponent);
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

            sb.append(Character.forDigit(high, 16));
            sb.append(Character.forDigit(low, 16));
        }
        return sb.toString();
    }
}
