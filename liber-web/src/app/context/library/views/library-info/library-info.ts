import { CommonModule } from '@angular/common'
import { Component, Input } from '@angular/core'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { LibraryInfo, LibraryInfoService } from '../../../../modules/openapi'
import { ErrorResponseService } from '../../../../modules/shared/service/errors/error-response-service'

@Component({
    selector: 'app-library-info',
    imports: [CommonModule, FontAwesomeModule, TranslatePipe],
    templateUrl: './library-info.html',
    styleUrl: './library-info.css',
})
export class LibraryInfoComponent {
    @Input()
    set libraryId(id: number | undefined) {
        this.loadInfo(id)
    }

    libInfo: LibraryInfo | null = null

    constructor(
        private libraryInfoService: LibraryInfoService,
        private errorResponseService: ErrorResponseService
    ) {}

    loadInfo(id?: number) {
        this.libInfo = null

        if (id) {
            this.libraryInfoService.loadLibraryInfo(id).subscribe({
                next: (value) => {
                    this.libInfo = value
                },
                error: (err) => {
                    this.errorResponseService.showError(
                        err,
                        'library_info_comp.loading.failed.title',
                        'library_info_comp.loading.failed.message'
                    )
                },
            })
        }
    }
}
