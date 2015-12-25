/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        assertThat("Failed to decrypt result", RESULT, is(encryptedPass));
    }
}
