import { Component } from '@angular/core'
import { RouterModule } from '@angular/router'
import { FaIconLibrary } from '@fortawesome/angular-fontawesome'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { TranslateModule, TranslateService } from '@ngx-translate/core'
import { I18nService } from './app/modules/shared/service/i18n/i18n-service'

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterModule, TranslateModule],
    template: `<router-outlet></router-outlet>`,
})
export class AppComponent {
    constructor(
        library: FaIconLibrary,
        private translate: TranslateService,
        i18nService: I18nService
    ) {
        library.addIconPacks(fas, fab)

        this.translate.addLangs(['en', 'it'])
        this.translate.setDefaultLang(i18nService.appLang())
        this.translate.use(i18nService.appLang())
    }
}
