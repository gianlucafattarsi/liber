import { CommonModule } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { TranslatePipe } from '@ngx-translate/core'
import { CardModule } from 'primeng/card'
import { TagModule } from 'primeng/tag'
import { Author, Book, Publisher, Serie, SeriesService, Tag } from '../../../../../../modules/openapi'
import { SerieBooksList } from './serie-books-list/serie-books-list'
import { SerieBooksPreview } from './serie-books-preview/serie-books-preview'

@Component({
    selector: 'app-dashboard-serie',
    imports: [CommonModule, CardModule, FontAwesomeModule, TranslatePipe, TagModule, SerieBooksPreview, SerieBooksList],
    templateUrl: './dashboard-serie.html',
    styleUrl: './dashboard-serie.css',
})
export class DashboardSerie implements OnInit {
    _serie: Serie | null = null

    _books: Book[] = []
    _tags: Tag[] = []
    _authors: Author[] = []
    _publishers: Publisher[] = []

    constructor(private seriesService: SeriesService) {}
    ngOnInit(): void {
        this._serie = null
        this._books = []
        this._tags = []
        this._authors = []
        this._publishers = []

        this.seriesService.loadRandomSerie().subscribe({
            next: (serie) => {
                this._serie = serie
                if (this._serie?.books) {
                    this._books = Array.from(this._serie.books.values())

                    this.buildSerieAuthors()
                    this.buildSeriePublishers()
                    this.buildSerieTags()
                }
            },
            error: (err) => {
                this._serie = null
                this._books = []
                this._tags = []
                this._authors = []
                this._publishers = []
            },
        })
    }

    private buildSerieTags() {
        // retrieve all tags form the books
        let allTags: Tag[] | undefined = this._serie!.books!.flatMap((book) => (book.tags ? Array.from(book.tags) : []))

        // getting unique array values
        allTags = allTags.filter((value, index, self) => self.findIndex((tag) => tag.name === value.name) === index)

        if (allTags) {
            this._tags = allTags
        }
    }

    private buildSerieAuthors() {
        // retrieve all authors form the books
        let allAuthors: Author[] | undefined = this._serie!.books!.flatMap((book) =>
            book.authors ? Array.from(book.authors) : []
        )

        // getting unique array values
        allAuthors = allAuthors.filter(
            (value, index, self) => self.findIndex((tag) => tag.name === value.name) === index
        )

        if (allAuthors) {
            this._authors = allAuthors
        }
    }

    private buildSeriePublishers() {
        // retrieve all publishers form the books
        let allPublishers: Publisher[] | undefined = this._serie!.books!.flatMap((book) =>
            book.publishers ? Array.from(book.publishers) : []
        )

        // getting unique array values
        allPublishers = allPublishers.filter(
            (value, index, self) => self.findIndex((tag) => tag.name === value.name) === index
        )

        if (allPublishers) {
            this._publishers = allPublishers
        }
    }
}
