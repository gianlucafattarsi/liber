import { Routes } from '@angular/router'
import { AppGuard } from '../../../modules/shared/service/guard/app.guard'

export const LIBRARY_ROUTES: Routes = [
    {
        path: 'list',
        loadComponent: () => import('./../../library/views/libraries/libraries').then((m) => m.Libraries),
        canActivate: [AppGuard],
        data: { roles: ['USER_ADMIN'] },
    },
    {
        path: 'importer',
        loadComponent: () => import('./../../library/views/importer/importer').then((m) => m.Importer),
        canActivate: [AppGuard],
        data: { roles: ['USER_ADMIN'] },
    },
    {
        path: 'importer/state',
        loadComponent: () => import('./../../library/views/importer/state/importer-state').then((m) => m.ImporterState),
        canActivate: [AppGuard],
        data: { roles: ['USER_ADMIN'] },
    },
    {
        path: '',
        redirectTo: 'list',
        pathMatch: 'full',
    },
]
