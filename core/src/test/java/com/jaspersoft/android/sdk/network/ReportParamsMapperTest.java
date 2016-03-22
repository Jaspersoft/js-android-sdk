package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;


public class ReportParamsMapperTest {
    @Test
    public void testToMap() throws Exception {
        ReportParameter reportParameter1 = new ReportParameter("a", Collections.singleton("b"));
        ReportParameter reportParameter2 = new ReportParameter("c", Collections.singleton("d"));
        Map<String, Set<String>> params = ReportParamsMapper.INSTANCE.toMap(
                Arrays.asList(reportParameter1, reportParameter2));

        Set<String> values = new HashSet<>();
        for (Set<String> sets : params.values()) {
            values.addAll(sets);
        }
        assertThat(params.keySet(), hasItems("a", "c"));
        assertThat(values, hasItems("b", "d"));
    }
}