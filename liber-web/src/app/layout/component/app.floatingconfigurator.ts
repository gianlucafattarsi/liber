import { Component, computed, inject } from '@angular/core'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { MenuItem } from 'primeng/api'
import { ButtonModule } from 'primeng/button'
import { MenuModule } from 'primeng/menu'
import { StyleClassModule } from 'primeng/styleclass'
import { I18nService } from '../../modules/shared/service/i18n/i18n-service'
import { LayoutService } from '../service/layout.service'
import { AppConfigurator } from './app.configurator'

@Component({
    selector: 'app-floating-configurator',
    imports: [
        ButtonModule,
        StyleClassModule,
        AppConfigurator,
        FontAwesomeModule,
        TranslatePipe,
        MenuModule,
    ],
    template: `
        <div class="fixed flex gap-4 top-8 right-8">
            <div class="relative">
                <p-button
                    (click)="menuLangs.toggle($event)"
                    icon="pi pi-language"
                    variant="text"
                    [rounded]="true"
                />
                <p-menu
                    #menuLangs
                    [model]="langItems"
                    [popup]="true"
                    appendTo="body"
                >
                    <ng-template #submenuheader let-item>
                        <span class="text-primary font-bold">{{
                            item.label | translate: { count: 0 }
                        }}</span>
                    </ng-template>
                    <ng-template #item let-item>
                        <div class="flex gap-2 ml-2 p-2">
                            <span
                                [class]="'mr-2 flag flag-' + item.icon"
                                style="width: 18px; height: 12px"
                            ></span>
                            {{ item.label | translate }}
                        </div>
                    </ng-template>
                </p-menu>
            </div>
            <p-button
                type="button"
                (onClick)="toggleDarkMode()"
                [rounded]="true"
                [icon]="isDarkTheme() ? 'pi pi-moon' : 'pi pi-sun'"
                severity="secondary"
            />
            <div class="relative">
                <p-button
                    icon="pi pi-palette"
                    pStyleClass="@next"
                    enterFromClass="hidden"
                    enterActiveClass="animate-scalein"
                    leaveToClass="hidden"
                    leaveActiveClass="animate-fadeout"
                    [hideOnOutsideClick]="true"
                    type="button"
                    rounded
                />
                <app-configurator />
            </div>
        </div>
    `,
})
export class AppFloatingConfigurator {
    LayoutService = inject(LayoutService)
    translateService = inject(TranslateService)
    i18nService = inject(I18nService)

    isDarkTheme = computed(() => this.LayoutService.layoutConfig().darkTheme)

    langItems: MenuItem[] = this.i18nService.langsMenuItems()

    toggleDarkMode() {
        this.LayoutService.layoutConfig.update((state) => ({
            ...state,
            darkTheme: !state.darkTheme,
        }))
    }
}
