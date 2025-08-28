import { Injectable } from '@angular/core'
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router'
import { AuthService } from '../auth/auth-service'

@Injectable({
    providedIn: 'root',
})
export class AppGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router
    ) {}

    canActivate(route: ActivatedRouteSnapshot): boolean {
        if (!this.authService.isAuthenticated()) {
            this.router.navigate(['/auth/login']) // Redirect if unauthorized
            return false
        }
        //this.authService.isAuthenticated()

        const requiredRoles: string[] = route.data['roles']
        if (requiredRoles) {
            const hasAccess = requiredRoles.some((role) =>
                this.authService.hasRole(role)
            )

            if (!hasAccess) {
                this.router.navigate(['/auth/access']) // Redirect if unauthorized
                return false
            }
        }

        return true
    }
}
