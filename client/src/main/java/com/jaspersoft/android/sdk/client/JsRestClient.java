/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
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
import com.jaspersoft.android.sdk.client.oxm.control.InputControl;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlState;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlStateList;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlsList;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParametersList;
import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import roboguice.util.Ln;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

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

    public static final String REST_SERVICES_URI = "/rest";
    public static final String REST_SERVICES_V2_URI = "/rest_v2";
    public static final String REST_RESOURCE_URI = "/resource";
    public static final String REST_RESOURCES_URI = "/resources";
    public static final String REST_REPORT_URI = "/report";
    public static final String REST_REPORTS_URI = "/reports";
    public static final String REST_INPUT_CONTROLS_URI = "/inputControls";
    public static final String REST_VALUES_URI = "/values";
    public static final String REST_SERVER_INFO_URI = "/serverInfo";

    @Inject
    private RestTemplate restTemplate;
    private JsServerProfile jsServerProfile;
    private String restServicesUrl;

    private ServerInfo serverInfo;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getRestServicesUrl() {
        return restServicesUrl;
    }

    public JsServerProfile getServerProfile() {
        return jsServerProfile;
    }

    public void setServerProfile(JsServerProfile serverProfile) {
        this.serverInfo = null;
        this.jsServerProfile = serverProfile;
        this.restServicesUrl = serverProfile.getServerUrl() + REST_SERVICES_URI;

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(serverProfile.getUsernameWithOrgId(), serverProfile.getPassword());
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(authScope, credentials);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
    }

    /**
     * Gets server information details
     *
     * @return the ServerInfo value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     *
     * @since 1.4
     */
    public ServerInfo getServerInfo() throws RestClientException {
        return getServerInfo(false);
    }

    /**
     * Gets server information details
     *
     * @param forceUpdate set to <code>true</code> to force update of the server info
     * @return the ServerInfo value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     *
     * @since 1.4
     */
    public ServerInfo getServerInfo(boolean forceUpdate) throws RestClientException {
        if(forceUpdate || serverInfo == null) {
            String uri = getServerProfile().getServerUrl() + REST_SERVICES_V2_URI + REST_SERVER_INFO_URI;
            try {
                serverInfo = restTemplate.getForObject(uri, ServerInfo.class);
            } catch (HttpStatusCodeException ex) {
                HttpStatus statusCode = ex.getStatusCode();
                if (statusCode == HttpStatus.NOT_FOUND) {
                    serverInfo = new ServerInfo();
                } else {
                    throw ex;
                }
            }
        }

        return serverInfo;
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
        String fullUri = restServicesUrl + REST_RESOURCE_URI + uri;
        return restTemplate.getForObject(fullUri, ResourceDescriptor.class);
    }

    /**
     * Modifies the resource with specified ResourceDescriptor
     *
     * @param resourceDescriptor ResourceDescriptor of resource being modified
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public void modifyResource(ResourceDescriptor resourceDescriptor) throws RestClientException {
        String fullUri = restServicesUrl + REST_RESOURCE_URI + resourceDescriptor.getUriString();
        restTemplate.postForLocation(fullUri, resourceDescriptor);
    }

    /**
     * Deletes the resource with the specified URI
     *
     * @param uri repository URI (i.e. /reports/samples/)
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public void deleteResource(String uri) throws RestClientException {
        String fullUri = restServicesUrl + REST_RESOURCE_URI + uri;
        restTemplate.delete(fullUri);
    }

    //---------------------------------------------------------------------
    // The Resources Service
    //---------------------------------------------------------------------

    /**
     * Gets the list of resource descriptors for all resources available in the specified repository URI.
     *
     * @param uri repository URI (i.e. /reports/samples/)
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public List<ResourceDescriptor> getResourcesList(String uri) throws RestClientException {
        String fullUri = restServicesUrl + REST_RESOURCES_URI + uri;
        ResourcesList resourcesList = restTemplate.getForObject(fullUri, ResourcesList.class);
        return resourcesList.getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for the resources available in the specified repository URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (i.e. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public List<ResourceDescriptor> getResourcesList(String uri, String query, Boolean recursive, Integer limit) throws RestClientException {
        String uriVariablesTemplate = "?q={query}&recursive={recursive}&limit={limit}";
        String fullUri = restServicesUrl + REST_RESOURCES_URI + uri + uriVariablesTemplate;
        ResourcesList resourcesList = restTemplate.getForObject(fullUri, ResourcesList.class, query, recursive, limit);
        return resourcesList.getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for the resources available in the specified repository URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (i.e. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type      Match only resources of the given type
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public List<ResourceDescriptor> getResourcesList(String uri, String query, String type, Boolean recursive,
                                                     Integer limit) throws RestClientException {
        String uriVariablesTemplate = "?q={query}&type={type}&recursive={recursive}&limit={limit}";
        String fullUri = restServicesUrl + REST_RESOURCES_URI + uri + uriVariablesTemplate;
        ResourcesList resourcesList = restTemplate.getForObject(fullUri, ResourcesList.class, query, type, recursive, limit);
        return resourcesList.getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for the resources available in the specified repository URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (i.e. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param types     Match only resources of the given types
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public List<ResourceDescriptor> getResourcesList(String uri, String query, List<String> types, Boolean recursive,
                                                     Integer limit) throws RestClientException {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(restServicesUrl).append(REST_RESOURCES_URI).append(uri);
        fullUri.append("?q={query}&recursive={recursive}&limit={limit}");
        if (types != null) {
            for (String type : types) {
                fullUri.append("&type=").append(type);
            }
        }
        ResourcesList resourcesList = restTemplate.getForObject(fullUri.toString(), ResourcesList.class, query, recursive, limit);
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
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ReportDescriptor getReportDescriptor(ResourceDescriptor resourceDescriptor, String format) throws RestClientException {
        String fullUri = restServicesUrl + REST_REPORT_URI + resourceDescriptor.getUriString() + "?IMAGES_URI=./&RUN_OUTPUT_FORMAT={format}";
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
     * @return Attachment file as byte array stored in memory.
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public byte[] getReportAttachment(String uuid, String name) throws RestClientException {
        String fullUri = restServicesUrl + REST_REPORT_URI + "/{uuid}?file={name}";
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
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public void saveReportAttachmentToFile(String uuid, String name, File file) throws RestClientException {
        String fullUri = restServicesUrl + REST_REPORT_URI + "/{uuid}?file={name}";
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

    //---------------------------------------------------------------------
    // The Report Service v2
    //---------------------------------------------------------------------

    /**
     * Generates the fully qualified report URL to receive all pages report output in HTML format.
     *
     * @param reportUri repository URI of the report
     * @param parameters list of report parameter/input control values
     * @return the fully qualified report URL
     *
     * @since 1.4
     */
    public String generateReportUrl(String reportUri, List<ReportParameter> parameters) {
        return generateReportUrl(reportUri, parameters, 0, "HTML");
    }

    /**
     * Generates the fully qualified report URL to receive all pages report output in specified format.
     *
     * @param reportUri repository URI of the report
     * @param parameters list of report parameter/input control values
     * @param format the format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV, XML.
     * @return the fully qualified report URL
     *
     * @since 1.4
     */
    public String generateReportUrl(String reportUri, List<ReportParameter> parameters, String format) {
        return generateReportUrl(reportUri, parameters, 0, format);
    }

    /**
     * Generates the fully qualified report URL according to specified parameters. The new v2/reports service allows clients
     * to receive report output in a single request-response using this url.
     *
     * @param reportUri repository URI of the report
     * @param parameters list of report parameter/input control values
     * @param page a positive integer value used to output a specific page or 0 to output all pages
     * @param format the format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV, XML.
     * @return the fully qualified report URL
     *
     * @since 1.4
     */
    public String generateReportUrl(String reportUri, List<ReportParameter> parameters, int page, String format) {
        StringBuilder reportUrl = new StringBuilder();
        reportUrl.append(getServerProfile().getServerUrl()).append(REST_SERVICES_V2_URI);
        reportUrl.append(REST_REPORTS_URI).append(reportUri).append(".").append(format);

        if (parameters == null) parameters = new ArrayList<ReportParameter>();
        if(page > 0) parameters.add(new ReportParameter("page", Integer.toString(page)));

        if (!parameters.isEmpty() ) {
            reportUrl.append("?");
            Iterator<ReportParameter> paramIterator = parameters.iterator();
            while (paramIterator.hasNext()) {
                ReportParameter parameter = paramIterator.next();
                Iterator<String> valueIterator = parameter.getValues().iterator();
                while (valueIterator.hasNext()) {
                    try {
                        String value = URLEncoder.encode(valueIterator.next(), "UTF-8");
                        reportUrl.append(parameter.getName()).append("=").append(value);
                        if (paramIterator.hasNext() || valueIterator.hasNext() ) reportUrl.append("&");
                    } catch (UnsupportedEncodingException ex) {
                        throw new IllegalArgumentException(ex);
                    }
                }
            }
        }
        return reportUrl.toString();
    }

    /**
     * Downloads report output, once it has been generated and saves it in the specified file.
     *
     * @param reportUrl the fully qualified report URL
     * @param file The file in which the output will be saved
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     *
     * @since 1.4
     */
    public void saveReportOutputToFile(String reportUrl, File file) throws RestClientException {
        URI uri;
        try {
            uri = new URI(reportUrl);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }

        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = restTemplate.getRequestFactory().createRequest(uri, HttpMethod.GET);
            response = request.execute();
            if (restTemplate.getErrorHandler().hasError(response)) {
                restTemplate.getErrorHandler().handleError(response);
            }
            copyResponseToFile(response, file);
        } catch (IOException ex) {
            throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
        } finally {
            if (response != null) response.close();
        }
    }

    //---------------------------------------------------------------------
    // Input Controls
    //---------------------------------------------------------------------

    /**
     * Gets the resource descriptor of a query-based input control that contains query data
     * according to specified parameters.
     *
     * @param uri repository URI of the input control
     * @param datasourceUri repository URI of a datasource for the control
     * @param params parameters for the input control (can be <code>null</code>)
     * @return the ResourceDescriptor of a query-based input control that contains query data
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceDescriptor getInputControlWithQueryData(String uri, String datasourceUri, List<ResourceParameter> params) throws RestClientException {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(restServicesUrl).append(REST_RESOURCE_URI).append(uri);
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
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
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

    //---------------------------------------------------------------------
    // Input Controls v2
    //---------------------------------------------------------------------

    /**
     * Gets the list of input controls for the report with specified URI
     *
     * @param reportUri repository URI of the report
     * @return a list of input controls
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     *
     * @since 1.4
     */
    public List<InputControl> getInputControlsForReport(String reportUri) throws RestClientException {
        String fullUri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORTS_URI + reportUri
                + REST_INPUT_CONTROLS_URI;
        InputControlsList inputControlsList = restTemplate.getForObject(fullUri, InputControlsList.class);
        return inputControlsList == null ? new ArrayList<InputControl>() : inputControlsList.getInputControls();
    }

    /**
     * Gets the states with updated values for input controls with specified IDs and according to specified parameters
     *
     * @param reportUri repository URI of the report
     * @param ids list of input controls IDs
     * @param selectedValues list of input controls selected values
     * @return a list of the input controls states
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     *
     *  @since 1.4
     */
    public List<InputControlState> getUpdatedInputControlsValues(String reportUri, List<String> ids,
            List<ReportParameter> selectedValues) throws RestClientException {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(jsServerProfile.getServerUrl()).append(REST_SERVICES_V2_URI).append(REST_REPORTS_URI)
                .append(reportUri).append(REST_INPUT_CONTROLS_URI);
        if (!ids.isEmpty()) {
            fullUri.append("/");
            for (String id : ids) {
                fullUri.append(id).append(";");
            }
        }
        fullUri.append(REST_VALUES_URI);

        ReportParametersList parametersList = new ReportParametersList();
        parametersList.setReportParameters(selectedValues);

        InputControlStateList stateList = restTemplate.postForObject(fullUri.toString(), parametersList, InputControlStateList.class);
        return stateList.getInputControlStateList();
    }

    /**
     * Validates controls values on the server side and returns states only for invalid controls
     *
     * @param reportUri repository URI of the report
     * @param inputControls list of input controls that should be validated
     * @return a list of the input controls states
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     *
     * @since 1.4
     */
    public List<InputControlState> validateInputControlsValues(String reportUri,
            List<InputControl> inputControls) throws RestClientException {
        if (inputControls.isEmpty()) {
            return new ArrayList<InputControlState>();
        } else {
            List<String> ids = new ArrayList<String>();
            List<ReportParameter> parameters = new ArrayList<ReportParameter>();
            for(InputControl control : inputControls) {
                ids.add(control.getId());
                parameters.add(new ReportParameter(control.getId(), control.getSelectedValues()));
            }

            List<InputControlState> stateList = getUpdatedInputControlsValues(reportUri, ids, parameters);

            Iterator<InputControlState> iterator = stateList.iterator();
            while(iterator.hasNext()) {
                InputControlState state = iterator.next();
                if(state.getError() == null) {
                    iterator.remove();
                }
            }
            return stateList;
        }
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    protected int copyResponseToFile(ClientHttpResponse response, File file) throws IOException {
        File parentFolder = file.getParentFile();
        if (parentFolder != null && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Ln.e("Unable to create folder %s", file.getParentFile());
        }
        return FileCopyUtils.copy(response.getBody(), new FileOutputStream(file));
    }

}