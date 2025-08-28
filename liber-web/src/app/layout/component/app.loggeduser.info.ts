import { CommonModule } from '@angular/common'
import { Component, signal } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { ButtonModule } from 'primeng/button'
import { SelectButtonModule } from 'primeng/selectbutton'
import { AuthService } from '../../modules/shared/service/auth/auth-service'
import { LayoutService } from '../service/layout.service'

@Component({
    selector: 'app-loggeduser-info',
    standalone: true,
    imports: [CommonModule, FormsModule, SelectButtonModule, ButtonModule],
    template: `
        <div class="flex flex-col gap-4">
            <div>
                <p class="text-muted-color font-semibold whitespace-nowrap">
                    <i class="mr-2 pi pi-envelope"></i>
                    {{ email() }}
                </p>
            </div>

            <div>
                <p class="text-muted-color font-semibold whitespace-nowrap">
                    <i class="mr-2 pi pi-user"></i>
                    {{ admin() }}
                </p>
            </div>

            <div>
                <p-button
                    label="Logout"
                    icon="pi pi-sign-out"
                    [style]="{ width: '100%' }"
                    (click)="logout()"
                />
            </div>
        </div>
    `,
    host: {
        class: 'hidden absolute top-[3.25rem] right-0 w-72 p-4 bg-surface-0 dark:bg-surface-900 border border-surface rounded-border origin-top shadow-[0px_3px_5px_rgba(0,0,0,0.02),0px_0px_2px_rgba(0,0,0,0.05),0px_1px_4px_rgba(0,0,0,0.08)]',
    },
    styles: `
        p-button > button {
            background-color: 'green';
            width: 100%;
        }
    `,
})
export class AppLoggedUserInfo {
    email = signal<string | null>(null)
    admin = signal<string | null>(null)

    constructor(
        public layoutService: LayoutService,
        private authService: AuthService
    ) {
        this.authService.currentUser.subscribe((user) => {
            this.email.set(null)
            this.admin.set('Standard user')
            if (user) {
                this.email.set(user.email)
                if (user.roles.find((r) => r == 'USER_ADMIN')) {
                    this.admin.set('Admin user')
                }
            }
        })
    }

    logout() {
        this.authService.logout()
    }
}
