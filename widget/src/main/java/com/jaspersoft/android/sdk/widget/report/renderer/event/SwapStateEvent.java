package com.jaspersoft.android.sdk.widget.report.renderer.event;

import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class SwapStateEvent implements Event {
    private final RenderState nextRenderState;

    public SwapStateEvent(RenderState nextRenderState) {
        this.nextRenderState = nextRenderState;
    }

    public RenderState getNextRenderState() {
        return nextRenderState;
    }
}
