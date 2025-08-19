import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Router } from '@angular/router'
import { JwtHelperService } from '@auth0/angular-jwt'
import { BehaviorSubject, firstValueFrom, map, Observable } from 'rxjs'
import { environment } from '../../../../../environments/environment'
import {
    AuthService as AuthAPIService,
    Session,
    UserLogin,
} from '../../../openapi'
import { LogService } from '../log/log.service'
import { LoggedUser } from './logged-user'
import { TokenService } from './token-service'

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    static LoginUser = class {
        id: number
        username: string
        password: string
        azienda: string
    }

    private currentUserSubject = new BehaviorSubject<LoggedUser | null>(null)

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router,
        private logService: LogService,
        private authApiService: AuthAPIService
    ) {}

    public isAuthenticated(): boolean {
        const token: any = this.tokenService.get()
        const helper = new JwtHelperService()
        const auth =
            !helper.isTokenExpired(token) ||
            this.tokenService.getRefresh() != ''
        if (auth) {
            const utente = new LoggedUser(
                this.tokenService.nomeUtente,
                this.tokenService.mail,
                this.tokenService.lang,
                this.tokenService.roles
            )
            this.currentUserSubject.next(utente)
        } else {
            this.currentUserSubject.next(null)
        }
        return auth
    }

    public get currentUser(): Observable<LoggedUser | null> {
        return this.currentUserSubject.asObservable()
    }

    public get currentUserValue(): LoggedUser | null {
        return this.currentUserSubject.getValue()
    }

    hasRole(role: string): boolean {
        const roles = this.tokenService.getRoles()
        if (roles) {
            return roles.includes(role)
        }
        return false
    }

    public async login(
        username: string,
        password: string
    ): Promise<Session | null> {
        this.logService.debug(
            `New login with parameters: username:${username}, password:${password}`
        )

        const loginUser: UserLogin = {
            username: username,
            password: password,
        }

        let session: Session | null
        try {
            session = await firstValueFrom(this.authApiService.login(loginUser))

            if (session?.accessToken && session?.refreshToken) {
                this.tokenService.store(
                    session.accessToken,
                    session.refreshToken
                )

                const utente = new LoggedUser(
                    this.tokenService.nomeUtente,
                    this.tokenService.mail,
                    this.tokenService.lang,
                    this.tokenService.roles
                )
                this.currentUserSubject.next(utente)
            } else {
                this.currentUserSubject.next(null)
            }
        } catch (e) {
            session = null
            this.currentUserSubject.next(null)
        }

        return session
    }

    public logout() {
        this.currentUserSubject.next(null)
        this.tokenService.remove()
        this.router.navigate(['auth/login'])
    }

    public refreshToken() {
        this.logService.debug('Refresh token call')
        return this.http
            .post<any>(
                `${environment.basePath}/api/auth/refresh-token`,
                `"${this.tokenService.getRefresh()}"`,
                {
                    headers: new HttpHeaders().append(
                        'Content-Type',
                        'application/json'
                    ),
                }
            )
            .pipe(
                map((tokenResponse) => {
                    this.logService.debug(
                        `New refresh token ${tokenResponse.refreshToken}`
                    )
                    this.tokenService.store(
                        tokenResponse.accessToken,
                        tokenResponse.refreshToken
                    )

                    const utente = new LoggedUser(
                        this.tokenService.nomeUtente,
                        this.tokenService.mail,
                        this.tokenService.lang,
                        this.tokenService.roles
                    )
                    this.currentUserSubject.next(utente)
                })
            )
    }
}
