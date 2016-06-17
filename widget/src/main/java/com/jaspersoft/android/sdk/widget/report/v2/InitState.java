package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class InitState extends PresenterState {
    protected InitState(Context context) {
        super(context);
    }

    @Override
    public void run(RunOptions options) {

    }

    @Override
    public void update(List<ReportParameter> parameters) {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void navigate(ReportQuery query) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void destroy() {

    }
}
