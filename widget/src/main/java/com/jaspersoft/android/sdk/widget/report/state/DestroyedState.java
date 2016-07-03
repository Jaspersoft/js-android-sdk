package com.jaspersoft.android.sdk.widget.report.state;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class DestroyedState extends State {
    DestroyedState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor) {
        super(dispatcher, eventFactory, commandFactory, commandExecutor);
    }

    @Override
    protected void internalInit(double initialScale) {
        throw new IllegalStateException("Could not init. Already destroyed.");
    }

    @Override
    protected void internalRender(RunOptions runOptions) {
        throw new IllegalStateException("Could not render. Already destroyed.");
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        throw new IllegalStateException("Could not Apply params. Already destroyed.");
    }

    @Override
    protected void internalNavigateTo(Destination destination) {
        throw new IllegalStateException("Could not navigate to. Already destroyed.");
    }

    @Override
    protected void internalRefresh() {
        throw new IllegalStateException("Could not refresh data. Already destroyed.");
    }

    @Override
    protected void internalClear() {
        throw new IllegalStateException("Could not clear. Already destroyed.");
    }

    @Override
    protected void internalDestroy() {
        throw new IllegalStateException("Could not destroy. Already destroyed.");
    }
}
