import { CommonModule } from '@angular/common'
import { Component, OnInit, signal } from '@angular/core'
import { RouterModule } from '@angular/router'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { MenuItem } from 'primeng/api'
import { ButtonModule } from 'primeng/button'
import { ChipModule } from 'primeng/chip'
import { ImageModule } from 'primeng/image'
import { MenuModule } from 'primeng/menu'
import { StyleClassModule } from 'primeng/styleclass'
import { AuthService } from '../../modules/shared/service/auth/auth-service'
import { I18nService } from '../../modules/shared/service/i18n/i18n-service'
import { LayoutService } from '../service/layout.service'
import { AppConfigurator } from './app.configurator'
import { AppLoggedUserInfo } from './app.loggeduser.info'

@Component({
    selector: 'app-topbar',
    standalone: true,
    imports: [
        RouterModule,
        CommonModule,
        StyleClassModule,
        ChipModule,
        ImageModule,
        AppConfigurator,
        AppLoggedUserInfo,
        MenuModule,
        ButtonModule,
        TranslatePipe,
        FontAwesomeModule,
    ],
    templateUrl: './app.topbar.html',
})
export class AppTopbar implements OnInit {
    items!: MenuItem[]

    utente = signal<string | null>(null)

    langItems: MenuItem[] = []

    constructor(
        public layoutService: LayoutService,
        private authService: AuthService,
        private i18nService: I18nService
    ) {}

    ngOnInit(): void {
        this.langItems = this.i18nService.langsMenuItems()
        this.authService.currentUser.subscribe((user) => {
            this.utente.set('')
            if (user) {
                this.utente.set(`${user.userName}`)
            }
        })
    }

    toggleDarkMode() {
        this.layoutService.layoutConfig.update((state) => ({
            ...state,
            darkTheme: !state.darkTheme,
        }))
    }
}
