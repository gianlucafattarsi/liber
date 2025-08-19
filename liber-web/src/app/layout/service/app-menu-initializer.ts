import { Injectable } from '@angular/core'
import { SettingsMenuInitService } from '../../context/admin/infrastructure/settings-menu-init.service'
import { DashboardMenuInitService } from '../../context/dashboard/infrastructure/dashboard-menu-init.service'
import { LibraryMenuInitService } from '../../context/library/infrastructure/library-menu-init.service'

@Injectable({
    providedIn: 'root',
})
export class AppMenuInitilizer {
    constructor(
        private dashboardMenuInitService: DashboardMenuInitService,
        private settingsMenuInitService: SettingsMenuInitService,
        private libraryMenuInitService: LibraryMenuInitService
    ) {}

    public init() {
        this.dashboardMenuInitService.init()
        this.settingsMenuInitService.init()
        this.libraryMenuInitService.init()
    }
}
