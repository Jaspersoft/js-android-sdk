package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobForm;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.network.entity.schedule.JobsSearchResult;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportScheduleRestApi {
    private final RestApi mRestApi;

    @TestOnly
    ReportScheduleRestApi(Retrofit retrofit) {
        mRestApi = retrofit.create(RestApi.class);
    }

    @NotNull
    public List<JobUnit> searchJob(@Nullable Map<String, Object> searchParams) throws IOException, HttpException {
        Map<String, Object> encodedParams = Collections.emptyMap();
        if (searchParams == null) {
            searchParams = Collections.emptyMap();
        }

        if (!searchParams.isEmpty()) {
            if (searchParams.containsKey("example")) {
                throw new IllegalArgumentException("Current version does not support 'example' search parameter");
            }

            encodedParams = new HashMap<>(searchParams.size());
            for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
                String rawValue = String.valueOf(entry.getValue());
                String encodedValue = URLEncoder.encode(rawValue, "UTF-8");
                encodedParams.put(entry.getKey(), encodedValue);
            }
        }

        Call<JobsSearchResult> call = mRestApi.searchJob(encodedParams);
        JobsSearchResult searchResult = CallWrapper.wrap(call).body();
        return searchResult.getJobSummary();
    }

    @NotNull
    public JobDescriptor createJob(@NotNull JobForm form) throws IOException, HttpException {
        Preconditions.checkNotNull(form, "Job form should not be null");
        Call<JobDescriptor> call = mRestApi.createJob(form);
        return CallWrapper.wrap(call).body();
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/jobs")
        public Call<JobsSearchResult> searchJob(@Nullable @QueryMap(encoded = true) Map<String, Object> searchParams);

        @NotNull
        @Headers({
                "Content-Type: application/job+json",
                "Accept: application/job+json"
        })
        @PUT("rest_v2/jobs")
        public Call<JobDescriptor> createJob(@NotNull @Body JobForm descriptor);
    }
}
