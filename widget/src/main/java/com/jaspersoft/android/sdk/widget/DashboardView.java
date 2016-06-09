package com.jaspersoft.android.sdk.widget;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class DashboardView implements Parcelable {

    private final String key;
    private final Command.Factory commandFactory;
    private final Scope scope;

    private final ScopeCache scopeCache = ScopeCache.INSTANCE;

    Lifecycle lifecycle = NullLifeCycle.INSTANCE;
    ErrorCallback errorCallback = NullErrorCallback.INSTANCE;

    public DashboardView() {
        this(new CommandFactory());
    }

    DashboardView(Command.Factory factory) {
        this.key = UUID.randomUUID().toString();
        this.commandFactory = factory;
        this.scope = Scope.newInstance(this);
        scopeCache.put(key, scope);
    }

    public DashboardView registerLifecycle(Lifecycle lifecycle) {
        if (lifecycle == null) {
            lifecycle = NullLifeCycle.INSTANCE;
        }
        this.lifecycle = lifecycle;
        return this;
    }

    public DashboardView registerErrorCallback(ErrorCallback errorCallback) {
        if (errorCallback == null) {
            errorCallback = NullErrorCallback.INSTANCE;
        }
        this.errorCallback = errorCallback;
        return this;
    }

    public void run(RunOptions runOptions) {
        Command runCommand = commandFactory.createInitCommand(runOptions);
        Dispatcher dispatcher = scope.getDispatcher();
        dispatcher.dispatch(runCommand);
    }

    public void resume() {
    }

    public void pause() {
    }

    public interface Lifecycle {
        void onInflateFinish();

        void onScriptLoaded();

        void onDashboardRendered();
    }

    public interface ErrorCallback {
        void onWindowError(WindowError error);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
    }

    protected DashboardView(Parcel in) {
        this.key = in.readString();
        this.commandFactory = new CommandFactory();
        this.scope = scopeCache.get(key);
    }

    public static final Creator<DashboardView> CREATOR = new Creator<DashboardView>() {
        @Override
        public DashboardView createFromParcel(Parcel source) {
            return new DashboardView(source);
        }

        @Override
        public DashboardView[] newArray(int size) {
            return new DashboardView[size];
        }
    };

    private static class NullLifeCycle implements Lifecycle {
        private static final Lifecycle INSTANCE = new NullLifeCycle();

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

    private static class NullErrorCallback implements ErrorCallback {
        private static final NullErrorCallback INSTANCE = new NullErrorCallback();

        @Override
        public void onWindowError(WindowError error) {
        }
    }
}
