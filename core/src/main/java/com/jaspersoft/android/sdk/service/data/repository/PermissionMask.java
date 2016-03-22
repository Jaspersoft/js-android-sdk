package com.jaspersoft.android.sdk.service.data.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public enum PermissionMask {
    NO_ACCESS(0),
    ADMINISTER(1),
    READ_ONLY(2),
    READ_WRITE(6),
    READ_DELETE(18),
    READ_WRITE_DELETE(30),
    EXECUTE_ONLY(32);

    private final int mMask;

    PermissionMask(int mask) {
        mMask = mask;
    }

    public int getMask() {
        return mMask;
    }

    public static PermissionMask fromRawValue(int mask) {
        Map<Integer, PermissionMask> masks = new HashMap<>();
        masks.put(NO_ACCESS.getMask(), NO_ACCESS);
        masks.put(ADMINISTER.getMask(), ADMINISTER);
        masks.put(READ_ONLY.getMask(), READ_ONLY);
        masks.put(READ_WRITE.getMask(), READ_WRITE);
        masks.put(READ_DELETE.getMask(), READ_DELETE);
        masks.put(READ_WRITE_DELETE.getMask(), READ_WRITE_DELETE);
        masks.put(EXECUTE_ONLY.getMask(), EXECUTE_ONLY);

        PermissionMask permissionMask = masks.get(mask);
        if (permissionMask == null) {
            throw new IllegalArgumentException("Undefined type of mask: '" + mask + "'");
        }

        return permissionMask;
    }
}
