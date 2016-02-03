package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.TestConstants;
import com.jaspersoft.android.sdk.network.entity.resource.FileLookup;
import com.jaspersoft.android.sdk.service.data.report.FileResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FileResourceMapperTest {
    @Mock
    FileLookup mFileLookup;

    private FileResourceMapper mapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mapper = new FileResourceMapper(TestConstants.DATE_TIME_FORMAT);
        mFileLookup = MockResourceFactory.mockCommonFields(mFileLookup);
    }

    @Test
    public void should_map_file_lookup() throws Exception {
        mockFileLookup();
        FileResource resource = mapper.transform(mFileLookup);
        assertThat(resource.getType(), is(FileResource.Type.pdf));
    }

    private void mockFileLookup() {
        when(mFileLookup.getType()).thenReturn("pdf");
        when(mFileLookup.getResourceType()).thenReturn("file");
    }
}