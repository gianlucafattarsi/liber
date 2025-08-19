import { CommonModule } from '@angular/common'
import { Component, Input, OnInit, output } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { ConfirmationService, MenuItem } from 'primeng/api'
import { ButtonModule } from 'primeng/button'
import { CardModule } from 'primeng/card'
import { CheckboxModule } from 'primeng/checkbox'
import { ConfirmDialogModule } from 'primeng/confirmdialog'
import { InputGroupModule } from 'primeng/inputgroup'
import { InputGroupAddonModule } from 'primeng/inputgroupaddon'
import { InputTextModule } from 'primeng/inputtext'
import { PasswordModule } from 'primeng/password'
import { SelectModule } from 'primeng/select'
import {
    NewUserPayload,
    User,
    UserPayload,
    UsersService,
} from '../../../../../../modules/openapi'
import { AuthService } from '../../../../../../modules/shared/service/auth/auth-service'
import { ErrorResponseService } from '../../../../../../modules/shared/service/errors/error-response-service'
import { I18nService } from '../../../../../../modules/shared/service/i18n/i18n-service'
import { ToastService } from '../../../../../../modules/shared/service/toast/toast-service'

@Component({
    selector: 'app-user',
    imports: [
        CommonModule,
        FormsModule,
        CardModule,
        InputTextModule,
        CheckboxModule,
        ButtonModule,
        InputGroupModule,
        InputGroupAddonModule,
        PasswordModule,
        ConfirmDialogModule,
        TranslatePipe,
        SelectModule,
        FontAwesomeModule,
    ],
    templateUrl: 'user.component.html',
    styleUrl: 'user.component.css',
    providers: [ConfirmationService],
})
export class UtenteComponent implements OnInit {
    @Input()
    set user(value: User | null) {
        if (value) {
            this._id = value.id!
            this._userPayload = {
                userName: value.userName!,
                password: '',
                email: value.email!,
                lang: value.lang,
                administrator: value?.administrator!,
            }
        } else {
            this._id = null
            this._userPayload = {
                ...{
                    userName: '',
                    password: '',
                    email: '',
                    lang: this.i18nService.appLang(),
                    administrator: false,
                },
            }
        }

        const _loggedUser = this.authService.currentUserValue

        this._deleteDisabled =
            !this._id || _loggedUser?.userName == this._userPayload.userName

        this.selectedLang =
            this.availableLangs.find(
                (item) => item['lang'] === this._userPayload.lang
            ) || this.availableLangs[0]
    }
    _id: number | null
    _userPayload: UserPayload

    _deleteDisabled: boolean = true

    availableLangs: MenuItem[] = []
    selectedLang: MenuItem

    onClose = output<boolean>()

    constructor(
        private usersService: UsersService,
        private toastService: ToastService,
        private authService: AuthService,
        private errorResponseService: ErrorResponseService,
        private confirmationService: ConfirmationService,
        private i18nService: I18nService,
        private translateService: TranslateService
    ) {}

    ngOnInit(): void {
        this.availableLangs = this.i18nService.langsItems()
    }

    saveUser() {
        if (this._id) {
            this.editUser()
        } else {
            this.addUser()
        }
    }

    close() {
        this.onClose.emit(true)
    }

    editUser() {
        this._userPayload.lang = this.selectedLang['lang']
        this.usersService.updateUser(this._id!, this._userPayload).subscribe({
            next: (user) => {
                this.toastService.showSuccessToast(
                    'user_comp.edit.successfull.title',
                    'user_comp.edit.successfull.message'
                )
                this.close()

                // update logged user lang
                if (
                    this.authService.currentUserValue?.userName ===
                    user.userName
                ) {
                    this.authService.currentUserValue.lang = user.lang
                }
                this.i18nService.applyLang(user.lang)
            },
            error: (err) => {
                this.errorResponseService.showError(
                    err,
                    'user_comp.edit.failed.title',
                    'user_comp.edit.failed.message'
                )
            },
        })
    }

    addUser() {
        const newUserPayload: NewUserPayload = {
            userName: this._userPayload.userName,
            email: this._userPayload.email,
            lang: this.selectedLang['lang'],
        }
        this.usersService.createUser(newUserPayload).subscribe({
            next: () => {
                this.toastService.showSuccessToast(
                    'user_comp.new.successfull.title',
                    'user_comp.new.successfull.message'
                )
                this.close()
            },
            error: (err) => {
                this.errorResponseService.showError(
                    err,
                    'user_comp.new.failed.title',
                    'user_comp.new.failed.message'
                )
            },
        })
    }

    deleteUser(event: Event) {
        if (this._id) {
            this.confirmationService.confirm({
                target: event.target as EventTarget,
                message: this.translateService.instant(
                    'user_comp.delete.confirmation.message',
                    { userName: this._userPayload.userName }
                ),
                header: this.translateService.instant(
                    'user_comp.delete.confirmation.title'
                ),
                icon: 'pi pi-info-circle',
                rejectLabel: this.translateService.instant('button.undo'),
                acceptLabel: this.translateService.instant('button.delete'),
                rejectButtonProps: {
                    severity: 'secondary',
                    outlined: true,
                },
                acceptButtonProps: {
                    severity: 'danger',
                },

                accept: () => {
                    this.usersService.deleteUser(this._id!).subscribe({
                        next: () => {
                            this.toastService.showSuccessToast(
                                'user_comp.delete.successfull.title',
                                'user_comp.delete.successfull.message'
                            )
                            this.close()
                        },
                        error: (err) => {
                            this.errorResponseService.showError(
                                err,
                                'user_comp.delete.failed.title',
                                'user_comp.delete.failed.message'
                            )
                        },
                    })
                },
            })
        }
    }
}
