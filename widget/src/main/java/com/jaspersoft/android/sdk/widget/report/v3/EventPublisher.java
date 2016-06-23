package com.jaspersoft.android.sdk.widget.report.v3;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.v3.event.Event;
import com.jaspersoft.android.sdk.widget.report.v3.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.v3.event.SwapStateEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class EventPublisher {
    private ReportRendererCallback reportRendererCallback;
    private List<Event> eventsQueue;

    public EventPublisher() {
        eventsQueue = new ArrayList<>();
    }

    @Subscribe
    public void onSwapState(SwapStateEvent nextStateEvent) {
        handleEvent(nextStateEvent);
    }

    @Subscribe
    public void onException(ExceptionEvent exceptionEvent) {
        handleEvent(exceptionEvent);
    }

    public void setReportRendererCallback(ReportRendererCallback reportRendererCallback) {
        this.reportRendererCallback = reportRendererCallback;
        if (reportRendererCallback == null) return;

        for (Event event : eventsQueue) {
            sendEvent(event);
        }
    }

    private void handleEvent(Event event) {
        if (reportRendererCallback == null) {
            eventsQueue.add(event);
        } else {
            sendEvent(event);
        }
    }

    private void sendEvent(Event event) {
        if (event instanceof SwapStateEvent) {
            reportRendererCallback.onRenderStateChanged(((SwapStateEvent) event).getNextRenderState());
        } else if (event instanceof ExceptionEvent) {
            ServiceException exception = ((ExceptionEvent) event).getException();
            reportRendererCallback.onError(exception);
        }
    }
}
