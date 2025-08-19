import { Routes } from '@angular/router'
import { AppGuard } from '../../../modules/shared/service/guard/app.guard'

export const DASHBOARD_ROUTES: Routes = [
    {
        path: 'dashboard',
        loadComponent: () =>
            import('./../views/dashboard/dashboard').then((m) => m.Dashboard),
        canActivate: [AppGuard],
    },
]
