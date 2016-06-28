package com.jaspersoft.android.sdk.widget.report.event;

import com.jaspersoft.android.sdk.widget.report.RenderState;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class SwapStateEvent implements Event {
    private RenderState nextRenderState;

    SwapStateEvent(RenderState nextRenderState) {
        this.nextRenderState = nextRenderState;
    }

    public RenderState getNextRenderState() {
        return nextRenderState;
    }
}
