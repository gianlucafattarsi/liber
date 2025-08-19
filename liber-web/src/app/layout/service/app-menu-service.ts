import { Injectable } from '@angular/core'
import { BehaviorSubject, Observable } from 'rxjs'

import { MenuItem } from 'primeng/api'

@Injectable({
    providedIn: 'root',
})
export class AppMenuService {
    private menuItemsSubject = new BehaviorSubject<MenuItem[]>([])

    constructor() {
        this.menuItemsSubject.next([])
    }

    public get menuItems(): Observable<MenuItem[]> {
        return this.menuItemsSubject.asObservable()
    }

    public addMenu(menuItem: MenuItem) {
        let currentMenu = this.menuItemsSubject.value

        let menuAdded = false
        currentMenu.forEach((item) => {
            if (item.label == menuItem.label) {
                menuItem.items?.forEach((child) => item.items?.push(child))
                menuAdded = true
            }
        })

        if (!menuAdded) {
            currentMenu.push(menuItem)
        }
        this.menuItemsSubject.next(currentMenu)
    }
}
