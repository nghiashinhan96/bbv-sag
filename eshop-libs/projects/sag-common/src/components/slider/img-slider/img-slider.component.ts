import { Component, OnInit, Input, AfterViewInit, OnDestroy } from '@angular/core';
declare var Swiper: any;
@Component({
    selector: 'sag-common-img-slider',
    templateUrl: './img-slider.component.html',
    styleUrls: ['./img-slider.component.scss']
})
export class SagCommonImgSliderComponent implements OnInit, AfterViewInit, OnDestroy {

    @Input() images: any[] = [];
    @Input() viewId: string;

    link = '';
    itemsPerSlide = 5;
    hideControl = false;
    isZoomable = true;
    galleryThumbs;
    galleryTop;

    constructor() { }

    ngAfterViewInit(): void {
        setTimeout(() => {
            this.galleryThumbs = new Swiper('.gallery-thumbs', {
                spaceBetween: 10,
                slidesPerView: this.itemsPerSlide,
                freeMode: true,
                watchSlidesVisibility: true,
                watchSlidesProgress: true,
                centerInsufficientSlides: true,
            });
            this.galleryTop = new Swiper('.gallery-top', {
                spaceBetween: 10,
                preloadImages: true,
                updateOnImagesReady: true,
                watchOverflow: true,
                thumbs: {
                    swiper: this.galleryThumbs
                },
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
            });

            this.resizeImg();
            this.checkZoomable();

            this.galleryTop.on('slideChangeTransitionStart', () => {
                this.checkZoomable();
            });
            this.galleryTop.on('imagesReady', () => {
                this.resizeImg();
            });
        });

    }


    ngOnInit() {
        this.hideControl = this.images.length <= this.itemsPerSlide;
        this.link = this.images[0].ref;
    }

    ngOnDestroy(): void {
        if (this.galleryThumbs) {
            this.galleryThumbs.destroy();
        }

        if (this.galleryTop) {
            this.galleryTop.destroy();
        }
    }

    select(slide) {
        this.link = slide.ref;
    }

    zoom(e) {
        let offsetX;
        let offsetY;
        const pageX = e.touches ? e.touches[0].pageX : 0;
        const zoomer = e.currentTarget;
        e.offsetX ? offsetX = e.offsetX : offsetX = pageX;
        e.offsetY ? offsetY = e.offsetY : offsetX = pageX;
        const x = offsetX / zoomer.offsetWidth * 100;
        const y = offsetY / zoomer.offsetHeight * 100;
        zoomer.style.backgroundPosition = x + '% ' + y + '%';
    }

    private checkZoomable() {
        const elRoot = document.querySelector('.swiper-slide-active');
        const width = elRoot.getBoundingClientRect().width;
        const minImgSize = width * 0.66;

        const elImg = document.querySelector('.swiper-slide-active .zoom');
        const imgWidth = elImg.getBoundingClientRect().width;

        this.isZoomable = imgWidth > minImgSize;
    }

    private resizeImg() {
        const elRoot = document.querySelector('.swiper-slide-active');
        const width = elRoot.getBoundingClientRect().width;
        const minImgSize = width * 0.66;

        const els = document.getElementsByClassName('zoom') as HTMLCollectionOf<HTMLElement>;
        // tslint:disable-next-line: prefer-for-of
        for (let i = 0; i < els.length; i++) {
            const el = els[i];
            const imgWidth = el.getBoundingClientRect().width;
            const isZoomable = imgWidth > minImgSize;
            if (isZoomable) {
                el.style.width = width * 0.7 + 'px';
                el.style.backgroundSize = 'auto';
            } else {
                el.style.backgroundSize = 'cover';
            }
        }
    }
}
