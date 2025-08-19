import { Routes } from '@angular/router'
import { AppLayout } from './app/layout/component/app.layout'
import { AppGuard } from './app/modules/shared/service/guard/app.guard'
import { Landing } from './app/pages/landing/landing'
import { Notfound } from './app/pages/notfound/notfound'

export const appRoutes: Routes = [
    {
        path: 'data',
        component: AppLayout,
        loadChildren: () =>
            import(
                './app/context/dashboard/infrastructure/dashboard.routers'
            ).then((m) => m.DASHBOARD_ROUTES),
        canActivate: [AppGuard],
    },
    {
        path: 'admin',
        component: AppLayout,
        loadChildren: () =>
            import('./app/context/admin/infrastructure/settings.routers').then(
                (m) => m.SETTINGS_ROUTES
            ),
        canActivate: [AppGuard],
    },
    {
        path: 'library',
        component: AppLayout,
        loadChildren: () =>
            import('./app/context/library/infrastructure/library.routers').then(
                (m) => m.LIBRARY_ROUTES
            ),
        canActivate: [AppGuard],
    },
    { path: 'landing', component: Landing },
    { path: 'notfound', component: Notfound },
    {
        path: 'auth',
        loadChildren: () => import('./app/pages/auth/auth.routes'),
    },
    {
        path: '',
        redirectTo: 'data/dashboard',
        pathMatch: 'full',
    },
]
