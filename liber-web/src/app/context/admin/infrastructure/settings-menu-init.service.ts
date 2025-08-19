import { Injectable } from '@angular/core'
import { AppMenuService } from '../../../layout/service/app-menu-service'

@Injectable({
    providedIn: 'root',
})
export class SettingsMenuInitService {
    constructor(private appMenuService: AppMenuService) {}

    public init() {
        this.appMenuService.addMenu({
            label: 'Admin',
            icon: 'gears',
            expanded: true,
            items: [
                {
                    label: 'menu.users',
                    icon: 'users',
                    routerLink: ['/admin/users'],
                },
            ],
        })
    }
}
