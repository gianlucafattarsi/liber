import { CommonModule } from '@angular/common'
import { Component, Input } from '@angular/core'
import { BadgeModule } from 'primeng/badge'
import { DataViewModule } from 'primeng/dataview'
import { TagModule } from 'primeng/tag'
import { Book } from '../../../../../../../modules/openapi'

@Component({
    selector: 'app-serie-books-list',
    imports: [CommonModule, DataViewModule, BadgeModule, TagModule],
    templateUrl: './serie-books-list.html',
    styleUrl: './serie-books-list.css',
})
export class SerieBooksList {
    @Input() books: Book[] = []
}
