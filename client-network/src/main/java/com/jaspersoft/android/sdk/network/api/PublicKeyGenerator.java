package com.jaspersoft.android.sdk.network.api;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class PublicKeyGenerator {
    private static final String UTF8 = "utf-8";

    private final BigInteger mModulus;
    private final BigInteger mExponent;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public PublicKeyGenerator(String modulus, String exponent) {
         mModulus = new BigInteger(modulus, 16);
         mExponent = new BigInteger(exponent, 16);
    }

    public String generateKey(String text) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(mModulus, mExponent);
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding", "SC");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherData = cipher.doFinal(text.getBytes(UTF8));
            return new String(cipherData, UTF8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
