import { CommonModule } from '@angular/common'
import { Component } from '@angular/core'
import { CardModule } from 'primeng/card'

@Component({
    selector: 'app-dashboard-serie',
    imports: [CommonModule, CardModule],
    templateUrl: './dashboard-serie.html',
    styleUrl: './dashboard-serie.css',
})
export class DashboardSerie {}
