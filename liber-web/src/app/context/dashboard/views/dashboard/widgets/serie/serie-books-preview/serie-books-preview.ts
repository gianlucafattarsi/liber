import { CommonModule } from '@angular/common'
import { Component, Input, OnInit } from '@angular/core'
import { CarouselModule } from 'primeng/carousel'
import { TagModule } from 'primeng/tag'
import { Book } from '../../../../../../../modules/openapi'

@Component({
    selector: 'app-serie-books-preview',
    imports: [CommonModule, CarouselModule, TagModule],
    templateUrl: './serie-books-preview.html',
    styleUrl: './serie-books-preview.css',
})
export class SerieBooksPreview implements OnInit {
    ngOnInit(): void {
        this.responsiveOptions = [
            {
                breakpoint: '1400px',
                numVisible: 2,
                numScroll: 1,
            },
            {
                breakpoint: '1199px',
                numVisible: 3,
                numScroll: 1,
            },
            {
                breakpoint: '767px',
                numVisible: 2,
                numScroll: 1,
            },
            {
                breakpoint: '575px',
                numVisible: 1,
                numScroll: 1,
            },
        ]
    }
    @Input() books: Book[] = []

    responsiveOptions: any[] | undefined
}
