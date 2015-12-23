/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.data.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerVersion implements Comparable<ServerVersion> {
    private final String versionName;
    private final double versionCode;

    public static final ServerVersion v5_5 = new ServerVersion("5.5", 5.5d);
    public static final ServerVersion v5_6 = new ServerVersion("5.6", 5.6d);
    public static final ServerVersion v5_6_1 = new ServerVersion("5.6.1", 5.61d);
    public static final ServerVersion v6 = new ServerVersion("6.0", 6d);
    public static final ServerVersion v6_0_1 = new ServerVersion("6.0.1", 6.01d);
    public static final ServerVersion v6_1 = new ServerVersion("6.1", 6.1d);
    public static final ServerVersion v6_1_1 = new ServerVersion("6.1.1", 6.11d);
    public static final ServerVersion v6_2 = new ServerVersion("6.2", 6.2d);

    @TestOnly
    ServerVersion(String versionName, double versionCode) {
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    @NotNull
    public static ServerVersion valueOf(@NotNull String version) {
        double code = VersionParser.toDouble(version);
        if (code == -1) {
            throw new IllegalArgumentException(String.format("Supplied version '%s' invalid", version));
        }
        return new ServerVersion(version, code);
    }

    public boolean greaterThan(ServerVersion other) {
        return this.compareTo(other) == 1;
    }

    public boolean greaterThanOrEquals(ServerVersion other) {
        return greaterThan(other) || equals(other);
    }

    public boolean lessThan(ServerVersion other) {
        return this.compareTo(other) == -1;
    }

    public boolean lessThanOrEquals(ServerVersion other) {
        return lessThan(other) || equals(other);
    }

    @Override
    public int compareTo(ServerVersion other) {
        return Double.compare(versionCode, other.versionCode);
    }

    @Override
    public String toString() {
        return versionName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerVersion)) {
            return false;
        }

        ServerVersion that = (ServerVersion) o;

        if (Double.compare(that.versionCode, versionCode) != 0) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        long temp = Double.doubleToLongBits(versionCode);
        return (int) (temp ^ (temp >>> 32));
    }
}
