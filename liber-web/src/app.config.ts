import {
    HttpClient,
    provideHttpClient,
    withInterceptors,
} from '@angular/common/http'
import {
    ApplicationConfig,
    importProvidersFrom,
    inject,
    provideAppInitializer,
    provideZoneChangeDetection,
} from '@angular/core'
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async'
import {
    provideRouter,
    withEnabledBlockingInitialNavigation,
    withHashLocation,
    withInMemoryScrolling,
} from '@angular/router'
import {
    TranslateCompiler,
    TranslateLoader,
    TranslateModule,
} from '@ngx-translate/core'
import Aura from '@primeng/themes/aura'
import { TranslateMessageFormatCompiler } from 'ngx-translate-messageformat-compiler'
import { MessageService } from 'primeng/api'
import { providePrimeNG } from 'primeng/config'
import { appRoutes } from './app.routes'
import { AppMenuInitilizer } from './app/layout/service/app-menu-initializer'
import { Configuration } from './app/modules/openapi'
import { TokenService } from './app/modules/shared/service/auth/token-service'
import { MultiTranslateHttpLoader } from './app/modules/shared/service/i18n/multi-translate-http-loader'
import { JwtTokenInterceptor } from './app/modules/shared/service/interceptors/jwt-token.interceptor'
import { LogService } from './app/modules/shared/service/log/log.service'
import { environment } from './environments/environment'

export function HttpLoaderFactory(_httpBackend: HttpClient) {
    return new MultiTranslateHttpLoader(_httpBackend, [
        '/assets/i18n/ui/',
        '/assets/i18n/lang/',
        '/assets/i18n/components/',
        '/assets/i18n/fields/',
        '/assets/i18n/other/',
    ])
}

export const appConfig: ApplicationConfig = {
    providers: [
        MessageService,
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(
            appRoutes,
            withHashLocation(),
            withInMemoryScrolling({
                anchorScrolling: 'enabled',
                scrollPositionRestoration: 'enabled',
            }),
            withEnabledBlockingInitialNavigation()
        ),
        provideHttpClient(withInterceptors([JwtTokenInterceptor])),
        provideAnimationsAsync(),
        providePrimeNG({
            theme: {
                preset: Aura,
                options: { darkModeSelector: '.app-dark' },
            },
        }),
        provideAppInitializer(() => {
            inject(AppMenuInitilizer).init()
            inject(LogService).setLogLevel(environment.logLevel)
        }),
        importProvidersFrom([
            TranslateModule.forRoot({
                loader: {
                    provide: TranslateLoader,
                    useFactory: HttpLoaderFactory,
                    deps: [HttpClient],
                },
                compiler: {
                    provide: TranslateCompiler,
                    useClass: TranslateMessageFormatCompiler,
                },
            }),
        ]),
        {
            provide: Configuration,
            useFactory: (tokenService: TokenService) =>
                new Configuration({
                    basePath: environment.basePath,
                    withCredentials: false,
                    credentials: {
                        bearerAuth: (tokenService.get ?? '').bind(tokenService),
                    },
                }),
            deps: [TokenService, HttpClient],
            multi: false,
        },
    ],
}
