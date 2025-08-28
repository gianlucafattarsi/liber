import { CommonModule } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { CardModule } from 'primeng/card'
import { TagModule } from 'primeng/tag'
import { Book, BooksService } from '../../../../../../modules/openapi'

@Component({
    selector: 'app-dashboard-book',
    imports: [CommonModule, CardModule, FontAwesomeModule, TagModule, TranslatePipe],
    templateUrl: './dashboard-book.html',
    styleUrl: './dashboard-book.css',
})
export class DashboardBook implements OnInit {
    _book: Book | null = null

    constructor(private booksService: BooksService) {}
    ngOnInit(): void {
        this._book = null

        this.booksService.loadRandomBook().subscribe({
            next: (book) => {
                this._book = book
            },
            error: (err) => {},
        })
    }
}
