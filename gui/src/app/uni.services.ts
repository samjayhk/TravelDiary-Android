import { Injectable } from '@angular/core';
import { Observable, of, Observer, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UniService {
    public title;
    public userView = false;
    public threadView = false;
    public writeView = false;

    public currentViewPage;
    public currentViewPage2;
    public currentPageRange;
    public currentTags;
    public currentPid;
    public currentPage;

    titleChange: Subject<string> = new Subject<string>();
    userChange: Subject<boolean> = new Subject<boolean>();
    threadViewChange: Subject<boolean> = new Subject<boolean>();
    pidChange: Subject<number> = new Subject<number>();
    pageChange: Subject<number> = new Subject<number>();
    writeViewChange: Subject<boolean> = new Subject<boolean>();
    pageRangeChange: Subject<number> = new Subject<number>();
    pageViewChange: Subject<number> = new Subject<number>();
    pageViewChange2: Subject<number> = new Subject<number>();
    tagsChange: Subject<number> = new Subject<number>();
    constructor() {
        this.title = "Travel Diary";
        this.userView = false;
        this.threadView = false;
        this.currentPid = '1';
        this.currentPage = '1';
        this.writeView = false;
        this.currentPageRange = 1;
        this.currentViewPage = 1;
        this.currentViewPage2 = 1;
        this.currentTags = 0;
    }

    setThreadTitle(title) {
        this.titleChange.next(title);
    }

    setWriteViewChange(open) {
        this.writeViewChange.next(open);
    }

    setCurrentTagsChange(range) {
        this.tagsChange.next(range);
    }

    setCurrentPageRangeChange(range) {
        this.pageRangeChange.next(range);
    }

    setUserViewChange(open) {
        this.userChange.next(open);
    }

    setThreadViewChange(open) {
        this.threadViewChange.next(open);
    }

    setCurrentPid(pid) {
        this.pidChange.next(pid);
    }

    setCurrentPage(page) {
        this.pageChange.next(page);
    }

    setCurrentViewPage(page) {
        this.pageViewChange.next(page);
    }

    setCurrentViewPage2(page) {
        this.pageViewChange2.next(page);
    }
}