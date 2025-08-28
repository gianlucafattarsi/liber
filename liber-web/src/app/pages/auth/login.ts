import { Component } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { Router, RouterModule } from '@angular/router'
import { TranslatePipe } from '@ngx-translate/core'
import { ButtonModule } from 'primeng/button'
import { CheckboxModule } from 'primeng/checkbox'
import { ImageModule } from 'primeng/image'
import { InputTextModule } from 'primeng/inputtext'
import { PasswordModule } from 'primeng/password'
import { RippleModule } from 'primeng/ripple'
import { Toast } from 'primeng/toast'
import { AppFloatingConfigurator } from '../../layout/component/app.floatingconfigurator'
import { Session } from '../../modules/openapi'
import { AuthService } from '../../modules/shared/service/auth/auth-service'
import { I18nService } from '../../modules/shared/service/i18n/i18n-service'
import { ToastService } from '../../modules/shared/service/toast/toast-service'

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [
        ButtonModule,
        CheckboxModule,
        InputTextModule,
        PasswordModule,
        FormsModule,
        RouterModule,
        RippleModule,
        AppFloatingConfigurator,
        Toast,
        ImageModule,
        TranslatePipe,
    ],
    template: `
        <p-toast key="app-login-toast" position="center"></p-toast>
        <app-floating-configurator />
        <div
            class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden"
        >
            <div class="flex flex-col items-center justify-center">
                <div
                    style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%)"
                >
                    <div
                        class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20"
                        style="border-radius: 53px"
                    >
                        <div class="text-center mb-8">
                            <div class="flex justify-center">
                                <p-image
                                    src="./../../../assets/images/logo-slim.png"
                                    alt="Image"
                                    width="60"
                                />
                            </div>
                            <div
                                class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4"
                            >
                                {{ 'login.welcome-title' | translate }}
                            </div>
                            <span class="text-muted-color font-medium">{{
                                'login.sign-in' | translate
                            }}</span>
                        </div>
                        <div>
                            <label
                                for="email1"
                                class="block text-surface-900 dark:text-surface-0 text-xl font-medium mb-2"
                                >{{ 'userName' | translate }}</label
                            >
                            <input
                                pInputText
                                id="email1"
                                type="text"
                                placeholder="Username"
                                class="w-full md:w-[30rem] mb-8"
                                [(ngModel)]="userName"
                            />

                            <label
                                for="password1"
                                class="block text-surface-900 dark:text-surface-0 font-medium text-xl mb-2"
                                >{{ 'password' | translate }}</label
                            >
                            <p-password
                                id="password1"
                                [(ngModel)]="password"
                                placeholder="Password"
                                [toggleMask]="true"
                                styleClass="mb-4"
                                [fluid]="true"
                                [feedback]="false"
                            ></p-password>

                            <p-button
                                label="{{ 'sign-in' | translate }}"
                                styleClass="w-full"
                                routerLink="/"
                                (onClick)="login()"
                            ></p-button>

                            <div class="mt-4 flex items-center justify-center">
                                <label
                                    class="block text-surface-900 dark:text-surface-0 font-medium"
                                    >{{
                                        'login.sign-up-message' | translate
                                    }}</label
                                >
                                <p-button
                                    link
                                    label="{{ 'sign-up' | translate }}"
                                    routerLink="/auth/signup"
                                ></p-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
})
export class Login {
    userName: string = ''

    password: string = ''

    constructor(
        private authService: AuthService,
        private toastService: ToastService,
        private router: Router,
        private i18nService: I18nService
    ) {}

    async login() {
        const session: Session | null = await this.authService.login(
            this.userName,
            this.password
        )

        if (!session) {
            this.toastService.showErrorToast(
                'login.failed.title',
                'login.failed.message',
                false,
                'app-login-toast'
            )
        } else {
            this.i18nService.applyLang(
                this.authService.currentUserValue?.lang || 'en'
            )
            this.router.navigate([''])
        }
    }
}
