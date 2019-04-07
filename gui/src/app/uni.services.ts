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

    public currentPid;
    public currentPage;

    userChange: Subject<boolean> = new Subject<boolean>();
    titleChange: Subject<string> = new Subject<string>();
    threadViewChange: Subject<boolean> = new Subject<boolean>();
    writeViewChange: Subject<boolean> = new Subject<boolean>();
    pidChange: Subject<string> = new Subject<string>();
    pageChange: Subject<string> = new Subject<string>();
    constructor() {
        this.title = "Travel Diary";
        this.userView = false;
        this.threadView = false;
        this.currentPid = '1';
        this.currentPage = '1';
        this.writeView = false;
    }

    setThreadTitle(title) {
        this.titleChange.next(title);
    }

    setWriteViewChange(open) {
        this.writeViewChange.next(open);
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
}