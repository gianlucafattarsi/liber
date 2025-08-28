import { Injectable } from '@angular/core'
import { AppMenuService } from '../../../layout/service/app-menu-service'

@Injectable({
    providedIn: 'root',
})
export class DashboardMenuInitService {
    constructor(private appMenuService: AppMenuService) {}

    public init() {
        this.appMenuService.addMenu({
            label: 'home',
            expanded: true,
            icon: 'home',
            items: [
                {
                    label: 'Dashboard',
                    icon: 'grip',
                    routerLink: ['/data/dashboard'],
                },
            ],
        })
    }
}
