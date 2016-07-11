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
    private final static ResourceFactory SERVER_MOB_DEMO = new ResourceFactory(new Profile(
            "http://mobiledemo.jaspersoft.com/jasperserver-pro/"
    ));
    private final static ResourceFactory SECURE_SERVER_MOB_DEMO = new ResourceFactory(new Profile(
            "https://mobiledemo.jaspersoft.com/jasperserver-pro/"
    ));
    private final static ResourceFactory SERVER_5_6_1 = new ResourceFactory(new Profile(
            "http://192.168.88.55:8082/jasperserver-pro-561/"
    ));
    private final static ResourceFactory SERVER_6_0 = new ResourceFactory(new Profile(
            "http://192.168.88.55:8083/jasperserver-pro-60/"
    ));
    private final static ResourceFactory SERVER_6_0_1 = new ResourceFactory(new Profile(
            "http://192.168.88.55:8084/jasperserver-pro-601/"
    ));
    private final static ResourceFactory SERVER_6_1 = new ResourceFactory(new Profile(
            "http://192.168.88.55:8085/jasperserver-pro-61/"
    ));
    private final static ResourceFactory SERVER_6_11 = new ResourceFactory(new Profile(
            "http://192.168.88.55:8086/jasperserver-pro-611/"
    ));
    private final static ResourceFactory SERVER_6_2 = new ResourceFactory(new Profile(
            "http://192.168.88.55:8088/jasperserver-pro-62/"
    ));
    private final static ResourceFactory SERVER_6_2_1 = new ResourceFactory(new Profile(
            "http://194.29.62.80:8089/jasperserver-pro-621/"
    ));

    private static final List<Resource> RESOURCES = Arrays.asList(
            SERVER_5_6_1.newReport(
                    "5.6.1 - 01. Geographic Results by Segment Report",
                    "/public/Samples/Reports/01._Geographic_Results_by_Segment_Report",
                    "{\"sales__product__product_name_1\":[],\"sales__product__recyclable_package_1\":[\"false\",\"true\"],\"sales__product__low_fat_1\":[\"false\",\"true\"],\"sales_fact_ALL__store_sales_2013_1\":[\"20\"]}"
            ),
            SERVER_5_6_1.newReport("5.6.1 - 05. Accounts Report", "/public/Samples/Reports/AllAccounts"),
            SERVER_5_6_1.newReport("5.6.1 - 06. Profit Detail Report","/public/Samples/Reports/ProfitDetailReport"),
            SERVER_5_6_1.newReport("5.6.1 - Not existing","/public/Samples/Reports/fail"),
            SERVER_6_0_1.newReport("6.0.1 - 02. Sales mix","/public/Samples/Reports/02._Sales_Mix_by_Demographic_Report"),
            SERVER_6_0_1.newReport("6.0.1 - 05. Accounts Report","/public/Samples/Reports/AllAccounts"),
            SERVER_6_0_1.newReport("6.0.1 - 06. Profit Detail Report","/public/Samples/Reports/ProfitDetailReport"),
            SERVER_6_0_1.newReport("6.0.1 - Hyperlink","/public/Visualize/Hiperlinks/HyperlinkReport"),
            SERVER_6_0_1.newReport("6.0.1 - Not existing","/public/Samples/Reports/fail"),
            SERVER_6_0_1.newReport("6.0.1 - 13. Top Fives Report", "/public/Samples/Reports/TopFivesReport"),
            SERVER_6_2_1.newReport("6.2.1 - 03. Store Segment","/public/Samples/Reports/03._Store_Segment_Performance_Report"),
            SERVER_6_2_1.newReport("6.2.1 - 05. Accounts Report","/public/Samples/Reports/AllAccounts"),
            SERVER_6_2_1.newReport("6.2.1 - 06. Profit Detail Report","/public/Samples/Reports/ProfitDetailReport"),
            SERVER_6_2_1.newReport("6.2.1 - Fusion_links_report_output_format", "/public/Visualize/Hiperlinks/Fusion_links_report_output_format"),
            SERVER_6_2_1.newReport("6.2.1 - Hyperlink","/public/Visualize/Hiperlinks/HyperlinkReport"),
            SERVER_6_2_1.newReport("6.2.1 - Not existing","/public/Samples/Reports/fail"),
            SERVER_MOB_DEMO.newReport("Mob demo - 03. Store Segment","/public/Samples/Reports/03._Store_Segment_Performance_Report"),
            SECURE_SERVER_MOB_DEMO.newReport("SECURE Mob demo - 05. Accounts Report","/public/Samples/Reports/AllAccounts"),
            SERVER_MOB_DEMO.newReport("Mob demo - 04. Product Results","/public/Samples/Reports/04._Product_Results_by_Store_Type_Report"),
            SERVER_MOB_DEMO.newReport("Mob demo - 05. Accounts Report","/public/Samples/Reports/AllAccounts"),
            SERVER_6_0.newDashboard("6.0 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_0_1.newDashboard("6.0.1 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_1.newDashboard("6.1 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_11.newDashboard("6.11 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_2.newDashboard("6.2 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_2_1.newDashboard("6.2.1 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_2_1.newDashboard("6.2.1 Performance Summary Dashboard", "/public/Samples/Dashboards/2._Performance_Summary_Dashboard"),
            SERVER_6_2_1.newDashboard("6.2.1 Remote AdHocViewExecution", "/public/Samples/Dashboards/Remote_AdHocViewExecution")
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
