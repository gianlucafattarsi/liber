import { Component } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { Router, RouterModule } from '@angular/router'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { ButtonModule } from 'primeng/button'
import { CheckboxModule } from 'primeng/checkbox'
import { ImageModule } from 'primeng/image'
import { InputTextModule } from 'primeng/inputtext'
import { PasswordModule } from 'primeng/password'
import { RippleModule } from 'primeng/ripple'
import { Toast } from 'primeng/toast'
import { AppFloatingConfigurator } from '../../layout/component/app.floatingconfigurator'
import { NewUserPayload, UsersService } from '../../modules/openapi'
import { ErrorResponseService } from '../../modules/shared/service/errors/error-response-service'
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
        <p-toast
            key="app-toast-signup"
            [showTransitionOptions]="'250ms'"
            [showTransformOptions]="'translateY(100%)'"
            [hideTransitionOptions]="'150ms'"
            [hideTransformOptions]="'translateY(100%)'"
            position="center"
        >
            <ng-template let-message #message>
                <div class="flex flex-col items-start flex-auto">
                    <div class="flex items-center gap-2">
                        <i [class]="message.icon"></i>
                        <span class="font-medium text-lg">{{
                            message.summary
                        }}</span>
                    </div>
                    <div class="my-4" [innerHTML]="message.detail"></div>
                </div>
            </ng-template>
        </p-toast>
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
                                {{ 'signup.title' | translate }}
                            </div>
                            <div class="mt-4 flex items-center justify-center">
                                <label
                                    class="block text-surface-900 dark:text-surface-0 font-medium"
                                    >{{
                                        'signup.sign-in-message' | translate
                                    }}</label
                                >
                                <p-button
                                    link
                                    label="{{ 'sign-in' | translate }}"
                                    routerLink="/auth/login"
                                ></p-button>
                            </div>
                        </div>
                        <div>
                            <label
                                for="username"
                                class="block text-surface-900 dark:text-surface-0 text-xl font-medium mb-2"
                                >{{ 'userName' | translate }}</label
                            >
                            <input
                                pInputText
                                id="username"
                                type="text"
                                class="w-full md:w-[30rem] mb-8"
                                required
                                [(ngModel)]="userName"
                            />

                            <label
                                for="email"
                                class="block text-surface-900 dark:text-surface-0 font-medium text-xl mb-2"
                                >{{ 'email' | translate }}</label
                            >
                            <input
                                pInputText
                                id="email"
                                type="text"
                                class="w-full md:w-[30rem] mb-2"
                                required
                                [(ngModel)]="email"
                            />

                            <label
                                for="email"
                                class="block text-surface-900 dark:text-surface-0 font-medium text-xl mb-2"
                            ></label>
                            <p-button
                                label="{{ 'sign-in' | translate }}"
                                styleClass="w-full md:w-[30rem] mb-2"
                                [disabled]="
                                    !email ||
                                    email.trim().length === 0 ||
                                    !userName ||
                                    userName.trim().length === 0
                                "
                                (onClick)="login()"
                            ></p-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
})
export class SignUp {
    userName: string = ''

    email: string = ''

    constructor(
        private usersService: UsersService,
        private toastService: ToastService,
        private router: Router,
        private errorResponseService: ErrorResponseService,
        private translateService: TranslateService
    ) {}

    async login() {
        const payload: NewUserPayload = {
            userName: this.userName,
            email: this.email,
            lang: this.translateService.currentLang,
        }

        this.usersService.createUser(payload).subscribe({
            next: () => {
                this.toastService.showSuccessToast(
                    'signup.successfull.title',
                    'signup.successfull.message',
                    false,
                    'app-toast-signup'
                )
                this.router.navigate(['/auth/login'])
            },
            error: (err) => {
                this.errorResponseService.showError(
                    err,
                    'signup.failed.title',
                    'signup.failed.message',
                    'app-toast-signup'
                )
            },
        })
    }
}
