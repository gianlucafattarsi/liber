import { CommonModule } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { ButtonModule } from 'primeng/button'
import { CardModule } from 'primeng/card'
import { DialogModule } from 'primeng/dialog'
import { TableModule } from 'primeng/table'
import { User, UsersService } from '../../../../../../modules/openapi'
import { ErrorResponseService } from '../../../../../../modules/shared/service/errors/error-response-service'
import { UtenteComponent } from '../user/user.component'

@Component({
    selector: 'app-utenti',
    imports: [
        CommonModule,
        CardModule,
        FontAwesomeModule,
        TableModule,
        ButtonModule,
        DialogModule,
        TranslatePipe,
        UtenteComponent,
    ],
    templateUrl: 'users.component.html',
    styleUrl: 'users.component.css',
})
export class UsersComponent implements OnInit {
    _users: User[] = []

    _userDialogVisible = false

    _selectedUser: User | null

    constructor(
        private usersService: UsersService,
        private errorResponseService: ErrorResponseService
    ) {}

    ngOnInit(): void {
        this.usersService.loadUsers().subscribe({
            next: (results) => {
                this._users = results
            },
            error: (err) => {
                this._users = []
                this.errorResponseService.showError(
                    err,
                    'users.loading.failed.title',
                    'users.loading.failed.message'
                )
            },
        })
    }

    openUtenteDialog(utente: User | null) {
        this._selectedUser = utente
        this._userDialogVisible = true
    }
}
