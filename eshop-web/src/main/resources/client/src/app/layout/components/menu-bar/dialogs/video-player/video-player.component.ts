import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-video-player',
    templateUrl: './video-player.component.html',
    styleUrls: ['./video-player.component.scss']
})
export class VideoPlayerComponent implements OnInit, AfterViewInit {

    @Input() videoTitle: string;
    @Input() url: string;
    @ViewChild('video', { static: true }) video: any;
    constructor(
        public bsModalRef: BsModalRef
    ) { }

    ngOnInit() {
    }

    ngAfterViewInit(): void {
        this.video.nativeElement.autoplay = true;
        this.video.nativeElement.load();
    }

}
