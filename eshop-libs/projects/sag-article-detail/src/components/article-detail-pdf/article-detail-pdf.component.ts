import {
    Component,
    OnInit,
    Input,
    ViewChild,
    ElementRef,
    Renderer2,
    HostListener,
    ChangeDetectionStrategy,
    ChangeDetectorRef
} from '@angular/core';
import { ArticleModel } from '../../models/article.model';

const MAX_WIDTH = 340;
const MAX_SIZE = 10;
const PDF_ITEM_WIDTH = 34;

@Component({
    selector: 'sag-article-detail-pdf',
    templateUrl: './article-detail-pdf.component.html',
    styleUrls: ['./article-detail-pdf.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SagArticleDetailPdfComponent implements OnInit {

    @Input() article: ArticleModel;
    @Input() viewId: string;

    pdfs: any[] = [];
    totalItems;
    position = 0;
    slides = [];
    left = 0;
    shown = false;
    contentWidth = 0;

    private documentListener: any;

    @ViewChild('pdfSlider', { static: false }) pdfSlider: ElementRef;
    @ViewChild('rightHand', { static: false }) rightHand: any;
    @ViewChild('leftHand', { static: false }) leftHand: any;
    constructor(private renderer: Renderer2, private cdr: ChangeDetectorRef) { }

    ngOnInit(): void {
        const sources = this.article.images || [];
        this.pdfs = sources.filter(image => image.img_typ === 'document');
        this.totalItems = this.pdfs.length;
        const size = Math.round(this.totalItems / MAX_SIZE + 0.5);
        this.contentWidth = Math.min(this.totalItems * PDF_ITEM_WIDTH, MAX_WIDTH);
        for (let i = 0; i < size; i++) {
            this.slides.push({
                next: i < (size - 1) && size > 1,
                prev: i > 0 && size > 1
            });
        }
    }

    @HostListener('click', ['$event'])
    onClickElement(event) {
        if (this.pdfSlider) {
            const checkClick = this.pdfSlider.nativeElement.contains(event.target);
            if (checkClick) {
                // do not excute document click;
                event.stopPropagation();
            }
        }
    }

    showPanel() {
        // if only a pfd file then open it
        if (this.totalItems === 1) {
            const win = window.open(this.pdfs[0].ref, '_blank');
            win.focus();
        } else {
            if (this.shown) {
                return;
            }
            this.shown = true;
            this.left = 0;
            this.position = 0;
            // binding event to close when click outside
            this.cdr.detectChanges();
            setTimeout(() => {
                this.bindDocumentEditListener();
            });
        }
    }

    nextSlide() {
        if (this.position > (this.slides.length - 1)) {
            this.position = this.slides.length - 1;
        }
        this.position += 1;
        this.left -= MAX_WIDTH;
        this.cdr.detectChanges();
    }

    prevSlide() {
        if (this.position < 0) {
            this.position = 0;
        }
        this.position -= 1;
        this.left += MAX_WIDTH;
        this.cdr.detectChanges();
    }

    private bindDocumentEditListener() {
        if (!this.documentListener) {
            this.documentListener = this.renderer.listen('document', 'click', () => {
                this.unbindDocumentEditListener();
            });
        }
    }

    private unbindDocumentEditListener() {
        if (this.documentListener) {
            this.documentListener();
            this.documentListener = null;
            this.shown = false;
            this.cdr.markForCheck();
        }
    }
}
