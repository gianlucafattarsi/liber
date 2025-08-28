import {
    HttpErrorResponse,
    HttpEvent,
    HttpHandlerFn,
    HttpRequest,
} from '@angular/common/http'
import { inject } from '@angular/core'
import { Router } from '@angular/router'
import { catchError, Observable, switchMap, throwError } from 'rxjs'
import { AuthService } from '../auth/auth-service'
import { TokenService } from '../auth/token-service'
import { I18nService } from '../i18n/i18n-service'

let isRefreshing = false
export function JwtTokenInterceptor(
    req: HttpRequest<unknown>,
    next: HttpHandlerFn
) {
    const tokenService = inject(TokenService)
    const authService = inject(AuthService)
    const router = inject(Router)
    const i18nService = inject(I18nService)

    req = req.clone({
        headers: req.headers.append('Accept-Language', i18nService.appLang()),
    })

    if (
        req.url.includes('/api/auth') ||
        (req.method === 'POST' && req.url.endsWith('/api/users'))
    ) {
        return next(req)
    }

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 403) {
                handle403Error(router)
            }
            if (!req.url.includes('api/auth/') && error.status === 401) {
                return handle401Error(authService, tokenService, req, next)
            }

            return throwError(() => error)
        })
    )
}

function handle401Error(
    authService: AuthService,
    tokenService: TokenService,
    request: HttpRequest<any>,
    next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
    if (!isRefreshing) {
        isRefreshing = true

        return authService.refreshToken().pipe(
            switchMap(() => {
                isRefreshing = false

                const reqCloned = request.clone({
                    headers: request.headers.set(
                        'Authorization',
                        'Bearer ' + tokenService.get()
                    ),
                })
                return next(reqCloned)
            }),
            catchError((error) => {
                isRefreshing = false

                if (error.status == '403' || error.status == '401') {
                    authService.logout()
                }

                return throwError(() => console.log('errore'))
            })
        )
    }
    return next(request)
}

function handle403Error(router: Router) {
    router.navigate(['/auth/access']) // Redirect if unauthorized
}
