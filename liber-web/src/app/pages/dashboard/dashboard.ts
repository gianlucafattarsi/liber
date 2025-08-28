import { Component } from '@angular/core'
import { CardModule } from 'primeng/card'
import { ChartModule } from 'primeng/chart'
import { Fieldset } from 'primeng/fieldset'
import { ProgressBarModule } from 'primeng/progressbar'
import { DataService } from '../../modules/shared/service/data/data-service'
import { ToastService } from '../../modules/shared/service/toast/toast-service'

@Component({
    selector: 'app-dashboard',
    imports: [ProgressBarModule, ChartModule, Fieldset, CardModule],
    templateUrl: 'dashboard.html',
})
export class Dashboard {
    constructor(
        private dataService: DataService,
        private toastService: ToastService
    ) {}
}
