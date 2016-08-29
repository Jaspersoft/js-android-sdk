package com.jaspersoft.android.sdk.widget.report.renderer.event;

import com.jaspersoft.android.sdk.widget.report.renderer.ReportComponent;
import java.util.List;

/**
 * @author Olexandr Dahno
 * @since 2.6
 */

public class ReportComponentsChangedEvent implements Event {

    private final List<ReportComponent> reportComponents;

    ReportComponentsChangedEvent(List<ReportComponent> reportComponents) {
        this.reportComponents = reportComponents;
    }

    public List<ReportComponent> getReportComponents() {
        return reportComponents;
    }
}
