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

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ServerBundle;
import com.jaspersoft.android.sdk.env.TestLogger;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.testkit.ServiceFactory;
import com.jaspersoft.android.sdk.testkit.dto.ReportExecConfig;
import com.jaspersoft.android.sdk.testkit.exception.HttpException;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class IntegrationReportExecutionRestApiTest {

    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    private Object[] reports() {
        return sEnv.listReports();
    }

    @Test
    @Parameters(method = "reports")
    public void shouldStartReportExecution(ServerBundle bundle, String reportUri) throws Exception {
        String baseUrl = bundle.getUrl();
        String token = bundle.getToken();
        ServiceFactory serviceFactory = ServiceFactory.builder()
                .baseUrl(baseUrl).token(token)
                .create();
        Map<String, Set<String>> params =
                serviceFactory.resourceParameter(reportUri).listParams();

        ReportExecutionRequestOptions executionRequestOptions = ReportExecutionRequestOptions.newRequest(reportUri);
        if (!params.isEmpty()) {
            executionRequestOptions.withParameters(params);
        }

        ReportExecutionDescriptor response = createApi(baseUrl).runReportExecution(token, executionRequestOptions);
        assertThat("Failed to start report execution", response != null);

        serviceFactory.reporExec().cancel(response.getExecutionId());
    }

    @Test
    @Parameters(method = "reports")
    public void shouldCancelReportExecution(ServerBundle bundle, String reportUri) throws Exception {
        String baseUrl = bundle.getUrl();
        String token = bundle.getToken();
        startExecution(token, baseUrl, reportUri).perform(new Action() {
            @Override
            public void call(Session session) {
                createApi(baseUrl).cancelReportExecution(token, session.getExecId());
            }
        });
    }

    @Test
    @Parameters(method = "reports")
    public void shouldReturnReportExecutionDetails(ServerBundle bundle, String reportUri) throws Exception {
        String baseUrl = bundle.getUrl();
        String token = bundle.getToken();
        startExecution(token, baseUrl, reportUri).perform(new Action() {
            @Override
            public void call(Session session) {
                ReportExecutionDescriptor response = createApi(baseUrl).requestReportExecutionDetails(token, session.getExecId());
                assertThat("Failed to start received execution details", response != null);
            }
        });
    }

    @Test
    @Parameters(method = "reports")
    public void shouldCheckReportExecutionStatus(ServerBundle bundle, String reportUri) throws Exception {
        String baseUrl = bundle.getUrl();
        String token = bundle.getToken();
        startExecution(token, baseUrl, reportUri).perform(new Action() {
            @Override
            public void call(Session session) {
                ExecutionStatus response = createApi(baseUrl).requestReportExecutionStatus(token, session.getExecId());
                assertThat("Failed to received execution status", response != null);
            }
        });
    }

    @Test
    @Parameters(method = "reports")
    public void searchForExecutionShouldReturnResult(ServerBundle bundle, final String reportUri) throws Exception {
        String baseUrl = bundle.getUrl();
        String token = bundle.getToken();
        startExecution(token, baseUrl, reportUri).perform(new Action() {
            @Override
            public void call(Session session) {
                Map<String, String> params = new HashMap<>();
                params.put("reportURI", reportUri);
                ReportExecutionSearchResponse response = createApi(baseUrl).searchReportExecution(token, params);
                assertThat("Failed to search execution items", !response.getItems().isEmpty());
            }
        });
    }


    @Test
    @Parameters(method = "reports")
    public void updateOfParametersForExecutionShouldReturnResult(ServerBundle bundle, String reportUri) throws Exception {
        String baseUrl = bundle.getUrl();
        String token = bundle.getToken();
        startExecution(token, baseUrl, reportUri).perform(new Action() {
            @Override
            public void call(Session session) {
                List<Map<String, Set<String>>> list = new ArrayList<>();
                list.add(session.getParams());

                boolean success = createApi(baseUrl).updateReportExecution(token, session.getExecId(), list);
                assertThat(success, is(true));
            }
        });
    }


    //==============================================================================================

    public static Session startExecution(String token, String baseUrl, String reportUri) throws IOException, HttpException {
        return new SessionManager(token, baseUrl, reportUri).start();
    }

    private ReportExecutionRestApi createApi(String baseUrl) {
        return new ReportExecutionRestApi.Builder()
                .baseUrl(baseUrl)
                .logger(TestLogger.get(this))
                .build();
    }

    public static class SessionManager {
        private final String token;
        private final String baseUrl;
        private final String reportUri;

        private SessionManager(String token, String baseUrl, String reportUri) {
            this.token = token;
            this.baseUrl = baseUrl;
            this.reportUri = reportUri;
        }

        public Session start() throws IOException, HttpException {
            ServiceFactory serviceFactory = ServiceFactory.builder()
                    .baseUrl(baseUrl).token(token)
                    .create();
            Map<String, Set<String>> params =
                    serviceFactory.resourceParameter(reportUri).listParams();
            ReportExecConfig.Builder configBuilder = ReportExecConfig.builder().uri(reportUri);
            if (!params.isEmpty()) {
                configBuilder.params(params);
            }
            String execId = serviceFactory.reporExec().start(configBuilder.create());
            return new Session(serviceFactory, params, execId);
        }
    }

    private static class Session {
        private final ServiceFactory serviceFactory;
        private final Map<String, Set<String>> params;
        private final String execId;

        private Session(ServiceFactory serviceFactory, Map<String, Set<String>> params, String execId) {
            this.serviceFactory = serviceFactory;
            this.execId = execId;
            this.params = params;
        }

        public String getExecId() {
            return execId;
        }

        public Map<String, Set<String>> getParams() {
            return params;
        }

        public void perform(Action callback) throws IOException, HttpException {
            callback.call(this);
            serviceFactory.reporExec().cancel(execId);
        }
    }

    private interface Action {
        void call(Session session);
    }
}
