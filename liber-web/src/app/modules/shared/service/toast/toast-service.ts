import { Injectable } from '@angular/core'
import { TranslateService } from '@ngx-translate/core'
import { MessageService } from 'primeng/api'

@Injectable({
    providedIn: 'root',
})
export class ToastService {
    public static readonly TOAST_KEY: string = 'app-toast2'

    constructor(
        private msgService: MessageService,
        private translateService: TranslateService
    ) {}

    async showSuccessToast(
        summary: string,
        detail: string,
        sticky: boolean = false,
        toastKey: string = ToastService.TOAST_KEY
    ): Promise<void> {
        this.showToast(
            summary,
            detail,
            'success',
            sticky,
            'pi pi-check',
            toastKey
        )
    }

    async showInfoToast(
        summary: string,
        detail: string,
        sticky: boolean = false,
        toastKey: string = ToastService.TOAST_KEY
    ): Promise<void> {
        this.showToast(
            summary,
            detail,
            'info',
            sticky,
            'pi pi-info-circle',
            toastKey
        )
    }
    async showWarnToast(
        summary: string,
        detail: string,
        sticky: boolean = false,
        toastKey: string = ToastService.TOAST_KEY
    ): Promise<void> {
        this.showToast(
            summary,
            detail,
            'warn',
            sticky,
            'pi pi-exclamation-triangle',
            toastKey
        )
    }

    async showErrorToast(
        summary: string,
        detail: string,
        sticky: boolean = false,
        toastKey: string = ToastService.TOAST_KEY
    ): Promise<void> {
        this.showToast(
            summary,
            detail,
            'error',
            sticky,
            'pi pi-times-circle',
            toastKey
        )
    }

    async showToast(
        summary: string,
        detail: string,
        severity: string,
        sticky: boolean,
        icon: string,
        toastKey: string = ToastService.TOAST_KEY
    ): Promise<void> {
        this.msgService.add({
            key: toastKey,
            severity: severity,
            summary: this.translateService.instant(summary),
            detail: this.translateService.instant(detail),
            sticky: sticky,
            icon: icon,
        })
    }
}
