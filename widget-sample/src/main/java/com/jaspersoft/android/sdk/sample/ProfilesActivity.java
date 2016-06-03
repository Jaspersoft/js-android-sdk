package com.jaspersoft.android.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jaspersoft.android.sdk.cookie.CookieProvision;

import java.util.Arrays;
import java.util.List;

public class ProfilesActivity extends AppCompatActivity {
    private static final List<Profile> PROFILES = Arrays.asList(
            new Profile("5.6.1 - 05. Accounts Report", "http://192.168.88.55:8082/jasperserver-pro-561/", "/public/Samples/Reports/AllAccounts"),
            new Profile("6.2.1 - 05. Accounts Report", "http://192.168.88.55:8088/jasperserver-pro-62/", "/public/Samples/Reports/AllAccounts")
    );

    public static final String SERVER_URL_EXTRA = "server_url";
    public static final String REPORT_URI_EXTRA = "report_uri";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<Profile> adapter = provideAdapter();
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CookieProvision.clearAll(ProfilesActivity.this);
                navigate(PROFILES.get(position));
            }
        });
    }

    @NonNull
    private ArrayAdapter<Profile> provideAdapter() {
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PROFILES);
    }

    private void navigate(Profile profile) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra(SERVER_URL_EXTRA, profile.url);
        intent.putExtra(REPORT_URI_EXTRA, profile.uri);
        startActivity(intent);
    }

    private static class Profile {
        private final String label;
        private final String url;
        private final String uri;

        private Profile(String label, String url, String uri) {
            this.label = label;
            this.url = url;
            this.uri = uri;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
