package com.jaspersoft.android.sdk.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewFragment extends Fragment {
    public static ReportViewFragment newInstance() {
        ReportViewFragment reportWebView = new ReportViewFragment();
        reportWebView.setRetainInstance(true);
        return reportWebView;
    }

    private JasperReportView reportView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (reportView == null) {
            reportView = new JasperReportView(getActivity());
        }
        return reportView;
    }

    public void init(AuthorizedClient client, JasperReportView.InflateCallback callback) {
        reportView.init(client, callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getRetainInstance() && reportView.getParent() instanceof ViewGroup) {
            ((ViewGroup) reportView.getParent()).removeView(reportView);
        }
    }
}
