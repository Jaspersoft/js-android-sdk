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

package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class RepositoryDestinationTest {
    @Rule
    public ExpectedException expected = none();

    @Test
    public void builder_should_not_accept_null_for_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository folder uri should not be null");
        new RepositoryDestination.Builder().withFolderUri(null);
    }

    @Test
    public void builder_should_not_accept_null_for_default_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository folder uri should not be null");
        new RepositoryDestination.Builder().withDefaultReportOutputFolderURI(null);
    }

   @Test
    public void builder_should_not_accept_null_for_output_local_path() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Output local path should not be null");
        new RepositoryDestination.Builder().withOutputLocalFolder(null);
    }

    @Test
    public void should_not_allow_invalid_timestampPattern() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Unparseable timestamp 'ooo'");
        new RepositoryDestination.Builder().withTimestampPattern("ooo").build();
    }

    @Test
    public void should_use_default_folder_uri_if_one_not_supplied() throws Exception {
        RepositoryDestination destination = new RepositoryDestination.Builder().build();
        assertThat(destination.getUseDefaultReportOutputFolderURI(), is(true));
    }
}