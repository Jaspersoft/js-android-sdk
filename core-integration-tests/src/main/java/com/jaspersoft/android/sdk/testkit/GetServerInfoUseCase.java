package com.jaspersoft.android.sdk.testkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jaspersoft.android.sdk.testkit.dto.Info;
import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class GetServerInfoUseCase extends HttpUseCase<Info, Void> {
    private final String mBaseUrl;

    protected GetServerInfoUseCase(OkHttpClient client, String baseUrl) {
        super(client);
        mBaseUrl = baseUrl;
    }

    @Override
    public Info execute(Void aVoid) throws IOException, HttpException {
        HttpUrl url = HttpUrl.parse(mBaseUrl).resolve("rest_v2/serverInfo");

        Response response = performRequest(url);

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream(), "utf-8"));
        return gson.fromJson(reader, Info.class);
    }
}
