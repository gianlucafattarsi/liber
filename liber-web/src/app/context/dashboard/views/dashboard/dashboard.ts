import { CommonModule } from '@angular/common'
import { Component, OnDestroy, OnInit } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'
import { CardModule } from 'primeng/card'
import { ChartModule } from 'primeng/chart'
import { ProgressBarModule } from 'primeng/progressbar'
import { LibrariesInfo, LibraryInfoService } from '../../../../modules/openapi'
import { ErrorResponseService } from '../../../../modules/shared/service/errors/error-response-service'
import { DashboardBook } from './widgets/book/dashboard-book'
import { DashboardCounter } from './widgets/counter/dashboard-counter/dashboard-counter'
import { DashboardSerie } from './widgets/serie/dashboard-serie'

@Component({
    selector: 'app-dashboard',
    imports: [
        ProgressBarModule,
        ChartModule,
        CardModule,
        CommonModule,
        DashboardCounter,
        TranslatePipe,
        DashboardBook,
        DashboardSerie,
    ],
    templateUrl: 'dashboard.html',
})
export class Dashboard implements OnInit, OnDestroy {
    info: LibrariesInfo | null

    constructor(
        private libraryInfoService: LibraryInfoService,
        private errorResponseService: ErrorResponseService
    ) {}

    ngOnInit() {
        this.loadInfo()
    }

    ngOnDestroy() {}

    private loadInfo() {
        this.info = null

        this.libraryInfoService.loadLibrariesInfo().subscribe({
            next: (value) => {
                this.info = value
            },
            error: (err) => {
                this.errorResponseService.showError(
                    err,
                    'dashboard_comp.libraries_info.loading.failed.title',
                    'dashboard_comp.libraries_info.loading.failed.message'
                )
            },
        })
    }
}
