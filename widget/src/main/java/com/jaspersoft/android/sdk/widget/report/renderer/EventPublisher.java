package com.jaspersoft.android.sdk.widget.report.renderer;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.renderer.event.Event;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.vis.HyperlinkEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ProgressStateEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.SwapStateEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class EventPublisher {
    private final List<Event> eventsQueue;
    private ReportRendererCallback reportRendererCallback;

    public EventPublisher() {
        eventsQueue = new ArrayList<>();
    }

    @Subscribe
    public void onSwapState(SwapStateEvent nextStateEvent) {
        handleEvent(nextStateEvent);
    }

    @Subscribe
    public void onProgressChange(ProgressStateEvent progressStateEvent) {
        handleEvent(progressStateEvent);
    }

    @Subscribe
    public void onHyperlinkClicked(HyperlinkEvent hyperlinkEvent) {
        handleEvent(hyperlinkEvent);
    }

    @Subscribe
    public void onException(ExceptionEvent exceptionEvent) {
        handleEvent(exceptionEvent);
    }

    public void setReportRendererCallback(ReportRendererCallback reportRendererCallback) {
        this.reportRendererCallback = reportRendererCallback;
        if (reportRendererCallback == null) return;

        ListIterator<Event> eventsIterator = eventsQueue.listIterator();
        while (eventsIterator.hasNext()) {
            sendEvent(eventsIterator.next());
            eventsIterator.remove();
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
        } else if (event instanceof ProgressStateEvent) {
            reportRendererCallback.onProgressStateChange(((ProgressStateEvent) event).isInProgress());
        } else if (event instanceof HyperlinkEvent) {
            reportRendererCallback.onHyperlinkClicked(((HyperlinkEvent) event).getHyperlink());
        }
    }
}