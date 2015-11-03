package com.jaspersoft.android.sdk.network.api;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JSEncryptionAlgorithmTest {
    private final static String PUBLIC_EXPONENT = "10001";
    private final static String MODULUS = "8964f27abbbde8f903c1a63670d98533b307166aac1f4cbaae025337a31ca032b2675196e89d0da4115d56adfa6965a2e695f073a8b587f47b9e392a7514d6895b6347bd4c14a43f4127841fa93008ba780bafac49d03954dad954857c6de8af3278320cce1a4a43f5c411a911280a05b71c0ec11a66b2e942c152ccecb564cf";
    private final static String RESULT = "5f3138592ee53d7956e10b2a40ceae2849cb161e27b0b82590348490d13f20345771f4184209b98230ad8d3d7ea752d33ce8db5bf8359c76b67e51964a21fbbedc8ad6b21a3c0cf2c680ebf31ad44ad84802d8d5f56bcda21c5d2f8004f0354b21a07d6e5e33e5784fc8fe29a1552cbdd7e47d2b01e2e26939616b8d0927d492";

    @Test
    public void testGenerateKey() throws Exception {
        JSEncryptionAlgorithm keyGenerator = JSEncryptionAlgorithm.
                create(new BouncyCastleProvider());
        String encryptedPass = keyGenerator.encrypt(MODULUS, PUBLIC_EXPONENT, "superuser");
        assertTrue(RESULT.equals(encryptedPass));
    }
}
