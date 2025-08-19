import { CommonModule } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { Router, RouterModule } from '@angular/router'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { ButtonModule } from 'primeng/button'
import { CardModule } from 'primeng/card'
import { DividerModule } from 'primeng/divider'
import { InputTextModule } from 'primeng/inputtext'
import { SelectModule } from 'primeng/select'
import { StepperModule } from 'primeng/stepper'
import { TabsModule } from 'primeng/tabs'
import { Library, LibraryImporterService, LibraryImportParam, LibraryService } from '../../../../modules/openapi'
import { ErrorResponseService } from '../../../../modules/shared/service/errors/error-response-service'

@Component({
    selector: 'app-importer',
    imports: [
        CommonModule,
        CardModule,
        FontAwesomeModule,
        TranslatePipe,
        TabsModule,
        FormsModule,
        ButtonModule,
        SelectModule,
        DividerModule,
        StepperModule,
        InputTextModule,
        RouterModule,
    ],
    templateUrl: './importer.html',
    styleUrl: './importer.css',
})
export class Importer implements OnInit {
    libraryType: 'newLibrary' | 'existingLibrary' = 'newLibrary'

    availableImporters: string[] = []
    selectedImporter: string = ''

    importPath: string = ''

    availableLibraries: Library[] = []
    selectedLibrary: Library | null = null
    newLibraryName: string = ''
    newLibraryPath: string = ''

    constructor(
        private libraryImporterService: LibraryImporterService,
        private libraryService: LibraryService,
        private errorResponseService: ErrorResponseService,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.loadImporters()
        this.loadLibraries()
    }

    private loadImporters() {
        this.selectedImporter = ''

        this.libraryImporterService.loadImporters().subscribe({
            next: (value) => {
                this.availableImporters = value
            },
            error: (err) => {
                this.availableImporters = []
            },
        })
    }

    private loadLibraries() {
        this.selectedLibrary = null

        this.libraryService.loadLibraries().subscribe({
            next: (value) => {
                this.availableLibraries = value
            },
            error: (err) => {
                this.availableLibraries = []
                this.errorResponseService.showError(
                    err,
                    'libraries_comp.loading.failed.title',
                    'libraries_comp.loading.failed.message'
                )
            },
        })
    }

    isImporterParamValid() {
        return (
            !this.selectedImporter ||
            this.selectedImporter.trim() === '' ||
            !this.importPath ||
            this.importPath.trim() === ''
        )
    }

    isLibraryParamValid(): boolean {
        if (this.libraryType === 'newLibrary') {
            return this.newLibraryName.trim() !== '' && this.newLibraryPath.trim() !== ''
        } else {
            return this.selectedLibrary !== null
        }
    }

    import(test: boolean) {
        let importParam: LibraryImportParam

        if (this.libraryType === 'newLibrary') {
            importParam = {
                library: {
                    name: this.newLibraryName,
                    path: this.newLibraryPath,
                },
                importPath: this.importPath,
            }
        } else {
            importParam = {
                library: this.selectedLibrary!,
                importPath: this.importPath,
            }
        }

        this.router.navigate(['/library/importer/state'], {
            queryParams: { test, importer: 'Calibre', importParams: JSON.stringify(importParam) },
        })
    }
}
