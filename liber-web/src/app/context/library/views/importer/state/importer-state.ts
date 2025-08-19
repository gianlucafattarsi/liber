import { CommonModule } from '@angular/common'
import { Component, OnDestroy, OnInit } from '@angular/core'
import { ActivatedRoute } from '@angular/router'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { CompatClient, Stomp, StompSubscription } from '@stomp/stompjs'
import { ButtonModule } from 'primeng/button'
import { CardModule } from 'primeng/card'
import { TableModule } from 'primeng/table'
import { environment } from '../../../../../../environments/environment'
import { LibraryImporterService, LibraryImportParam } from '../../../../../modules/openapi'
import { AuthService } from '../../../../../modules/shared/service/auth/auth-service'
import { LogService } from '../../../../../modules/shared/service/log/log.service'
import { ImporterMessage } from './importer-message'

@Component({
    selector: 'app-importer-state',
    imports: [CommonModule, CardModule, FontAwesomeModule, TranslatePipe, TableModule, ButtonModule],
    templateUrl: './importer-state.html',
    styleUrl: './importer-state.css',
})
export class ImporterState implements OnInit, OnDestroy {
    private connection: CompatClient | undefined = undefined
    private subscription: StompSubscription | undefined

    messages: ImporterMessage[] = []

    test: boolean = true
    importer: string = ''
    importParams: LibraryImportParam | null = null

    constructor(
        private logService: LogService,
        private route: ActivatedRoute,
        private libraryImporterService: LibraryImporterService,
        private authService: AuthService
    ) {}

    ngOnInit(): void {
        this.messages = []

        this.route.queryParams.subscribe((params) => {
            this.test = params['test'] === 'true'
            this.importer = params['importer']
            this.importParams = JSON.parse(params['importParams'])

            this.connection = Stomp.client(environment.websocketUri)
            if (this.connection) {
                this.connection.connect({}, () => {
                    this.subscription = this.connection!.subscribe('/topic/library-import', (message) =>
                        this.handleMessage(JSON.parse(message.body))
                    )

                    if (this.test === true) {
                        this.startTestImport()
                    } else {
                        this.startImport()
                    }
                })
            }
        })
    }

    private startTestImport() {
        if (this.importer != '' && this.importParams !== null) {
            this.libraryImporterService.testImporterConnection(this.importer, this.importParams).subscribe({
                next: () => {},
            })
        }
    }

    startImport() {
        this.test = false
        this.messages = []
        if (this.importer != '' && this.importParams !== null) {
            this.libraryImporterService.importData(this.importer, this.importParams).subscribe({
                next: () => {},
            })
        }
    }

    handleMessage(message: ImporterMessage) {
        this.logService.debug(`========= New message ===========================`)
        this.logService.debug(`Message: ${message.message}`)
        this.logService.debug(`Error: ${message.error}`)
        this.logService.debug(`UserName: ${message.username}`)
        if (this.authService.currentUserValue?.userName === message.username) {
            this.messages.unshift(message)
        }
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe()
        }
        this.connection?.disconnect()
    }
}
