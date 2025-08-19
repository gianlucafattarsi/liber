import { Injectable } from '@angular/core'
import { JwtHelperService } from '@auth0/angular-jwt'

@Injectable({
    providedIn: 'root',
})
export class TokenService {
    private static TOKEN_NAME = 'app_token'
    private static REFRESH_TOKEN_NAME = 'app_token_refresh'

    jwtHelper = new JwtHelperService()

    nomeUtente = ''
    mail: string
    lang: string = 'en'
    roles: string[] = []

    token: string | null = null
    refreshToken: string | null = null

    constructor() {
        this.getRefresh()
    }

    public get(): string {
        let token = localStorage.getItem(TokenService.TOKEN_NAME)

        if (this.jwtHelper.isTokenExpired(token)) {
            token = null
        }

        this.decodeToken(token)
        return token || ''
    }

    public getRefresh(): string {
        if (!this.refreshToken) {
            let token = localStorage.getItem(TokenService.REFRESH_TOKEN_NAME)

            this.refreshToken = token ?? ''
        }
        return this.refreshToken
    }

    private decodeToken(token?: string | null) {
        this.nomeUtente = ''
        this.roles = []

        if (token !== null && token !== undefined) {
            const tokenDecoded = this.jwtHelper.decodeToken(token)
            this.nomeUtente = tokenDecoded['sub']
            this.mail = tokenDecoded['mail']
            this.lang = tokenDecoded['lang']
            this.roles = tokenDecoded['roles']
        }
    }

    public getRoles(): string[] {
        let token = localStorage.getItem(TokenService.TOKEN_NAME)

        if (token !== null) {
            this.decodeToken(token)
            return this.roles
        }

        return []
    }

    public store(token: string, refreshToken: string) {
        this.remove()

        this.decodeToken(token)

        localStorage.setItem(TokenService.TOKEN_NAME, token)
        localStorage.setItem(TokenService.REFRESH_TOKEN_NAME, refreshToken)

        this.refreshToken = refreshToken
    }

    public remove() {
        localStorage.removeItem(TokenService.TOKEN_NAME)
        localStorage.removeItem(TokenService.REFRESH_TOKEN_NAME)

        this.nomeUtente = ''
        this.mail = ''
        this.lang = 'en'
        this.roles = []

        this.refreshToken = null
    }
}
