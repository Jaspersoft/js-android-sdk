package com.jaspersoft.android.sdk.network;

import com.squareup.okhttp.HttpUrl;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;
import java.util.Map;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class QueryMapperTest {

    private QueryMapper mQueryMapper;
    private final HttpUrl mLocalhost = HttpUrl.parse("http://localhost/");

    @Before
    public void setUp() throws Exception {
        mQueryMapper = QueryMapper.INSTANCE;
    }

    @Test
    public void should_ignore_null_params() throws Exception {
        HttpUrl expected = mQueryMapper.mapParams(null, mLocalhost);
        assertThat(expected, is(mLocalhost));
    }

    @Test
    @Parameters(method = "params")
    public void should_encode_any_param(String key, String value, String query) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(key, value);

        HttpUrl expected = mQueryMapper.mapParams(params, mLocalhost);
        assertThat(expected, is(HttpUrl.parse("http://localhost/?" + query)));
    }

    private Object[] params() {
        return $(
                $("reportUnitURI", "/some/report", "reportUnitURI=%2Fsome%2Freport"),
                $("owner", "jasperadmin|organization_1", "owner=jasperadmin%7Corganization_1"),
                $("label", "Sample Name", "label=Sample%20Name"),
                $("startIndex", "1", "startIndex=1"),
                $("numberOfRows", "-1", "numberOfRows=-1"),
                $("sortType", "NONE", "sortType=NONE"),
                $("isAscending", "true", "isAscending=true")
        );
    }
}