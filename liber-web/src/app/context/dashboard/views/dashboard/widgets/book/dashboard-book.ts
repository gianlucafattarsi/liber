import { CommonModule } from '@angular/common'
import { Component } from '@angular/core'
import { CardModule } from 'primeng/card'

@Component({
    selector: 'app-dashboard-book',
    imports: [CommonModule, CardModule],
    templateUrl: './dashboard-book.html',
    styleUrl: './dashboard-book.css',
})
export class DashboardBook {}
