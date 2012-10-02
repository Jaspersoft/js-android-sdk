/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jasperforge.org/projects/androidsdk
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client;

import com.google.inject.Inject;
import com.jaspersoft.android.sdk.client.oxm.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import roboguice.util.Ln;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * The central class that provides a set of convenient methods to interact with the JasperReports Server REST API
 * and does mapping of the returned data to object model.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class JsRestClient {

    public final static String REST_SERVICE_URI = "/rest";
    public final static String REST_RESOURCE_URI = "/resource";
    public final static String REST_RESOURCES_URI = "/resources";
    public final static String REST_REPORT_URI = "/report";

    @Inject
    private RestTemplate restTemplate;
    private JsServerProfile jsServerProfile;
    private String restServiceUrl;


    public String getRestServiceUrl() {
        return restServiceUrl;
    }

    public JsServerProfile getServerProfile() {
        return jsServerProfile;
    }

    public void setServerProfile(JsServerProfile serverProfile) {
        this.jsServerProfile = serverProfile;
        this.restServiceUrl = serverProfile.getServerUrl() + REST_SERVICE_URI;

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(serverProfile.getUsernameWithOrgId(), serverProfile.getPassword());
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(authScope, credentials);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
    }

    //---------------------------------------------------------------------
    // The Resource Service
    //---------------------------------------------------------------------


    /**
     * Gets the resource descriptor for the resource with specified URI.
     *
     * @param uri repository URI (i.e. /reports/samples/)
     * @return the ResourceDescriptor value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceDescriptor getResource(String uri) throws RestClientException {
        String fullUri = restServiceUrl + REST_RESOURCE_URI + uri;
        return restTemplate.getForObject(fullUri, ResourceDescriptor.class);
    }

    /**
     * Modifies the resource with specified ResourceDescriptor
     *
     * @param resourceDescriptor ResourceDescriptor of resource being modified
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public void modifyResource(ResourceDescriptor resourceDescriptor) throws RestClientException {
        String fullUri = restServiceUrl + REST_RESOURCE_URI + resourceDescriptor.getUriString();
        restTemplate.postForLocation(fullUri, resourceDescriptor);
    }

    /**
     * Deletes the resource with the specified URI
     *
     * @param uri repository URI (i.e. /reports/samples/)
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public void deleteResource(String uri) throws RestClientException {
        String fullUri = restServiceUrl + REST_RESOURCE_URI + uri;
        restTemplate.delete(fullUri);
    }


    //---------------------------------------------------------------------
    // The Resources Service
    //---------------------------------------------------------------------


    /**
     * Gets the list of resource descriptors for the resources available in the specified repository URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (i.e. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type      Match only resources of the given type (can be <code>null</code>)
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */

    public List<ResourceDescriptor> getResourcesList(String uri, String query, String type, Boolean recursive, Integer limit) throws RestClientException {
        String uriVariablesTemplate = "?q={query}&type={type}&recursive={recursive}&limit={limit}";
        String fullUri = restServiceUrl + REST_RESOURCES_URI + uri + uriVariablesTemplate;
        ResourcesList resourcesList = restTemplate.getForObject(fullUri, ResourcesList.class, query, type, recursive, limit);
        return resourcesList.getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for all resources available in the specified repository URI.
     *
     * @param uri repository URI (i.e. /reports/samples/)
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public List<ResourceDescriptor> getResourcesList(String uri) throws RestClientException {
        String fullUri = restServiceUrl + REST_RESOURCES_URI + uri;
        ResourcesList resourcesList = restTemplate.getForObject(fullUri, ResourcesList.class);
        return resourcesList.getResourceDescriptors();
    }


    //---------------------------------------------------------------------
    // The Report Service
    //---------------------------------------------------------------------


    /**
     * Runs the report and generates the specified output. The response contains report descriptor
     * with the ID of the saved output for downloading later with a GET request.
     *
     * @param resourceDescriptor resource descriptor of this report
     * @param format The format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV,
     *               XML, JRPRINT. The Default is PDF.
     * @return ReportDescriptor
     * @throws RestClientException
     */
    public ReportDescriptor getReportDescriptor(ResourceDescriptor resourceDescriptor, String format) throws RestClientException {
        String fullUri = restServiceUrl + REST_REPORT_URI + resourceDescriptor.getUriString() + "?IMAGES_URI=./&RUN_OUTPUT_FORMAT={format}";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(singletonList(MediaType.TEXT_XML));
        HttpEntity<ResourceDescriptor> requestEntity = new HttpEntity<ResourceDescriptor>(resourceDescriptor, requestHeaders);
        ResponseEntity<ReportDescriptor> entity = restTemplate.exchange(fullUri, HttpMethod.PUT, requestEntity, ReportDescriptor.class, format);
        return entity.getBody();
    }

    /**
     * Downloads specified report attachment, once a report has been generated, and keeps it in memory as a byte array.
     *
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URI is visible only to the currently logged in user.
     * @param name One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @return Attachment file as byte array strored in memory.
     * @throws RestClientException
     */
    public byte[] getReportAttachment(String uuid, String name) throws RestClientException {
        String fullUri = restServiceUrl + REST_REPORT_URI + "/{uuid}?file={name}";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(singletonList(MediaType.APPLICATION_OCTET_STREAM));
        ResponseEntity<byte[]> entity = restTemplate.exchange(fullUri, HttpMethod.GET, new HttpEntity<byte[]>(requestHeaders), byte[].class, uuid, name);
        return entity.getBody();
    }

    /**
     * Downloads specified report attachment, once a report has been generated and saves it in the specified file.
     *
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URI is visible only to the currently logged in user.
     * @param name One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @param file The file in which the attachment will be saved.
     * @throws RestClientException
     */
    public void saveReportAttachmentToFile(String uuid, String name, File file) throws RestClientException {
        String fullUri = restServiceUrl + REST_REPORT_URI + "/{uuid}?file={name}";
        UriTemplate uriTemplate = new UriTemplate(fullUri);
        URI expanded = uriTemplate.expand(uuid, name);

        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = restTemplate.getRequestFactory().createRequest(expanded, HttpMethod.GET);
            response = request.execute();
            if (restTemplate.getErrorHandler().hasError(response)) {
                restTemplate.getErrorHandler().handleError(response);
            }
//            FileCopyUtils.copy(response.getBody(), new FileOutputStream(file));
            copyResponseToFile(response, file);
        } catch (IOException ex) {
            throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
        } finally {
            if (response != null) response.close();
        }
    }

    /**
     * Gets the resource descriptor of a query-based input control that contains query data
     * according to specified parameters.
     *
     * @param uri repository URI of the input control
     * @param datasourceUri repository URI of a datasource for the control
     * @param params parameters for the input control (can be <code>null</code>)
     * @return the ResourceDescriptor of a query-based input control that contains query data
     * @throws RestClientException
     */
    public ResourceDescriptor getInputControlWithQueryData(String uri, String datasourceUri, List<ResourceParameter> params) throws RestClientException {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(restServiceUrl).append(REST_RESOURCE_URI).append(uri);
        fullUri.append("?IC_GET_QUERY_DATA=").append(datasourceUri);
        if (params != null) {
            for(ResourceParameter parameter : params) {
                fullUri.append(parameter.isListItem() ? "&PL_" : "&P_");
                fullUri.append(parameter.getName()).append("=").append(parameter.getValue());
            }
        }
        return restTemplate.getForObject(fullUri.toString(), ResourceDescriptor.class);
    }

    /**
     * Gets the query data of a query-based input control, according to specified parameters.
     *
     * @param uri repository URI of the input control
     * @param datasourceUri repository URI of a datasource for the control
     * @param params parameters for the input control (can be <code>null</code>)
     * @return The query data as list of ResourceProperty objects
     * @throws RestClientException
     */
    public List<ResourceProperty> getInputControlQueryData(String uri, String datasourceUri, List<ResourceParameter> params) throws RestClientException {
        ResourceDescriptor descriptor = getInputControlWithQueryData(uri, datasourceUri, params);
        ResourceProperty queryDataProperty = descriptor.getPropertyByName(ResourceDescriptor.PROP_QUERY_DATA);
        
        List<ResourceProperty> listOfValues = new ArrayList<ResourceProperty>();

        if (queryDataProperty != null) {
            List<ResourceProperty> queryData = queryDataProperty.getProperties();
            // rows
            for (ResourceProperty queryDataRow : queryData) {
                ResourceProperty property = new ResourceProperty();
                property.setName(queryDataRow.getValue());
                //cols
                StringBuilder value = new StringBuilder();
                for(ResourceProperty queryDataCol : queryDataRow.getProperties()) {
                    if(ResourceDescriptor.PROP_QUERY_DATA_ROW_COLUMN.equals(queryDataCol.getName())) {
                        if (value.length() > 0) value.append(" | ");
                        value.append(queryDataCol.getValue());
                    }
                }
                property.setValue(value.toString());
                listOfValues.add(property);
            }
        }

        return listOfValues;
    }

    protected int copyResponseToFile(ClientHttpResponse response, File file) throws IOException {
        File parentFolder = file.getParentFile();
        if (parentFolder != null && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Ln.e("Unable to create folder %s", file.getParentFile());
        }
        return FileCopyUtils.copy(response.getBody(), new FileOutputStream(file));
    }

}