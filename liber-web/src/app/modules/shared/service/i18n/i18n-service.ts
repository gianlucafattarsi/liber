import { Injectable } from '@angular/core'
import { TranslateService } from '@ngx-translate/core'
import { MenuItem } from 'primeng/api'
import { AuthService } from '../auth/auth-service'

@Injectable({
    providedIn: 'root',
})
export class I18nService {
    constructor(
        private translateService: TranslateService,
        private authService: AuthService
    ) {}

    public langsMenuItems(): MenuItem[] {
        return [
            {
                label: 'language',
                items: this.langsItems(),
            },
        ]
    }

    public langsItems(): MenuItem[] {
        return [
            {
                lang: 'en',
                label: 'languages.english',
                icon: 'uk',
                command: () => {
                    this.applyLang('en')
                },
            },
            {
                lang: 'it',
                label: 'languages.italian',
                icon: 'it',
                command: () => {
                    this.applyLang('it')
                },
            },
        ]
    }

    public appLang(): string {
        return this.authService.currentUserValue?.lang || 'en'
    }

    public applyLang(lang: string) {
        this.translateService.use(lang)
    }
}
