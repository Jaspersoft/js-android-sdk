package com.jaspersoft.android.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jaspersoft.android.sdk.sample.entity.Profile;
import com.jaspersoft.android.sdk.sample.entity.Resource;
import com.jaspersoft.android.sdk.sample.entity.ResourceFactory;

import java.util.Arrays;
import java.util.List;

public class ResourcesActivity extends AppCompatActivity {
    private final static ResourceFactory SECURE_SERVER_MOB_DEMO = new ResourceFactory(new Profile(
            "https://mobiledemo.jaspersoft.com/jasperserver-pro/"
    ));
    private final static ResourceFactory SERVER_TRUNK = new ResourceFactory(new Profile(
            "http://build-master.jaspersoft.com:5980/jrs-pro-trunk/"
    ));

    private static final List<Resource> RESOURCES = Arrays.asList(
            SECURE_SERVER_MOB_DEMO.newReport("Mob demo - 05. Accounts Report", "/public/Samples/Reports/AllAccounts"),
            SECURE_SERVER_MOB_DEMO.newReport("Mob demo - 03. Store Segment", "/public/Samples/Reports/03._Store_Segment_Performance_Report"),
            SECURE_SERVER_MOB_DEMO.newReport("Mob demo - 04. Product Results", "/public/Samples/Reports/04._Product_Results_by_Store_Type_Report"),
            SECURE_SERVER_MOB_DEMO.newReport("Mob demo - 05. Accounts Report", "/public/Samples/Reports/AllAccounts"),
            SERVER_TRUNK.newReport("Trunk - 03. Store Segment", "/public/Samples/Reports/03._Store_Segment_Performance_Report"),
            SERVER_TRUNK.newReport("Trunk - 04. Product Results", "/public/Samples/Reports/04._Product_Results_by_Store_Type_Report"),
            SERVER_TRUNK.newReport("Trunk - 05. Accounts Report", "/public/Samples/Reports/AllAccounts"),
            SERVER_TRUNK.newReport("Trunk - HyperlinkReport", "/public/Visualize/Hiperlinks/HyperlinkReport"),
            SERVER_TRUNK.newReport("Trunk - Fusion_links_report_anchor", "/public/Visualize/Hiperlinks/Fusion_links_report_anchor"),
            SERVER_TRUNK.newReport("Trunk - Fusion_link_to_report_page", "/public/Visualize/Hiperlinks/Fusion_link_to_report_page"),
            SERVER_TRUNK.newReport("Trunk - Fusion_links_report_output_format", "/public/Visualize/Hiperlinks/Fusion_links_report_output_format")
    );

    public static final String RESOURCE_EXTRA = "resource";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<Resource> adapter = provideAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigate(RESOURCES.get(position));
            }
        });
    }

    @NonNull
    private ArrayAdapter<Resource> provideAdapter() {
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RESOURCES);
    }

    private void navigate(Resource resource) {
        if (resource.isReport()) {
            navigateToReport(resource);
        }
        if (resource.isDashboard()) {
            navigateToDashboard(resource);
        }
    }

    private void navigateToReport(Resource resource) {
        Intent intent = new Intent(this, ReportViewActivity.class);
        intent.putExtra(RESOURCE_EXTRA, resource);
        startActivity(intent);
    }

    private void navigateToDashboard(Resource resource) {
        Intent intent = new Intent(this, DashboardViewActivity.class);
        intent.putExtra(RESOURCE_EXTRA, resource);
        startActivity(intent);
    }
}
