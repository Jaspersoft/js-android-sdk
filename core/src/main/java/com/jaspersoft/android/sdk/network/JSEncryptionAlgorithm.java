/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * Encrypt the passed value using RSA/NONE/NoPadding algorithm with the bouncy castle as a provider. Used by network layer API password encryption task.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public final class JSEncryptionAlgorithm {
    private static final String UTF_8 = "UTF-8";
    private static final int RADIX_16 = 16;

    private final Provider mProvider;

    private JSEncryptionAlgorithm(Provider provider) {
        mProvider = provider;
    }

    public static JSEncryptionAlgorithm create() {
        return create(null);
    }

    public static JSEncryptionAlgorithm create(Provider provider) {
        return new JSEncryptionAlgorithm(provider);
    }

    /**
     * Accept public data required for initialization of {@link RSAPublicKeySpec}.
     * With generated public/private raw text encrypted.
     *
     * @param modulus the modulus
     * @param exponent the public exponent
     * @param text raw text target for encryption
     * @return if encryption was success returns encrypted value otherwise raw one
     */
    public String encrypt(@NotNull String modulus, @NotNull String exponent, @NotNull String text) {
        Utils.checkNotNull(modulus, "Modulus should not be null");
        Utils.checkNotNull(exponent, "Exponent should not be null");
        Utils.checkNotNull(text, "Raw text should not be null");

        try {
            PublicKey publicKey = createPublicKey(modulus, exponent);

            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            String utfPass = URLEncoder.encode(text, UTF_8);
            byte[] encryptedUtfPass = cipher.doFinal(utfPass.getBytes());

            return byteArrayToHexString(encryptedUtfPass);
        } catch (Exception ex) {
            // Oops we failed to cipher text remove original
           return text;
        }
    }

    private Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        if (mProvider == null) {
            return Cipher.getInstance("RSA/NONE/NoPadding", "BC");
        }
        return Cipher.getInstance("RSA/NONE/NoPadding", mProvider);
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
     * @param byteArr The byte array to convert.
     * @return The hex string.
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
