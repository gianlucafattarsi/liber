import { CommonModule } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { RouterModule } from '@angular/router'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { ButtonModule } from 'primeng/button'
import { CardModule } from 'primeng/card'
import { FieldsetModule } from 'primeng/fieldset'
import { PanelModule } from 'primeng/panel'
import { TableModule } from 'primeng/table'
import { Library, LibraryService } from '../../../../modules/openapi'
import { ErrorResponseService } from '../../../../modules/shared/service/errors/error-response-service'
import { LibraryInfoComponent } from '../library-info/library-info'

@Component({
    selector: 'app-libraries',
    imports: [
        CommonModule,
        CardModule,
        FontAwesomeModule,
        TranslatePipe,
        FieldsetModule,
        TableModule,
        ButtonModule,
        PanelModule,
        RouterModule,
        LibraryInfoComponent,
    ],
    templateUrl: './libraries.html',
    styleUrl: './libraries.css',
})
export class Libraries implements OnInit {
    libraries$: Library[] = []

    selectedLibrary: Library | undefined

    constructor(
        private libraryService: LibraryService,
        private errorResponseService: ErrorResponseService
    ) {}

    ngOnInit(): void {
        this.loadLibraries()
    }

    loadLibraries() {
        this.selectedLibrary = undefined

        this.libraryService.loadLibraries().subscribe({
            next: (value) => {
                this.libraries$ = value
            },
            error: (err) => {
                this.libraries$ = []
                this.errorResponseService.showError(
                    err,
                    'libraries_comp.loading.failed.title',
                    'libraries_comp.loading.failed.message'
                )
            },
        })
    }
}
