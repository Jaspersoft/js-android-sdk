package com.jaspersoft.android.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ResourcesActivity extends AppCompatActivity {
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
            "http://192.168.88.55:8089/jasperserver-pro-621/"
    ));

    private static final List<Resource> RESOURCES = Arrays.asList(
            SERVER_5_6_1.newReport("5.6.1 - 05. Accounts Report", "/public/Samples/Reports/AllAccounts"),
            SERVER_6_2.newReport("6.2 - 05. Accounts Report","/public/Samples/Reports/AllAccounts"),
            SERVER_6_0.newDashboard("6.0 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_0_1.newDashboard("6.0.1 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_1.newDashboard("6.1 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_11.newDashboard("6.11 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_2.newDashboard("6.2 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard"),
            SERVER_6_2_1.newDashboard("6.2.1 Supermat Dashboard", "/public/Samples/Dashboards/1._Supermart_Dashboard")
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
                WebViewCookieCompat.removeAllCookies(ResourcesActivity.this);
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
        Toast.makeText(this, "No report view", Toast.LENGTH_LONG);
    }

    private void navigateToDashboard(Resource resource) {
        Intent intent = new Intent(this, DashboardViewActivity.class);
        intent.putExtra(RESOURCE_EXTRA, resource);
        startActivity(intent);
    }
}
