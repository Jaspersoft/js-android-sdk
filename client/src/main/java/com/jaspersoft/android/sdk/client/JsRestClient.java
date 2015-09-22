/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
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

import android.net.Uri;

import com.jaspersoft.android.sdk.client.oxm.ReportDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceParameter;
import com.jaspersoft.android.sdk.client.oxm.ResourceProperty;
import com.jaspersoft.android.sdk.client.oxm.ResourcesList;
import com.jaspersoft.android.sdk.client.oxm.control.InputControl;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlState;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlStatesList;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlsList;
import com.jaspersoft.android.sdk.client.oxm.report.ExportExecution;
import com.jaspersoft.android.sdk.client.oxm.report.ExportsRequest;
import com.jaspersoft.android.sdk.client.oxm.report.FolderDataResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportDataResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParametersList;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ExecutionRequestAdapter;
import com.jaspersoft.android.sdk.client.oxm.report.option.ReportOption;
import com.jaspersoft.android.sdk.client.oxm.report.option.ReportOptionResponse;
import com.jaspersoft.android.sdk.client.oxm.resource.ReportUnit;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookupSearchCriteria;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookupsList;
import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;
import com.jaspersoft.android.sdk.util.CookieHttpRequestInterceptor;
import com.jaspersoft.android.sdk.util.KeepAliveHttpRequestInterceptor;
import com.jaspersoft.android.sdk.util.LocalesHttpRequestInterceptor;
import com.jaspersoft.android.sdk.util.StaticCacheHelper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonList;

/**
 * The central class that provides a set of convenient methods to interact with the JasperReports Server REST API
 * and does mapping of the returned data to object model.
 *
 * @author Ivan Gadzhega
 * @since 1.0
 */
public class JsRestClient {

    public static final String REST_SERVICES_URI = "/rest";
    public static final String REST_SERVICES_V2_URI = "/rest_v2";
    public static final String REST_RESOURCE_URI = "/resource";
    public static final String REST_RESOURCES_URI = "/resources";
    public static final String REST_REPORT_URI = "/report";
    public static final String REST_REPORT_OPTIONS_URI = "/options";
    public static final String REST_REPORTS_URI = "/reports";
    public static final String REST_INPUT_CONTROLS_URI = "/inputControls";
    public static final String REST_VALUES_URI = "/values";
    public static final String REST_SERVER_INFO_URI = "/serverInfo";
    public static final String REST_REPORT_EXECUTIONS = "/reportExecutions";
    public static final String REST_REPORT_EXPORTS = "/exports";
    public static final String REST_REPORT_STATUS = "/status";
    public static final String REST_THUMBNAILS = "/thumbnails";


    // the timeout in milliseconds until a connection is established
    private int connectTimeout = 15 * 1000;
    // the socket timeout in milliseconds for waiting for data
    private int readTimeout = 120 * 1000;

    private JsServerProfile jsServerProfile;
    private String restServicesUrl;
    private ServerInfo serverInfo;

    private final RestTemplate restTemplate;
    private final SimpleClientHttpRequestFactory requestFactory;
    private final DataType dataType;

    //---------------------------------------------------------------------
    // Factory methods
    //---------------------------------------------------------------------

    public static Builder builder() {
        return new Builder();
    }

    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------

    public JsRestClient() {
        this(new RestTemplate(false), new SimpleClientHttpRequestFactory());
    }

    public JsRestClient(RestTemplate restTemplate) {
        this(restTemplate, new SimpleClientHttpRequestFactory());
    }

    public JsRestClient(RestTemplate restTemplate,
                        SimpleClientHttpRequestFactory factory) {
        this(restTemplate, factory, DataType.XML);
    }

    private JsRestClient(Builder builder) {
        this(builder.restTemplate, new SimpleClientHttpRequestFactory(), builder.dataType);
    }

    private JsRestClient(RestTemplate restTemplate,
                         SimpleClientHttpRequestFactory factory, DataType dataType) {
        this.restTemplate = restTemplate;
        this.requestFactory = factory;
        this.dataType = dataType;
        configureMessageConverters();
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getRestServicesUrl() {
        return restServicesUrl;
    }

    public JsServerProfile getServerProfile() {
        return jsServerProfile;
    }

    public DataType getDataType() {
        return dataType;
    }

    public SimpleClientHttpRequestFactory getRequestFactory() {
        return requestFactory;
    }

    //---------------------------------------------------------------------
    // Timeouts
    //---------------------------------------------------------------------

    /**
     * Set the underlying URLConnection's connect timeout. A timeout value of 0 specifies an infinite timeout.
     *
     * @param timeout the timeout value in milliseconds
     * @since 1.5
     */
    public void setConnectTimeout(int timeout) {
        connectTimeout = timeout;
        updateConnectTimeout();
    }

    /**
     * Set the underlying URLConnection's read timeout. A timeout value of 0 specifies an infinite timeout.
     *
     * @param timeout the timeout value in milliseconds
     * @since 1.5
     */
    public void setReadTimeout(int timeout) {
        readTimeout = timeout;
        updateReadTimeout();
    }

    //---------------------------------------------------------------------
    // Server Profiles & Info
    //---------------------------------------------------------------------

    /**
     * Alternative way to change server profile.
     * This method mutates request factory.
     * This method doesn't mutates interceptors collection.
     *
     * @param serverProfile store of user auth credentials
     */
    public void updateServerProfile(final JsServerProfile serverProfile) {
        this.serverInfo = null;
        this.jsServerProfile = serverProfile;

        // We allow user to set profile to null value
        if (jsServerProfile != null) {
            this.restServicesUrl = serverProfile.getServerUrl() + REST_SERVICES_URI;
            updateRequestFactoryTimeouts();
            restTemplate.setRequestFactory(requestFactory);
        }
    }

    /**
     * Legacy way to change server profile.
     * This method mutates request factory.
     * This method mutates interceptors collection.
     *
     * @param serverProfile store of user auth credentials
     */
    @Deprecated
    public void setServerProfile(final JsServerProfile serverProfile) {
        this.serverInfo = null;
        this.jsServerProfile = serverProfile;

        // We allow user to set profile to null value
        if (jsServerProfile != null) {
            this.restServicesUrl = serverProfile.getServerUrl() + REST_SERVICES_URI;

            updateRequestFactoryTimeouts();
            restTemplate.setRequestFactory(requestFactory);

            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
            interceptors.add(new LocalesHttpRequestInterceptor());
            interceptors.add(new CookieHttpRequestInterceptor(jsServerProfile));
            interceptors.add(new KeepAliveHttpRequestInterceptor());
            restTemplate.setInterceptors(interceptors);
        }
    }

    /**
     * Allows to mutate list of request request interceptors.
     *
     * @param interceptors list of new request interceptors.
     */
    public void setRequestInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        restTemplate.setInterceptors(interceptors);
    }

