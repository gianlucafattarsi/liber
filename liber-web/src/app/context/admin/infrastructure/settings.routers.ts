import { Routes } from '@angular/router'
import { AppGuard } from '../../../modules/shared/service/guard/app.guard'

export const SETTINGS_ROUTES: Routes = [
    {
        path: 'users',
        loadComponent: () =>
            import('./../account/users/views/users/users.component').then(
                (m) => m.UsersComponent
            ),
        canActivate: [AppGuard],
        data: { roles: ['USER_ADMIN'] },
    },
    {
        path: '',
        redirectTo: 'users',
        pathMatch: 'full',
    },
]
