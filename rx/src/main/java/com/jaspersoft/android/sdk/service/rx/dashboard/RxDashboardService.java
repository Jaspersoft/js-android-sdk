package com.jaspersoft.android.sdk.service.rx.dashboard;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.dashboard.DashboardService;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;

import org.jetbrains.annotations.NotNull;

import rx.Observable;
import rx.functions.Func0;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RxDashboardService {
    private final DashboardService mSyncDelegate;

    RxDashboardService(DashboardService mSyncDelegate) {
        this.mSyncDelegate = mSyncDelegate;
    }

    /**
     * Performs request that export dashboard
     *
     * @param dashboardExportOptions options for exporting dashboard
     * @return dashboard resource output
     * @throws ServiceException wraps both http/network/api related errors
     */
    /**
     * Performs request that export dashboard
     *
     * @param dashboardExportOptions options for exporting dashboard
     * @return observable that emits dashboard resource output if operation was successful
     */
    @NotNull
    public Observable<ResourceOutput> export(final DashboardExportOptions dashboardExportOptions) {
        return Observable.defer(new Func0<Observable<ResourceOutput>>() {
            @Override
            public Observable<ResourceOutput> call() {
                try {
                    ResourceOutput resourceOutput = mSyncDelegate.export(dashboardExportOptions);
                    return Observable.just(resourceOutput);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static RxDashboardService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        DashboardService service = DashboardService.newService(client);
        return new RxDashboardService(service);
    }

    /**
     * Provides synchronous counterpart of service
     *
     * @return wrapped version of service {@link DashboardService}
     */
    public DashboardService toBlocking() {
        return mSyncDelegate;
    }
}
