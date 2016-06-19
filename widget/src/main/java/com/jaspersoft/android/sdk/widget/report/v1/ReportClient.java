package com.jaspersoft.android.sdk.widget.report.v1;

import android.os.Parcel;
import android.os.Parcelable;

import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

import java.util.UUID;


/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportClient implements Parcelable {
    private final String key;
    private final Command.Factory commandFactory;
    private final Scope scope;

    private final ScopeCache scopeCache = ScopeCache.INSTANCE;

    LifecycleCallbacks lifecycleCallbacks = SimpleLifeCycle.NULL;
    ErrorCallbacks errorCallbacks = SimpleErrorCallbacks.NULL;

    public ReportClient() {
        this(new CommandFactory());
    }

    ReportClient(Command.Factory factory) {
        this.key = UUID.randomUUID().toString();
        this.commandFactory = factory;
        this.scope = Scope.newInstance(this);
        scopeCache.put(key, scope);
    }

    protected ReportClient(Parcel in) {
        this.key = in.readString();
        this.scope = scopeCache.get(key);
        this.commandFactory = new CommandFactory();
    }

    public ReportClient registerLifecycleCallbacks(LifecycleCallbacks lifecycleCallbacks) {
        if (lifecycleCallbacks == null) {
            lifecycleCallbacks = SimpleLifeCycle.NULL;
        }
        this.lifecycleCallbacks = lifecycleCallbacks;
        return this;
    }

    public ReportClient registerErrorCallbacks(ErrorCallbacks errorCallbacks) {
        if (errorCallbacks == null) {
            errorCallbacks = SimpleErrorCallbacks.NULL;
        }
        this.errorCallbacks = errorCallbacks;
        return this;
    }

    public void run(RunOptions runOptions) {
        dispatchCommand(commandFactory.createLoadTemplateCommand(runOptions));
    }

    public void pause() {
        scope.getDispatcher().unregister(this);
    }

    public void resume() {
        scope.getDispatcher().register(this);
    }

    private void dispatchCommand(Command runCommand) {
        Dispatcher dispatcher = scope.getDispatcher();
        dispatcher.dispatch(runCommand);
    }

    public void removeCallbacks() {
        errorCallbacks = SimpleErrorCallbacks.NULL;
        lifecycleCallbacks = SimpleLifeCycle.NULL;
    }

    public void destroy() {
        scopeCache.remove(key);
        scope.destroy();
    }

    public interface LifecycleCallbacks {
        void onInflateFinish();

        void onScriptLoaded();

        void onReportRendered();
    }

    public interface ErrorCallbacks {
        void onWindowError(WindowError error);
    }

    public static abstract class SimpleLifeCycle implements LifecycleCallbacks {
        private static final LifecycleCallbacks NULL = new SimpleLifeCycle() {
        };

        @Override
        public void onInflateFinish() {
        }

        @Override
        public void onScriptLoaded() {
        }

        @Override
        public void onReportRendered() {
        }
    }

    public static abstract class SimpleErrorCallbacks implements ErrorCallbacks {
        private static final SimpleErrorCallbacks NULL = new SimpleErrorCallbacks() {
        };

        @Override
        public void onWindowError(WindowError error) {
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
    }


    public static final Creator<ReportClient> CREATOR = new Creator<ReportClient>() {
        @Override
        public ReportClient createFromParcel(Parcel source) {
            return new ReportClient(source);
        }

        @Override
        public ReportClient[] newArray(int size) {
            return new ReportClient[size];
        }
    };
}
