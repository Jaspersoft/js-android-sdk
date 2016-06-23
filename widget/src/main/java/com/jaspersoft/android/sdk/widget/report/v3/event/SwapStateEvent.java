package com.jaspersoft.android.sdk.widget.report.v3.event;

import com.jaspersoft.android.sdk.widget.report.v3.RenderState;

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
