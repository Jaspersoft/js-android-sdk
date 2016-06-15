package com.jaspersoft.android.sdk.widget.dashboard;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;
import com.jaspersoft.android.sdk.widget.RunOptions;

import java.util.UUID;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class DashboardClient implements Parcelable {

    private final String key;
    private final Command.Factory commandFactory;
    private final Scope scope;

    private final ScopeCache scopeCache = ScopeCache.INSTANCE;

    LifecycleCallbacks lifecycleCallbacks = NullLifeCycle.INSTANCE;
    ErrorCallbacks errorCallbacks = NullErrorCallbacks.INSTANCE;
    DashletCallbacks dashletCallbacks = NullDashletCallbacks.INSTANCE;

    public DashboardClient() {
        this(new CommandFactory());
    }

    DashboardClient(Command.Factory factory) {
        this.key = UUID.randomUUID().toString();
        this.commandFactory = factory;
        this.scope = Scope.newInstance(this);
        scopeCache.put(key, scope);
    }

    public DashboardClient registerLifecycleCallbacks(LifecycleCallbacks lifecycleCallbacks) {
        if (lifecycleCallbacks == null) {
            lifecycleCallbacks = NullLifeCycle.INSTANCE;
        }
        this.lifecycleCallbacks = lifecycleCallbacks;
        return this;
    }

    public DashboardClient registerErrorCallbacks(ErrorCallbacks errorCallbacks) {
        if (errorCallbacks == null) {
            errorCallbacks = NullErrorCallbacks.INSTANCE;
        }
        this.errorCallbacks = errorCallbacks;
        return this;
    }

    public DashboardClient registerDashletCallbacks(DashletCallbacks dashletCallbacks) {
        if (dashletCallbacks == null) {
            dashletCallbacks = NullDashletCallbacks.INSTANCE;
        }
        this.dashletCallbacks = dashletCallbacks;
        return this;
    }

    public void run(RunOptions runOptions) {
        dispatchCommand(commandFactory.createInitCommand(runOptions));
    }

    public void resume() {
    }

    public void pause() {
    }

    public void minimizeDashlet(WebView webView) {
        dispatchCommand(commandFactory.createMinimizeCommand(webView));
    }

    private void dispatchCommand(Command runCommand) {
        Dispatcher dispatcher = scope.getDispatcher();
        dispatcher.dispatch(runCommand);
    }

    public interface LifecycleCallbacks {
        void onInflateFinish();

        void onScriptLoaded();

        void onDashboardRendered();
    }

    public interface ErrorCallbacks {
        void onWindowError(WindowError error);
    }

    public interface DashletCallbacks {
        void onMaximizeStart(String componentName);

        void onMaximizeEnd(String componentName);

        void onMinimizeStart(String componentName);

        void onMinimizeEnd(String componentName);

        void onHypeLinkClick(DashboardHyperlink dashboardHyperlink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
    }

    protected DashboardClient(Parcel in) {
        this.key = in.readString();
        this.commandFactory = new CommandFactory();
        this.scope = scopeCache.get(key);
    }

    public static final Creator<DashboardClient> CREATOR = new Creator<DashboardClient>() {
        @Override
        public DashboardClient createFromParcel(Parcel source) {
            return new DashboardClient(source);
        }

        @Override
        public DashboardClient[] newArray(int size) {
            return new DashboardClient[size];
        }
    };

    private static class NullLifeCycle implements LifecycleCallbacks {
        private static final LifecycleCallbacks INSTANCE = new NullLifeCycle();

        @Override
        public void onInflateFinish() {
        }

        @Override
        public void onScriptLoaded() {
        }

        @Override
        public void onDashboardRendered() {
        }
    }

    private static class NullErrorCallbacks implements ErrorCallbacks {
        private static final NullErrorCallbacks INSTANCE = new NullErrorCallbacks();

        @Override
        public void onWindowError(WindowError error) {
        }
    }

    private static class NullDashletCallbacks implements DashletCallbacks {
        private static final DashletCallbacks INSTANCE = new NullDashletCallbacks();

        @Override
        public void onMaximizeStart(String componentName) {
        }

        @Override
        public void onMaximizeEnd(String componentName) {
        }

        @Override
        public void onMinimizeStart(String componentName) {
        }

        @Override
        public void onMinimizeEnd(String componentName) {
        }

        @Override
        public void onHypeLinkClick(DashboardHyperlink dashboardHyperlink) {
        }
    }
}
