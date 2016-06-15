package com.jaspersoft.android.sdk.widget.report;

import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

import java.util.UUID;


/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportClient {
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

    public void run(RunOptions runOptions) {
        dispatchCommand(commandFactory.createLoadTemplateCommand(runOptions));
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

    public static abstract class SimpleLifeCycle implements LifecycleCallbacks {
        private static final LifecycleCallbacks NULL = new SimpleLifeCycle() {};

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

    public static abstract class SimpleErrorCallbacks implements ErrorCallbacks {
        private static final SimpleErrorCallbacks NULL = new SimpleErrorCallbacks() {};

        @Override
        public void onWindowError(WindowError error) {
        }
    }
}
