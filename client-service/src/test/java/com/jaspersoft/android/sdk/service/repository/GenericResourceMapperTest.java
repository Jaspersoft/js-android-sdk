package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.repository.GenericResource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class GenericResourceMapperTest {
    public static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());
    @Mock
    ServerInfo mServerInfo;
    @Mock
    ResourceLookup mResourceLookup;

    private GenericResourceMapper objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new GenericResourceMapper();

        when(mResourceLookup.getCreationDate()).thenReturn("2013-10-03 16:32:05");
        when(mResourceLookup.getUpdateDate()).thenReturn("2013-11-03 16:32:05");
        when(mResourceLookup.getResourceType()).thenReturn("reportUnit");
        when(mResourceLookup.getDescription()).thenReturn("description");
        when(mResourceLookup.getLabel()).thenReturn("label");
    }

    @Test
    public void testTransform() throws Exception {
        long creationTime = DATE_FORMAT.parse("2013-10-03 16:32:05").getTime();
        long updateTime = DATE_FORMAT.parse("2013-11-03 16:32:05").getTime();

        GenericResource resource = objectUnderTest.transform(mResourceLookup, DATE_FORMAT);
        assertThat(resource.getCreationDate().getTime(), is(creationTime));
        assertThat(resource.getUpdateDate().getTime(), is(updateTime));
        assertThat(resource.getDescription(), is("description"));
        assertThat(resource.getLabel(), is("label"));
        assertThat(resource.getResourceType(), is(ResourceType.reportUnit));
    }

    @Test
    public void testTransformResourceLookupCollection() {
        ResourceLookup mockResourceLookupOne = mock(ResourceLookup.class);
        ResourceLookup mockResourceLookupTwo = mock(ResourceLookup.class);

        List<ResourceLookup> lookups = new ArrayList<ResourceLookup>(5);
        lookups.add(mockResourceLookupOne);
        lookups.add(mockResourceLookupTwo);

        Collection<GenericResource> resourcesCollection = objectUnderTest.transform(lookups, DATE_FORMAT);

        assertThat(resourcesCollection.toArray()[0], is(instanceOf(GenericResource.class)));
        assertThat(resourcesCollection.toArray()[1], is(instanceOf(GenericResource.class)));
        assertThat(resourcesCollection.size(), is(2));
    }
}