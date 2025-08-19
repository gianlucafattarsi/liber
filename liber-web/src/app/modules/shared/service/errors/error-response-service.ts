import { Injectable } from '@angular/core'
import { TranslateService } from '@ngx-translate/core'
import { ToastService } from '../../../../modules/shared/service/toast/toast-service'
import { LiberErrorResponse } from '../../../openapi'

@Injectable({
    providedIn: 'root',
})
export class ErrorResponseService {
    constructor(
        private toastService: ToastService,
        private translateService: TranslateService
    ) {}

    public showError(
        error: any,
        defaultTitle: string,
        defaultMessage: string,
        toastkey: string = ToastService.TOAST_KEY
    ) {
        if (!error) {
            return
        }
        if (!error.error) {
            this.toastService.showErrorToast(
                defaultTitle,
                defaultMessage,
                false,
                toastkey
            )
        }

        try {
            const e: LiberErrorResponse = error.error
            this.parseErrorResponse(e, toastkey)
        } catch (e: any) {
            this.toastService.showErrorToast(
                defaultTitle,
                defaultMessage,
                false,
                toastkey
            )
        }
    }

    private parseErrorResponse(error: LiberErrorResponse, toastkey: string) {
        let message: string = error.error.userMessage

        if (error.error.constraintErrors) {
            message = message + '<br><br>'
            error.error.constraintErrors.forEach((contraint) => {
                const fieldName = this.translateService.instant(
                    (contraint.fieldName ??= '')
                )

                message = message + `<b>${fieldName}</b><br>`
                if (contraint.constraintsNotRespected) {
                    contraint.constraintsNotRespected?.forEach((constr) => {
                        //message += `<b>&#187;</b>  ${this.parseConstraintMessage(constr)}<br>`
                        message += `<b>&#187;</b>  ${constr}<br>`
                    })
                    message += '<br>'
                }
            })
        }

        this.toastService.showErrorToast(
            error.error.userTitle,
            message,
            false,
            toastkey
        )
    }

    // private parseConstraintMessage(message: string) {
    //     let value = message

    //     for (const key in this.i18Tables) {
    //         value = value.replace(key, this.i18Tables[key])
    //     }

    //     return value
    // }
}
