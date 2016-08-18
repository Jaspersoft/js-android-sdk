package com.jaspersoft.android.sdk.widget.report.renderer;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.renderer.event.AvailableChartTypesEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.BookmarksEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ComponentUpdatedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.CurrentPageChangedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.Event;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.MultiPageStateChangedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.PagesCountChangedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ProgressStateEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ReportComponentsChangedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ReportPartsEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.SwapStateEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.vis.HyperlinkEvent;
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
    public void onCurrentPageChanged(CurrentPageChangedEvent currentPageChangedEvent) {
        handleEvent(currentPageChangedEvent);
    }

    @Subscribe
    public void onPagesCountChanged(PagesCountChangedEvent pagesCountChangedEvent) {
        handleEvent(pagesCountChangedEvent);
    }

    @Subscribe
    public void onMultiPageStateChanged(MultiPageStateChangedEvent multiPageStateChangedEvent) {
        handleEvent(multiPageStateChangedEvent);
    }

    @Subscribe
    public void onBookmarkListChanged(BookmarksEvent bookmarksEvent) {
        handleEvent(bookmarksEvent);
    }

    @Subscribe
    public void onReportPartsChanged(ReportPartsEvent reportPartsEvent) {
        handleEvent(reportPartsEvent);
    }

    @Subscribe
    public void onReportComponentsChanged(ReportComponentsChangedEvent reportComponentsEvent) {
        handleEvent(reportComponentsEvent);
    }

    @Subscribe
    public void onAvailableChartTypes(AvailableChartTypesEvent event) {
        handleEvent(event);
    }

    @Subscribe
    public void onComponentUpdated(ComponentUpdatedEvent event) {
        handleEvent(event);
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
            reportRendererCallback.onProgressStateChanged(((ProgressStateEvent) event).isInProgress());
        } else if (event instanceof HyperlinkEvent) {
            reportRendererCallback.onHyperlinkClicked(((HyperlinkEvent) event).getHyperlink());
        } else if (event instanceof CurrentPageChangedEvent) {
            reportRendererCallback.onCurrentPageChanged(((CurrentPageChangedEvent) event).getCurrentPage());
        } else if (event instanceof PagesCountChangedEvent) {
            reportRendererCallback.onPagesCountChanged(((PagesCountChangedEvent) event).getTotalCount());
        } else if (event instanceof MultiPageStateChangedEvent) {
            reportRendererCallback.onMultiPageStateChanged(((MultiPageStateChangedEvent) event).isMultiPage());
        } else if (event instanceof BookmarksEvent) {
            reportRendererCallback.onBookmarkListAvailable(((BookmarksEvent) event).getBookmarkList());
        } else if (event instanceof ReportPartsEvent) {
            reportRendererCallback.onReportPartsAvailable(((ReportPartsEvent) event).getReportPartList());
        } else if (event instanceof AvailableChartTypesEvent) {
            reportRendererCallback.onAvailableChartTypes(((AvailableChartTypesEvent) event).getChartTypes());
        } else if (event instanceof ReportComponentsChangedEvent) {
            reportRendererCallback.onReportComponentsChanged(((ReportComponentsChangedEvent) event).getReportComponents());
        } else if (event instanceof ComponentUpdatedEvent) {
            reportRendererCallback.onComponentUpdated();
        }
    }
}
