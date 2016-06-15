package com.jaspersoft.android.sdk.widget.report;


import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class CommandHandlerFactory implements CommandHandler.Factory {
    private static final String REST_TEMPLATE = "report-rest-template.html";
    private static final String VIS_TEMPLATE = "report-vis-template.html";

    private final Dispatcher dispatcher;
    private final Event.Factory eventFactory;
    private final Command.Factory commandFactory;

    CommandHandlerFactory(Dispatcher dispatcher) {
        this(dispatcher, new EventFactory(), new CommandFactory());
    }

    CommandHandlerFactory(Dispatcher dispatcher, Event.Factory eventFactory, Command.Factory commandFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
    }

    @Override
    public CommandHandler<LoadTemplateCommand> createProxyLoadTemplateCommandHandler() {
        return new ProxyLoadTemplateCommandHandler(this);
    }

    @Override
    public CommandHandler<LoadTemplateCommand> createLoadTemplateCommandHandler(double version) {
        Object javascriptInterface;
        String templateName;

        if (version >= 6.0) {
            javascriptInterface = new VisualizeJavascriptEvents(dispatcher, eventFactory);
            templateName = VIS_TEMPLATE;
        } else {
            javascriptInterface = new RestJavascriptEvents();
            templateName = REST_TEMPLATE;
        }
        return new LoadTemplateCommandHandler(
                dispatcher,
                eventFactory,
                commandFactory,
                javascriptInterface,
                templateName,
                version
        );
    }

    @Override
    public CommandHandler<RunCommand> runCommandHandler() {
        return new RunCommandHandler();
    }
}