    /**
     * Gets server information details
     *
     * @return the ServerInfo value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
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
     * @since 1.4
     */
    public ServerInfo getServerInfo(boolean forceUpdate) throws RestClientException {
        if (forceUpdate || serverInfo == null) {
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
     * Gets the single resource lookup for the specified URI.
     *
     * @param uri resource URI (e.g. /reports/samples/)
     * @return the ResourceDescriptor value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceLookup getReportResource(String uri) throws RestClientException {
        String fullUri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_RESOURCES_URI + uri;

        HttpHeaders headers = new HttpHeaders();
        if (dataType == DataType.JSON) {
            headers.add("Accept", "application/repository.reportUnit+json");
            headers.add("Content-Type", "application/json");
        } else if (dataType == DataType.XML) {
            headers.add("Accept", "application/repository.reportUnit+xml");
            headers.add("Content-Type", "application/xml");
        }

        HttpEntity<String> httpEntity = new HttpEntity<String>("", headers);

        ReportUnit resourceLookup = restTemplate.exchange(fullUri,
                HttpMethod.GET, httpEntity, ReportUnit.class).getBody();
        resourceLookup.setResourceType(ResourceLookup.ResourceType.reportUnit);

        return resourceLookup;
    }

    /**
     * Gets the resource descriptor for the resource with specified URI.
     *
     * @param uri resource URI (e.g. /reports/samples/)
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
     * @param uri resource URI (e.g. /reports/samples/)
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
     * Gets the list of resource descriptors for all resources available in the folder specified in the URI.
     *
     * @param uri folder URI (e.g. /reports/samples/)
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public List<ResourceDescriptor> getResourcesList(String uri) throws RestClientException {
        return getResources(uri).getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for all resources available in the folder specified in the URI.
     *
     * @param uri folder URI (e.g. /reports/samples/)
     * @return the ResourcesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourcesList getResources(String uri) throws RestClientException {
        String fullUri = restServicesUrl + REST_RESOURCES_URI + uri;
        return restTemplate.getForObject(fullUri, ResourcesList.class);
    }

    /**
     * Gets the list of resource descriptors for the resources available in the folder specified in the URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the list of ResourceDescriptor values
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public List<ResourceDescriptor> getResourcesList(String uri, String query, Boolean recursive, Integer limit) throws RestClientException {
        return getResources(uri, query, recursive, limit).getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for the resources available in the folder specified in the URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the ResourcesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourcesList getResources(String uri, String query, Boolean recursive, Integer limit) throws RestClientException {
        String uriVariablesTemplate = "?q={query}&recursive={recursive}&limit={limit}";
        String fullUri = restServicesUrl + REST_RESOURCES_URI + uri + uriVariablesTemplate;
        return restTemplate.getForObject(fullUri, ResourcesList.class, query, recursive, limit);
    }

    /**
     * Gets the list of resource descriptors for the resources available in the folder specified in the URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (e.g. /reports/samples/)
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
        return getResources(uri, query, type, recursive, limit).getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for the resources available in the folder specified in the URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type      Match only resources of the given type
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the ResourcesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourcesList getResources(String uri, String query, String type, Boolean recursive, Integer limit) throws RestClientException {
        String uriVariablesTemplate = "?q={query}&type={type}&recursive={recursive}&limit={limit}";
        String fullUri = restServicesUrl + REST_RESOURCES_URI + uri + uriVariablesTemplate;
        return restTemplate.getForObject(fullUri, ResourcesList.class, query, type, recursive, limit);
    }

    /**
     * Gets the list of resource descriptors for the resources available in the folder specified in the URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (e.g. /reports/samples/)
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
        return getResources(uri, query, types, recursive, limit).getResourceDescriptors();
    }

    /**
     * Gets the list of resource descriptors for the resources available in the folder specified in the URI
     * and matching the specified parameters.
     *
     * @param uri       repository URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param types     Match only resources of the given types
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit     Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     * @return the ResourcesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourcesList getResources(String uri, String query, List<String> types, Boolean recursive, Integer limit) throws RestClientException {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(restServicesUrl).append(REST_RESOURCES_URI).append(uri);
        fullUri.append("?q={query}&recursive={recursive}&limit={limit}");
        if (types != null) {
            for (String type : types) {
                fullUri.append("&type=").append(type);
            }
        }
        return restTemplate.getForObject(fullUri.toString(), ResourcesList.class, query, recursive, limit);
    }

    //---------------------------------------------------------------------
    // The Resources Service v2
    //---------------------------------------------------------------------

    /**
     * Retrieves the list of resource lookup objects for the resources contained in the given parent folder
     * and matching the specified parameters.
     *
     * @param folderUri parent folder URI (e.g. /reports/samples/)
     * @param recursive Get resources recursively
     * @param offset    Pagination. Start index for requested page.
     * @param limit     Pagination. Resources count per page.
     * @return the ResourceLookupsList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceLookupsList getResourceLookups(String folderUri, boolean recursive, int offset, int limit) throws RestClientException {
        return getResourceLookups(folderUri, null, null, recursive, offset, limit);
    }

    /**
     * Retrieves the list of resource lookup objects for the resources contained in the given parent folder
     * and matching the specified parameters.
     *
     * @param folderUri parent folder URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description.
     *                  (can be <code>null</code>)
     * @param types     Match only resources of the given types. Multiple resource types allowed.
     *                  (can be <code>null</code>)
     * @param recursive Get resources recursively
     * @param offset    Pagination. Start index for requested page.
     * @param limit     Pagination. Resources count per page.
     * @return the ResourceLookupsList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceLookupsList getResourceLookups(String folderUri, String query, List<String> types, boolean recursive,
                                                  int offset, int limit) throws RestClientException {
        return getResourceLookups(folderUri, query, types, null, recursive, offset, limit);
    }

    /**
     * Retrieves the list of resource lookup objects for the resources contained in the given parent folder
     * and matching the specified parameters.
     *
     * @param folderUri parent folder URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description.
     *                  (can be <code>null</code>)
     * @param types     Match only resources of the given types. Multiple resource types allowed.
     *                  (can be <code>null</code>)
     * @param sortBy    Represents a field in the results to sort by: uri, label, description, type, creationDate,
     *                  updateDate, accessTime, or popularity (based on access events).
     *                  (can be <code>null</code>)
     * @param recursive Get resources recursively
     * @param offset    Pagination. Start index for requested page.
     * @param limit     Pagination. Resources count per page.
     * @return the ResourceLookupsList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceLookupsList getResourceLookups(String folderUri, String query, List<String> types, String sortBy, boolean recursive,
                                                  int offset, int limit) throws RestClientException {
        ResourceLookupSearchCriteria criteria = new ResourceLookupSearchCriteria();
        criteria.setFolderUri(folderUri);
        criteria.setQuery(query);
        criteria.setTypes(types);
        criteria.setSortBy(sortBy);
        criteria.setRecursive(recursive);
        criteria.setOffset(offset);
        criteria.setLimit(limit);
        return getResourceLookups(criteria);
    }

    /**
     * Retrieves the list of resource lookup objects matching the specified search criteria.
     *
     * @param searchCriteria the search criteria
     * @return the ResourceLookupsList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceLookupsList getResourceLookups(ResourceLookupSearchCriteria searchCriteria) throws RestClientException {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(getServerProfile().getServerUrl())
                .append(REST_SERVICES_V2_URI)
                .append(REST_RESOURCES_URI)
                .append("?folderUri={folderUri}")
                .append("&q={query}")
                .append("&sortBy={sortBy}")
                .append("&recursive={recursive}")
                .append("&forceFullPage={forceFullPage}")
                .append("&offset={offset}")
                .append("&limit={limit}")
                .append("&accessType={accessType}");

        if (searchCriteria.getTypes() != null) {
            for (String type : searchCriteria.getTypes()) {
                fullUri.append("&type=").append(type);
            }
        }

        ResponseEntity<ResourceLookupsList> responseEntity = restTemplate.exchange(fullUri.toString(), HttpMethod.GET,
                null, ResourceLookupsList.class, searchCriteria.getFolderUri(), searchCriteria.getQuery(),
                searchCriteria.getSortBy(), searchCriteria.isRecursive(), searchCriteria.isForceFullPage(),
                searchCriteria.getOffset(), searchCriteria.getLimit(), searchCriteria.getAccessType());

        if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            return new ResourceLookupsList();
        } else {
            ResourceLookupsList resourceLookupsList = responseEntity.getBody();
            resourceLookupsList.setResultCount(responseEntity.getHeaders().getFirst("Result-Count"));
            resourceLookupsList.setTotalCount(responseEntity.getHeaders().getFirst("Total-Count"));
            resourceLookupsList.setStartIndex(responseEntity.getHeaders().getFirst("Start-Index"));
            resourceLookupsList.setNextOffset(responseEntity.getHeaders().getFirst("Next-Offset"));
            return resourceLookupsList;
        }
    }

    /**
     * Retrives all data for the root folder
     */
    public FolderDataResponse getRootFolderData() {
        String fullUri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_RESOURCES_URI;
        HttpHeaders headers = new HttpHeaders();
        if (dataType == DataType.JSON) {
            headers.add("Accept", "application/repository.folder+json");
            headers.add("Content-Type", "application/json");
        } else if (dataType == DataType.XML) {
            headers.add("Accept", "application/repository.folder+xml");
            headers.add("Content-Type", "application/xml");
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>("", headers);

        ResponseEntity<FolderDataResponse> responseEntity = restTemplate.exchange(fullUri,
                HttpMethod.GET, httpEntity, FolderDataResponse.class);

        return responseEntity.getBody();
    }

    //---------------------------------------------------------------------
    // The Report Service
    //---------------------------------------------------------------------

    /**
     * Runs the report and generates the specified output. The response contains report descriptor
     * with the ID of the saved output for downloading later with a GET request.
     *
     * @param resourceDescriptor resource descriptor of this report
     * @param format             The format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV,
     *                           XML, JRPRINT. The Default is PDF.
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
        URI expandedUri = uriTemplate.expand(uuid, name);

        downloadFile(expandedUri, file);
    }

    //---------------------------------------------------------------------
    // The Report Service v2
    //---------------------------------------------------------------------

    /**
     * Generates the fully qualified report URL to receive all pages report output in HTML format.
     *
     * @param reportUri  repository URI of the report
     * @param parameters list of report parameter/input control values
     * @return the fully qualified report URL
     * @since 1.4
     */
    public String generateReportUrl(String reportUri, List<ReportParameter> parameters) {
        return generateReportUrl(reportUri, parameters, 0, "HTML");
    }

    /**
     * Generates the fully qualified report URL to receive all pages report output in specified format.
     *
     * @param reportUri  repository URI of the report
     * @param parameters list of report parameter/input control values
     * @param format     the format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV, XML.
     * @return the fully qualified report URL
     * @since 1.4
     */
    public String generateReportUrl(String reportUri, List<ReportParameter> parameters, String format) {
        return generateReportUrl(reportUri, parameters, 0, format);
    }

    /**
     * Generates the fully qualified report URL according to specified parameters. The new v2/reports service allows clients
     * to receive report output in a single request-response using this url.
     *
     * @param reportUri  repository URI of the report
     * @param parameters list of report parameter/input control values
     * @param page       a positive integer value used to output a specific page or 0 to output all pages
     * @param format     the format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV, XML.
     * @return the fully qualified report URL
     * @since 1.4
     */
    public String generateReportUrl(String reportUri, List<ReportParameter> parameters, int page, String format) {
        StringBuilder reportUrl = new StringBuilder();
        reportUrl.append(getServerProfile().getServerUrl()).append(REST_SERVICES_V2_URI);
        reportUrl.append(REST_REPORTS_URI).append(reportUri).append(".").append(format);

        if (parameters == null) parameters = new ArrayList<ReportParameter>();
        if (page > 0) parameters.add(new ReportParameter("page", Integer.toString(page)));

        if (!parameters.isEmpty()) {
            reportUrl.append("?");
            Iterator<ReportParameter> paramIterator = parameters.iterator();
            while (paramIterator.hasNext()) {
                ReportParameter parameter = paramIterator.next();
                Iterator<String> valueIterator = parameter.getValues().iterator();
                while (valueIterator.hasNext()) {
                    try {
                        String value = URLEncoder.encode(valueIterator.next(), "UTF-8");
                        reportUrl.append(parameter.getName()).append("=").append(value);
                        if (paramIterator.hasNext() || valueIterator.hasNext())
                            reportUrl.append("&");
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
     * @param file      The file in which the output will be saved
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.4
     */
    public void saveReportOutputToFile(String reportUrl, File file) throws RestClientException {
        URI uri;
        try {
            uri = new URI(reportUrl);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }

        downloadFile(uri, file);
    }

    //---------------------------------------------------------------------
    // Report Execution Service
    //---------------------------------------------------------------------

    /**
     * Forms and executes url "{server url}/rest_v2/reportExecutions"
     *
     * @param request we delegate to the restTemplate.
     * @return report execution response. Includes executionId for later use.
     * @throws RestClientException
     */
    public ReportExecutionResponse runReportExecution(ReportExecutionRequest request) throws RestClientException {
        checkForProfile();
        request = ExecutionRequestAdapter.newInstance(jsServerProfile.getVersionCode()).adapt(request);
        String url = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORT_EXECUTIONS;
        return restTemplate.postForObject(url, request, ReportExecutionResponse.class);
    }

    /**
     * Sends request with porpose to fetch current export datum.
     *
     * @param executionId Identifies current id of running report.
     * @param request     we delegate to the restTemplate.
     * @return response with all exports datum associated with request.
     * @throws RestClientException
     */
    public ExportExecution runExportForReport(String executionId, ExportsRequest request) throws RestClientException {
        checkForProfile();
        request = ExecutionRequestAdapter.newInstance(jsServerProfile.getVersionCode()).adapt(request);
        URI uri = getExportForReportURI(executionId);
        return restTemplate.postForObject(uri, request, ExportExecution.class);
    }

    /**
     * Generates link for requesting data on specified export resource.
     *
     * @param executionId Identifies current id of running report.
     * @return "{server url}/rest_v2/reportExecutions/{executionId}/exports"
     */
    public URI getExportForReportURI(String executionId) {
        String outputResourceUri = "/{executionId}";
        String fullUri = jsServerProfile.getServerUrl() +
                REST_SERVICES_V2_URI + REST_REPORT_EXECUTIONS +
                outputResourceUri + REST_REPORT_EXPORTS;

        return new UriTemplate(fullUri).expand(executionId, executionId);
    }

    /**
     * Sends request for the current running report for the status check.
     *
     * @param executionId Identifies current id of running report.
     * @return response which expose current report status.
     */
    public ReportStatusResponse runReportStatusCheck(String executionId) {
        return restTemplate.getForObject(getReportStatusCheckURI(executionId), ReportStatusResponse.class);
    }

    /**
     * Generates link for requesting report execution status.
     *
     * @param executionId Identifies current id of running report.
     * @return "{server url}/rest_v2/reportExecutions/{executionId}/status"
     */
    public URI getReportStatusCheckURI(String executionId) {
        String outputResourceUri = "/{executionId}";

        String fullUri = jsServerProfile.getServerUrl() +
                REST_SERVICES_V2_URI + REST_REPORT_EXECUTIONS +
                outputResourceUri + REST_REPORT_STATUS;

        return new UriTemplate(fullUri).expand(executionId);
    }

    /**
     * Sends request for the current running export for the status check.
     *
     * @param executionId  Identifies current id of running report.
     * @param exportOutput Identifier which refers to current requested export.
     * @return response which expose current export status.
     */
    public ReportStatusResponse runExportStatusCheck(String executionId, String exportOutput) {
        return restTemplate.getForObject(getExportStatusCheckURI(executionId, exportOutput), ReportStatusResponse.class);
    }

    /**
     * Generates link for requesting report execution status.
     *
     * @param executionId  Identifies current id of running report.
     * @param exportOutput Identifier which refers to current requested export.
     * @return "{server url}/rest_v2/reportExecutions/{executionId}/exports/{exportOutput}/status"
     */
    public URI getExportStatusCheckURI(String executionId, String exportOutput) {
        String outputResourceUri = "/{executionId}/exports/{exportOutput}/status";
        String fullUri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORT_EXECUTIONS + outputResourceUri;

        UriTemplate uriTemplate = new UriTemplate(fullUri);
        return uriTemplate.expand(executionId, exportOutput);
    }

    /**
     * Saves resource ouput in file.
     *
     * @param executionId  Identifies current id of running report.
     * @param exportOutput Identifier which refers to current requested export.
     * @param file         The file in which the output will be saved
     * @throws RestClientException
     */
    public void saveExportOutputToFile(String executionId, String exportOutput, File file) throws RestClientException {
        URI outputResourceUri = getExportOutputResourceURI(executionId, exportOutput);
        downloadFile(outputResourceUri, file);
    }

    /**
     * Returns response for export request in order to process InputStream directly.
     *
     * @param executionId  Identifies current id of running report.
     * @param exportOutput Identifier which refers to current requested export.
     * @throws RestClientException
     */
    public ClientHttpResponse getExportOutputResponse(String executionId, String exportOutput) throws RestClientException {
        URI outputResourceUri = getExportOutputResourceURI(executionId, exportOutput);
        return requestFile(outputResourceUri);
    }

    /**
     * Load output data for export on current request.
     *
     * @param executionId  Identifies current id of running report.
     * @param exportOutput Identifier which refers to current requested export.
     * @return Basically it will be 3 cases HTML/JSON/XML types.
     */
    public ReportDataResponse runExportOutputResource(String executionId, String exportOutput) {
        ResponseEntity<String> response = restTemplate.exchange(
                getExportOutputResourceURI(executionId, exportOutput), HttpMethod.GET, null, String.class);

        boolean isFinal;
        boolean hasOutputFinal = response.getHeaders().containsKey("output-final");
        String data = response.getBody();

        if (hasOutputFinal) {
            isFinal = Boolean.valueOf(response.getHeaders().getFirst("output-final"));
        } else {
            // "output-final" header is missing for JRS 5.5 and lower,
            // so we consider request to be not final by default
            isFinal = false;
        }
        return new ReportDataResponse(isFinal, data);
    }

    /**
     * Generates link for requesting report output resource datum.
     *
     * @param executionId  Identifies current id of running report.
     * @param exportOutput Identifier which refers to current requested export.
     * @return "{server url}/rest_v2/reportExecutions/{executionId}/exports/{exportOutput}/outputResource"
     */
    public URI getExportOutputResourceURI(String executionId, String exportOutput) {
        String outputResourceUri = "/{executionId}/exports/{exportOutput}/outputResource";
        String fullUri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORT_EXECUTIONS + outputResourceUri;

        UriTemplate uriTemplate = new UriTemplate(fullUri);
        return uriTemplate.expand(executionId, exportOutput);
    }

    /**
     * Save report in file with specified name.
     *
     * @param executionId    Identifies current id of running report.
     * @param exportOutput   Identifier which refers to current requested export.
     * @param attachmentName Name of attachment we store on JRS side.
     * @param file           The file in which the output will be saved
     * @throws RestClientException
     */
    public void saveExportAttachmentToFile(String executionId, String exportOutput,
                                           String attachmentName, File file) throws RestClientException {
        URI attachmentUri = getExportAttachmentURI(executionId, exportOutput, attachmentName);
        downloadFile(attachmentUri, file);
    }

    /**
     * Generates link for requesting of report export attachemnt data.
     *
     * @param executionId    Identifies current id of running report.
     * @param exportOutput   Identifier which refers to current requested export.
     * @param attachmentName Name of attachment we store on JRS side.
     * @return "{server url}/rest_v2/reportExecutions/{executionId}/exports/{exportOutput}/attachments/{attachment}"
     */
    public URI getExportAttachmentURI(String executionId, String exportOutput, String attachmentName) {
        String attachmentUri = "/{executionId}/exports/{exportOutput}/attachments/{attachment}";
        String fullUri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORT_EXECUTIONS + attachmentUri;
        return new UriTemplate(fullUri).expand(executionId, exportOutput, attachmentName);
    }

    /**
     * Sends request for retrieving report details data.
     *
     * @param executionId Identifies current id of running report.
     * @return response which expose current report details.
     */
    public ReportExecutionResponse runReportDetailsRequest(String executionId) {
        return restTemplate.getForObject(getReportDetailsURI(executionId), ReportExecutionResponse.class);
    }

    /**
     * Generates link for requesting details on specified report.
     *
     * @param executionId Identifies current id of running report.
     * @return "{server url}/rest_v2/reportExecutions/{executionId}"
     */
    public URI getReportDetailsURI(String executionId) {
        String attachmentUri = "/{executionId}";
        String fullUri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORT_EXECUTIONS + attachmentUri;
        return new UriTemplate(fullUri).expand(executionId);
    }

    //---------------------------------------------------------------------
    // Input Controls
    //---------------------------------------------------------------------

    /**
     * Gets the resource descriptor of a query-based input control that contains query data
     * according to specified parameters.
     *
     * @param uri           repository URI of the input control
     * @param datasourceUri repository URI of a datasource for the control
     * @param params        parameters for the input control (can be <code>null</code>)
     * @return the ResourceDescriptor of a query-based input control that contains query data
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     */
    public ResourceDescriptor getInputControlWithQueryData(String uri, String datasourceUri, List<ResourceParameter> params) throws RestClientException {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(restServicesUrl).append(REST_RESOURCE_URI).append(uri);
        fullUri.append("?IC_GET_QUERY_DATA=").append(datasourceUri);
        if (params != null) {
            for (ResourceParameter parameter : params) {
                fullUri.append(parameter.isListItem() ? "&PL_" : "&P_");
                fullUri.append(parameter.getName()).append("=").append(parameter.getValue());
            }
        }
        return restTemplate.getForObject(fullUri.toString(), ResourceDescriptor.class);
    }

    /**
     * Gets the query data of a query-based input control, according to specified parameters.
     *
     * @param uri           repository URI of the input control
     * @param datasourceUri repository URI of a datasource for the control
     * @param params        parameters for the input control (can be <code>null</code>)
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
                for (ResourceProperty queryDataCol : queryDataRow.getProperties()) {
                    if (ResourceDescriptor.PROP_QUERY_DATA_ROW_COLUMN.equals(queryDataCol.getName())) {
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
     * Gets the list of all input controls for the report with specified URI.
     *
     * @param reportUri repository URI of the report
     * @return a list of input controls
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public List<InputControl> getInputControls(String reportUri) throws RestClientException {
        return getInputControlsList(reportUri).getInputControls();
    }

    /**
     * Deprecated due to the invalid selectedValues argument. Starting from 1.10 we are ignoring it.
     * <p/>
     * Gets the list of input controls with specified IDs for the report with specified URI
     * and according to selected values.
     *
     * @param reportUri      repository URI of the report
     * @param controlsIds    list of input controls IDs
     * @param selectedValues list of selected values
     * @return a list of input controls
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    @Deprecated
    public List<InputControl> getInputControls(String reportUri, List<String> controlsIds,
                                               List<ReportParameter> selectedValues) throws RestClientException {
        return getInputControlsList(reportUri, controlsIds, selectedValues).getInputControls();
    }

    /**
     * Gets the list of all input controls for the report with specified URI.
     *
     * @param reportUri repository URI of the report
     * @return the InputControlsList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public InputControlsList getInputControlsList(String reportUri) throws RestClientException {
        return getInputControlsList(reportUri, new ArrayList<String>(), new ArrayList<ReportParameter>());
    }

    /**
     * Deprecated due to the invalid selectedValues argument. Starting from 1.10 we are ignoring it.
     * <p/>
     * Gets the list of input controls with specified IDs for the report with specified URI
     * and according to selected values.
     *
     * @param reportUri      repository URI of the report
     * @param controlsIds    list of input controls IDs
     * @param selectedValues list of selected values
     * @return the InputControlsList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    @Deprecated
    public InputControlsList getInputControlsList(String reportUri, List<String> controlsIds,
                                                  List<ReportParameter> selectedValues) throws RestClientException {
        // generate full url
        String url = generateInputControlsUrl(reportUri, controlsIds, false);
        // execute POST request
        InputControlsList controlsList =
                restTemplate.getForObject(url, InputControlsList.class);
        return (controlsList != null) ? controlsList : new InputControlsList();
    }

    /**
     * Gets the list of states of all input controls for the report with specified URI.
     *
     * @param reportUri repository URI of the report
     * @return the list of the input controls states
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public List<InputControlState> getInputControlsValues(String reportUri) throws RestClientException {
        return getInputControlsValuesList(reportUri).getInputControlStates();
    }

    /**
     * Gets the list of states of input controls with specified IDs for the report with specified URI
     * and according to selected values.
     *
     * @param reportUri      repository URI of the report
     * @param controlsIds    list of input controls IDs
     * @param selectedValues list of selected values
     * @return the list of the input controls states
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public List<InputControlState> getInputControlsValues(String reportUri, List<String> controlsIds,
                                                          List<ReportParameter> selectedValues) throws RestClientException {
        return getInputControlsValuesList(reportUri, controlsIds, selectedValues).getInputControlStates();
    }

    /**
     * Gets the list of states of all input controls for the report with specified URI.
     *
     * @param reportUri repository URI of the report
     * @return the InputControlStatesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public InputControlStatesList getInputControlsValuesList(String reportUri) throws RestClientException {
        return getInputControlsValuesList(reportUri, new ArrayList<String>(), new ArrayList<ReportParameter>());
    }

    /**
     * Gets the list of states of input controls with specified IDs for the report with specified URI
     * and according to selected values.
     *
     * @param reportUri      repository URI of the report
     * @param controlsIds    list of input controls IDs
     * @param selectedValues list of selected values
     * @return the InputControlStatesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public InputControlStatesList getInputControlsValuesList(String reportUri, List<String> controlsIds,
                                                             List<ReportParameter> selectedValues) throws RestClientException {
        // generate full url
        String url = generateInputControlsUrl(reportUri, controlsIds, true);

        Object request = null;
        if (dataType == DataType.JSON) {
            Map<String, Set<String>> map = new HashMap<String, Set<String>>();
            for (ReportParameter reportParameter : selectedValues) {
                map.put(reportParameter.getName(), reportParameter.getValues());
            }
            request = map;
        } else if (dataType == DataType.XML) {
            // add selected values to request
            ReportParametersList parametersList = new ReportParametersList();
            parametersList.setReportParameters(selectedValues);
            request = parametersList;
        }
        if (request == null) {
            throw new IllegalStateException("Failed to create request object");
        }
        // execute POST request
        try {
            return restTemplate.postForObject(url, request, InputControlStatesList.class);
        } catch (HttpMessageNotReadableException exception) {
            return new InputControlStatesList();
        }
    }

    /**
     * Validates the input controls values on the server side and returns states only for invalid controls.
     *
     * @param reportUri     repository URI of the report
     * @param inputControls list of input controls that should be validated
     * @return the list of the input controls states
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.4
     */
    public List<InputControlState> validateInputControlsValues(String reportUri, List<InputControl> inputControls)
            throws RestClientException {
        return validateInputControlsValuesList(reportUri, inputControls).getInputControlStates();
    }

    /**
     * Validates the input controls values on the server side and returns states only for invalid controls.
     *
     * @param reportUri      repository URI of the report
     * @param controlsIds    list of input controls IDs that should be validated
     * @param selectedValues list of selected values for validation
     * @return the list of the input controls states
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public List<InputControlState> validateInputControlsValues(String reportUri, List<String> controlsIds,
                                                               List<ReportParameter> selectedValues) throws RestClientException {
        return validateInputControlsValuesList(reportUri, controlsIds, selectedValues).getInputControlStates();
    }

    /**
     * Validates the input controls values on the server side and returns states only for invalid controls.
     *
     * @param reportUri     repository URI of the report
     * @param inputControls list of input controls that should be validated
     * @return the InputControlStatesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public InputControlStatesList validateInputControlsValuesList(String reportUri, List<InputControl> inputControls)
            throws RestClientException {
        List<String> controlsIds = new ArrayList<String>();
        List<ReportParameter> selectedValues = new ArrayList<ReportParameter>();
        for (InputControl control : inputControls) {
            controlsIds.add(control.getId());
            selectedValues.add(new ReportParameter(control.getId(), control.getSelectedValues()));
        }
        // execute validation request
        return validateInputControlsValuesList(reportUri, controlsIds, selectedValues);
    }

    /**
     * Validates the input controls values on the server side and returns states only for invalid controls.
     *
     * @param reportUri      repository URI of the report
     * @param controlsIds    list of input controls IDs that should be validated
     * @param selectedValues list of selected values for validation
     * @return the InputControlStatesList value
     * @throws RestClientException thrown by RestTemplate whenever it encounters client-side HTTP errors
     * @since 1.6
     */
    public InputControlStatesList validateInputControlsValuesList(String reportUri, List<String> controlsIds,
                                                                  List<ReportParameter> selectedValues) throws RestClientException {
        InputControlStatesList statesList = getInputControlsValuesList(reportUri, controlsIds, selectedValues);
        // remove states without validation errors
        Iterator<InputControlState> iterator = statesList.getInputControlStates().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getError() == null) {
                iterator.remove();
            }
        }
        return statesList;
    }

    /**
     * Method which flashes all stared cookies.
     */
    public static void flushCookies() {
        StaticCacheHelper.clearCache();
    }

    //---------------------------------------------------------------------
    // Thumbnails API
    //---------------------------------------------------------------------

    /**
     * Returns thumbnail image or encoded image of the requested URI without placeholder
     *
     * @param resourceUri Uri of resource
     * @return {serverUrl}/rest_v2/thumbnails/{resourceUri}?defaultAllowed=false
     */
    public String generateThumbNailUri(String resourceUri) {
        return generateThumbNailUri(resourceUri, false);
    }

    /**
     * Returns thumbnail image or encoded image of the requested URI
     *
     * @param resourceUri    Uri of resource
     * @param defaultAllowed If true, a placeholder thumbnail will be provided when no thumbnail is available (default: false)
     * @return {serverUrl}/rest_v2/thumbnails/{resourceUri}?defaultAllowed={allowedFlag}
     */
    public String generateThumbNailUri(String resourceUri, boolean defaultAllowed) {
        return jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_THUMBNAILS
                + resourceUri + "?defaultAllowed=" + Boolean.toString(defaultAllowed);
    }

    //---------------------------------------------------------------------
    // Report options API
    //---------------------------------------------------------------------

    public ReportOptionResponse getReportOptionsList(String reportUnitUri) {
        String uri = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORTS_URI + reportUnitUri + REST_REPORT_OPTIONS_URI;
        return restTemplate.getForObject(uri, ReportOptionResponse.class);
    }

    public ReportOption createReportOption(String reportUnitUri, String optionLabel,
                                           Map<String, Set<String>> controlsValues,
                                           boolean overwrite) {
        String base = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORTS_URI + reportUnitUri + REST_REPORT_OPTIONS_URI;
        Uri uri =  Uri.parse(base)
                .buildUpon()
                .appendQueryParameter("label", optionLabel)
                .appendQueryParameter("overwrite", String.valueOf(overwrite))
                .build();

        if (dataType == DataType.JSON) {
            return restTemplate.postForObject(uri.toString(), controlsValues, ReportOption.class);
        } else if (dataType == DataType.XML) {
            ReportParametersList list = ReportParamsAdapter.INSTANCE.adapt(controlsValues);
            return restTemplate.postForObject(uri.toString(), list, ReportOption.class);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void updateReportOption(String reportUnitUri, String optionId, Map<String, Set<String>> controlsValues) {
        String base = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORTS_URI + reportUnitUri + REST_REPORT_OPTIONS_URI;
        Uri uri = Uri.parse(base)
                .buildUpon()
                .appendPath(optionId)
                .build();

        if (dataType == DataType.JSON) {
            restTemplate.put(uri.toString(), controlsValues);
        } else if (dataType == DataType.XML) {
            ReportParametersList list = ReportParamsAdapter.INSTANCE.adapt(controlsValues);
            restTemplate.put(uri.toString(), list);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void deleteReportOption(String reportUnitUri, String optionId) {
        String base = jsServerProfile.getServerUrl() + REST_SERVICES_V2_URI + REST_REPORTS_URI + reportUnitUri + REST_REPORT_OPTIONS_URI;
        Uri uri = Uri.parse(base)
                .buildUpon()
                .appendPath(optionId)
                .build();
        restTemplate.delete(URI.create(uri.toString()));
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private void downloadFile(URI uri, File file) throws RestClientException {
        ClientHttpResponse response = null;
        try {
            response = requestFile(uri);
            copyResponseToFile(response, file);
        } catch (IOException ex) {
            throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
        } finally {
            if (response != null) response.close();
        }
    }

    private ClientHttpResponse requestFile(URI uri) throws RestClientException {
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = restTemplate.getRequestFactory().createRequest(uri, HttpMethod.GET);
            response = request.execute();
            if (restTemplate.getErrorHandler().hasError(response)) {
                restTemplate.getErrorHandler().handleError(response);
            }
            return response;
        } catch (IOException ex) {
            throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
        }
    }

    protected int copyResponseToFile(ClientHttpResponse response, File file) throws IOException {
        File parentFolder = file.getParentFile();
        if (parentFolder != null && !parentFolder.exists() && !parentFolder.mkdirs()) {
            throw new IllegalStateException("Unable to create folder: " + parentFolder);
        }
        return FileCopyUtils.copy(response.getBody(), new FileOutputStream(file));
    }

    private void updateRequestFactoryTimeouts() {
        updateConnectTimeout();
        updateReadTimeout();
    }

    private void updateConnectTimeout() {
        requestFactory.setConnectTimeout(connectTimeout);
    }

    private void updateReadTimeout() {
        requestFactory.setReadTimeout(readTimeout);
    }

    private String generateInputControlsUrl(String reportUri, List<String> controlsIds, boolean valuesOnly) {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(jsServerProfile.getServerUrl())
                .append(REST_SERVICES_V2_URI)
                .append(REST_REPORTS_URI)
                .append(reportUri)
                .append(REST_INPUT_CONTROLS_URI);
        // add ids to uri
        if (!controlsIds.isEmpty()) {
            fullUri.append("/");
            for (String id : controlsIds) {
                fullUri.append(id).append(";");
            }
        }
        // add "values" suffix
        if (valuesOnly) {
            fullUri.append(REST_VALUES_URI);
        }
        return fullUri.toString();
    }

    private void configureMessageConverters() {
        MessageConvertersFactory.newInstance(restTemplate, dataType).createMessageConverters();
    }

    private void checkForProfile() {
        if (jsServerProfile == null) {
            throw new IllegalStateException("Server profile is missing.");
        }
    }

    //---------------------------------------------------------------------
    // Inner classes
    //---------------------------------------------------------------------

    public enum DataType {
        XML, JSON
    }

    public static class Builder {
        private RestTemplate restTemplate;
        private JsRestClient.DataType dataType;

        public Builder setRestTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        public Builder setDataType(JsRestClient.DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public JsRestClient build() {
            ensureSaneDefaults();
            return new JsRestClient(this);
        }

        private void ensureSaneDefaults() {
            if (restTemplate == null) {
                restTemplate = new RestTemplate(false);
            }
            if (dataType == null) {
                dataType = DataType.XML;
            }
        }
    }

}