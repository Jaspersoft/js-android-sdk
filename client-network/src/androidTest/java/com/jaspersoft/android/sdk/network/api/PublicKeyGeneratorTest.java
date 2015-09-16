package com.jaspersoft.android.sdk.network.api;

import android.test.AndroidTestCase;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class PublicKeyGeneratorTest extends AndroidTestCase {
    private final static String PUBLIC_EXPONENT = "10001";
    private final static String MODULUS = "95d334976c4cd8ee44dd9b1d756ea7eba34a8b6b9f5903d4480b6a38dcdaa9e602590bdd871fb618f9a88467c4da16085a8104e8488203459358a049ad435eeaa5a600447d3c55caa362c6e10eea0081f2c6a60e99e8ca02c98948d36dad2b00146ef5054f638f4143f2e4ccefb17630fe658e995406464d4f84af2269dcdb81";

    public void testGenerateKey() throws Exception {
        PublicKeyGenerator keyGenerator = new PublicKeyGenerator(MODULUS, PUBLIC_EXPONENT);
        String encryptedPass = keyGenerator.generateKey("1234");
        assertTrue(encryptedPass != null);
    }
}
