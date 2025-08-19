import { CommonModule } from '@angular/common'
import { Component, signal } from '@angular/core'
import { NavigationEnd, Router, RouterModule } from '@angular/router'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { MenuItem } from 'primeng/api'
import { PanelMenuModule } from 'primeng/panelmenu'
import { filter } from 'rxjs'
import { AppMenuService } from '../service/app-menu-service'

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        PanelMenuModule,
        TranslatePipe,
        FontAwesomeModule,
    ],
    template: `<p-panelmenu [model]="model()" [multiple]="true">
        <ng-template #item let-item let-idx="index">
            <a
                pRipple
                [routerLink]="item.routerLink"
                class="flex items-center px-4 py-2 cursor-pointer group"
            >
                <fa-icon
                    [icon]="['fas', item.icon]"
                    class="flex items-center"
                    style="color: var(--primary-color) !important"
                ></fa-icon>

                <span *ngIf="item.routerLink" class="ml-2" [style]="item.style">
                    {{ item.label | translate }}
                </span>
                <span
                    *ngIf="!item.routerLink"
                    class="ml-2"
                    style="font-weight: bold;text-transform: uppercase;"
                >
                    {{ item.label }}
                </span>
            </a>
        </ng-template>
    </p-panelmenu> `,
})
export class AppMenu {
    model = signal<MenuItem[]>([])

    constructor(
        private appMenuService: AppMenuService,
        public router: Router
    ) {
        this.appMenuService.menuItems.subscribe((items) => {
            this.model.set(items)
        })

        this.router.events
            .pipe(filter((event) => event instanceof NavigationEnd))
            .subscribe((params) => {
                this.model().forEach((item) =>
                    this.callRecursively(item, params.urlAfterRedirects)
                )
            })
    }

    callRecursively(item: MenuItem, link: string) {
        item.style = {}
        if (item.routerLink == link) {
            item.style = { color: 'var(--primary-color) !important' }
        }
        if (item.items) {
            item.items.forEach((itemChild) => {
                this.callRecursively(itemChild, link)
            })
        }
    }
}
