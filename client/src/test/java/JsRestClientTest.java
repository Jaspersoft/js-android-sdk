import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class JsRestClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JsRestClient jsRestClient = new JsRestClient() {
        @Override
        protected int copyResponseToFile(ClientHttpResponse response, File file) {
            // do nothing
            return 0;
        }
    };

    private final static String resourceUri = "/Samples/AllAccounts";
    private final static String folderUri = "/reports/samples";
    private final static String inputControlUri = "/Samples/AllAccounts/inputControl";
    private final static String datasourceUri = "/datasource";
    private final String uuid = "d7bf6c9-9077-41f7-a2d4-8682e74b637e";
    private final static String attachmentName = "img_0_0_0";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    //---------------------------------------------------------------------
    // The Resource Service
    //---------------------------------------------------------------------

    @Test
    public void test_getResource() {
        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_RESOURCE_URI + resourceUri;

        ResourceDescriptor expectedDescriptor = mock(ResourceDescriptor.class);
        when(restTemplate.getForObject(fullUri, ResourceDescriptor.class)).thenReturn(expectedDescriptor);

        ResourceDescriptor actualDescriptor = jsRestClient.getResource(resourceUri);

        verify(restTemplate).getForObject(fullUri, ResourceDescriptor.class);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void test_modifyResource() {
        ResourceDescriptor resourceDescriptor = mock(ResourceDescriptor.class);

        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_RESOURCE_URI + resourceDescriptor.getUriString();

        jsRestClient.modifyResource(resourceDescriptor);
        verify(restTemplate).postForLocation(fullUri, resourceDescriptor);
    }

    @Test
    public void test_deleteResource() {
        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_RESOURCE_URI + resourceUri;

        jsRestClient.deleteResource(resourceUri);
        verify(restTemplate).delete(fullUri);
    }

    //---------------------------------------------------------------------
    // The Resources Service
    //---------------------------------------------------------------------

    @Test
    public void test_getResourcesList() {
        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_RESOURCES_URI + folderUri;

        List<ResourceDescriptor> expectedResourcesList = (List<ResourceDescriptor>) mock(List.class);

        ResourcesList resourcesList = mock(ResourcesList.class);
        when(resourcesList.getResourceDescriptors()).thenReturn(expectedResourcesList);

        when(restTemplate.getForObject(fullUri, ResourcesList.class)).thenReturn(resourcesList);

        List<ResourceDescriptor> actualResourcesList = jsRestClient.getResourcesList(folderUri);

        verify(restTemplate).getForObject(fullUri, ResourcesList.class);
        assertEquals(expectedResourcesList, actualResourcesList);
    }

    @Test
    public void test_getResourcesList_matchingParameters() {
        String query = "AllAccounts";
        String type = "reportUnit";
        Boolean recursive = true;
        Integer limit = 10;

        String uriVariablesTemplate = "?q={query}&type={type}&recursive={recursive}&limit={limit}";
        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_RESOURCES_URI + folderUri + uriVariablesTemplate;

        List<ResourceDescriptor> expectedResourcesList = (List<ResourceDescriptor>) mock(List.class);

        ResourcesList resourcesList = mock(ResourcesList.class);
        when(resourcesList.getResourceDescriptors()).thenReturn(expectedResourcesList);

        when(restTemplate.getForObject(fullUri, ResourcesList.class, query, type, recursive, limit))
                .thenReturn(resourcesList);

        List<ResourceDescriptor> actualResourcesList = jsRestClient.getResourcesList(folderUri, query, type, recursive, limit);

        verify(restTemplate).getForObject(fullUri, ResourcesList.class, query, type, recursive, limit);
        assertEquals(expectedResourcesList, actualResourcesList);
    }

    //---------------------------------------------------------------------
    // The Report Service
    //---------------------------------------------------------------------

    @Test
    public void test_getReportDescriptor() {
        String format = "HTML";
        ResourceDescriptor resourceDescriptor = mock(ResourceDescriptor.class);

        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_REPORT_URI +
                resourceDescriptor.getUriString() + "?IMAGES_URI=./&RUN_OUTPUT_FORMAT={format}";

        ReportDescriptor expectedDescriptor = mock(ReportDescriptor.class);

        ResponseEntity<ReportDescriptor> responseEntity = (ResponseEntity<ReportDescriptor>) mock(ResponseEntity.class);

        when(responseEntity.getBody()).thenReturn(expectedDescriptor);

        when(restTemplate.exchange(eq(fullUri), eq(HttpMethod.PUT), isA(HttpEntity.class), eq(ReportDescriptor.class), eq(format)))
                .thenReturn(responseEntity);

        ReportDescriptor actualDescriptor = jsRestClient.getReportDescriptor(resourceDescriptor, format);

        verify(restTemplate).exchange(eq(fullUri), eq(HttpMethod.PUT), isA(HttpEntity.class), eq(ReportDescriptor.class), eq(format));
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void test_getReportAttachment() {
        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_REPORT_URI + "/{uuid}?file={name}";

        byte[] expectedAttachment = new byte[10];

        ResponseEntity<byte[]> responseEntity = (ResponseEntity<byte[]>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(expectedAttachment);

        when(restTemplate.exchange(eq(fullUri), eq(HttpMethod.GET), isA(HttpEntity.class), eq(byte[].class), eq(uuid), eq(attachmentName)))
                .thenReturn(responseEntity);

        byte[] actualAttachment = jsRestClient.getReportAttachment(uuid, attachmentName);

        verify(restTemplate).exchange(eq(fullUri), eq(HttpMethod.GET), isA(HttpEntity.class), eq(byte[].class), eq(uuid), eq(attachmentName));
        assertEquals(expectedAttachment, actualAttachment);
    }

    @Test
    public void test_saveReportAttachmentToFile() {
        String fullUri = jsRestClient.getRestServicesUrl() + JsRestClient.REST_REPORT_URI + "/{uuid}?file={name}";

        ClientHttpRequestFactory factory = mock(ClientHttpRequestFactory.class);
        when(restTemplate.getRequestFactory()).thenReturn(factory);

        ResponseErrorHandler errorHandler = mock(ResponseErrorHandler.class);
        when(restTemplate.getErrorHandler()).thenReturn(errorHandler);

        UriTemplate uriTemplate = new UriTemplate(fullUri);
        URI expanded = uriTemplate.expand(uuid, attachmentName);

        ClientHttpRequest request = mock(ClientHttpRequest.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);

        InputStream inputStream = mock(InputStream.class);

        File file = mock(File.class);

        try {
            when(factory.createRequest(expanded, HttpMethod.GET)).thenReturn(request);
            when(request.execute()).thenReturn(response);

            jsRestClient.saveReportAttachmentToFile(uuid, attachmentName, file);
            verify(restTemplate).getRequestFactory();
            verify(factory).createRequest(expanded, HttpMethod.GET);
            verify(response).close();
        } catch (IOException ex) {
            throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
        }
    }

    @Test
    public void test_getInputControlWithQueryData() {
        List<ResourceParameter> params = new ArrayList<ResourceParameter>();
        params.add(mock(ResourceParameter.class));
        params.add(mock(ResourceParameter.class));

        StringBuilder fullUri = new StringBuilder();
        fullUri.append(jsRestClient.getRestServicesUrl()).append(JsRestClient.REST_RESOURCE_URI).append(inputControlUri);
        fullUri.append("?IC_GET_QUERY_DATA=").append(datasourceUri);

        for(ResourceParameter parameter : params) {
            fullUri.append(parameter.isListItem() ? "&PL_" : "&P_");
            fullUri.append(parameter.getName()).append("=").append(parameter.getValue());
        }

        ResourceDescriptor expectedDescriptor = mock(ResourceDescriptor.class);
        when(restTemplate.getForObject(fullUri.toString(), ResourceDescriptor.class)).thenReturn(expectedDescriptor);

        ResourceDescriptor actualDescriptor = jsRestClient.getInputControlWithQueryData(inputControlUri, datasourceUri, params);

        verify(restTemplate).getForObject(fullUri.toString(), ResourceDescriptor.class);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void test_getInputControlQueryData() {
        List<ResourceParameter> params = new ArrayList<ResourceParameter>();
        params.add(mock(ResourceParameter.class));
        params.add(mock(ResourceParameter.class));


        JsRestClient spyJsRestClient = spy(jsRestClient);
        ResourceDescriptor resourceDescriptor = mock(ResourceDescriptor.class);
        when(spyJsRestClient.getInputControlWithQueryData(inputControlUri, datasourceUri, params)).thenReturn(resourceDescriptor);

        ResourceProperty queryDataProperty = mock(ResourceProperty.class);

        List<ResourceProperty> queryData = new ArrayList<ResourceProperty>();
        queryData.add(mock(ResourceProperty.class));
        queryData.add(mock(ResourceProperty.class));

        when(queryDataProperty.getProperties()).thenReturn(queryData);

        when(resourceDescriptor.getPropertyByName(ResourceDescriptor.PROP_QUERY_DATA)).thenReturn(queryDataProperty);

        List<ResourceProperty> listOfValues = jsRestClient.getInputControlQueryData(inputControlUri, datasourceUri, params);

        verify(spyJsRestClient).getInputControlWithQueryData(inputControlUri, datasourceUri, params);
        assertNotNull(listOfValues);
    }
}
