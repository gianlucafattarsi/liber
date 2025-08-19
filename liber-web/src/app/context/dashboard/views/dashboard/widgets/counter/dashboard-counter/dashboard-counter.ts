import { Component, Input } from '@angular/core'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'

@Component({
    selector: 'app-dashboard-counter',
    imports: [FontAwesomeModule],
    templateUrl: './dashboard-counter.html',
    styleUrl: './dashboard-counter.scss',
})
export class DashboardCounter {
    @Input() title: string
    @Input() icon: string
    @Input() value: any
    @Input() color: string
}
