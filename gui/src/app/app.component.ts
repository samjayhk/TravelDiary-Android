import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RestService } from './service';
import { ActivatedRoute, Router } from '@angular/router';
import { UniService } from './uni.services';
import { ToastrService } from 'ngx-toastr';
import {Location} from '@angular/common';

import { faSearch, faEdit, faTrashAlt, faArrowLeft, faRedoAlt, faPlus, faBars, faPassport, faCommentAlt, faCommentMedical } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  title;
  faArrowLeft = faArrowLeft;
  faEdit = faEdit;
  faSearch = faSearch;
  faTrashAlt = faTrashAlt;
  faRedo = faRedoAlt;
  faPlus = faPlus;
  faBars = faBars;
  faPassport = faPassport;
  faComment = faCommentAlt;
  faCommentMedical = faCommentMedical;
  
  currentPid;
  currentPage;

  session;
  localSession;

  searchView;
  threadView;
  userView;
  writeView;

  currentViewPage = 1;
  currnetViewTags = 0;
  currentViewPageRange = 1;
  keywords;

  _subscriptionPid;
  _subscriptionPage;
  _subscriptionCurrentPageRange;
  _subscriptionTitle;
  _subscriptionThread;
  _subscriptionUser;
  _subscriptionWrite;
  _subscriptionTags;
  _subscriptionViewPage;

  constructor(private changeDetectorRef: ChangeDetectorRef, private location: Location, private toastr: ToastrService, public rest:RestService, public uni: UniService, public actRoute: ActivatedRoute, public router: Router) { 
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.title = uni.title;
    if (uni.titleChange.observers.length === 0) {
      this._subscriptionTitle = uni.titleChange.subscribe((value) => {
        this.title = value;
      });
    }
    if (uni.threadViewChange.observers.length === 0) {
      this._subscriptionThread = uni.threadViewChange.subscribe((value) => {
        this.threadView = value;
      });
    }
    if (uni.userChange.observers.length === 0) {
      this._subscriptionUser = uni.userChange.subscribe((value) => {
        this.userView = value;
      });
    }
    if (uni.pidChange.observers.length === 0) {
      this._subscriptionTitle = uni.pidChange.subscribe((value) => {
        this.currentPid = value;
      });
    }
    if (uni.pageChange.observers.length === 0) {
      this._subscriptionPage = uni.pageChange.subscribe((value) => {
        this.currentPage = value;
      });
    }
    if (uni.writeViewChange.observers.length === 0) {
      this._subscriptionWrite = uni.writeViewChange.subscribe((value) => {
        this.writeView = value;
      });
    }
    if (uni.pageRangeChange.observers.length === 0) {
      this._subscriptionCurrentPageRange = uni.pageRangeChange.subscribe((value) => {
        this.currentViewPageRange = value;
      });
    }
    if (uni.pageViewChange.observers.length === 0) {
      this._subscriptionViewPage = uni.pageViewChange.subscribe((value) => {
        this.currentViewPage = value;
      });
    }
    if (uni.tagsChange.observers.length === 0) {
      this._subscriptionTags = uni.tagsChange.subscribe((value) => {
        this.currnetViewTags = value;
      });
    }
  }

  ngOnDestroy() {
    this._subscriptionPid.unsubscribe();
    this._subscriptionPage.unsubscribe();
    this._subscriptionCurrentPageRange.unsubscribe();
    this._subscriptionTitle.unsubscribe();
    this._subscriptionThread.unsubscribe();
    this._subscriptionUser.unsubscribe();
    this._subscriptionWrite.unsubscribe();
    this._subscriptionTags.unsubscribe();
    this._subscriptionViewPage.unsubscribe();
  }

  ngOnInit() {
  }

  arr(number) {
    var i:number; 
    var arrys = [];
    for(i = 0; i<number; i++) {
      arrys.push(i)
    }
    return arrys;
  }

  changePage(p) {
    this.uni.setCurrentViewPage2(p);
    this.currentViewPage = p;
    if (this.currnetViewTags == 0) {
      this.location.go('thread/' + p)
    } else {
      this.location.go('thread/tag/' + this.currnetViewTags + '/' + p)
    }
  }

  loadSession() {
    if (localStorage.getItem('traveldiaryv1')) {
      this.session = true;
      this.localSession = JSON.parse(localStorage.getItem('traveldiaryv1'));
      return true
    } else {
      localStorage.removeItem('traveldiaryv1');
      this.session = false;
      console.log("Please login and continue.")
      return false
    }
    return false
  }

  onUserDialog() {
    this.uni.setUserViewChange(true);
  }

  back() {
    this.uni.setThreadTitle('Travel Diary');
    this.uni.setThreadViewChange(false);
    //this.router.navigate(['thread/' + this.currentViewPage])
    //this.changePage(this.currentViewPage)

    if (this.currnetViewTags == 0) {
      this.router.navigate(['thread', this.currentViewPage])
    } else {
      this.router.navigate(['thread', 'tag', this.currnetViewTags,  this.currentViewPage])
    }
  }

  backFun() {
    if (this.searchView) {
      this.searchView = false;
      this.router.navigate(['thread/1'])
    }

    if (this.writeView) {
      this.uni.setWriteViewChange(false);
      this.router.navigate(['thread/1'])
    }
  }

  searchList() {
    if (!this.searchView) {
      this.searchView = true;
      this.keywords = '';
    } else {
      this.router.navigate(['search', this.keywords, 1])
    }
  }

  refreshList() {
    this.uni.setListViewRefreshChange(true)
  }

  refreshThread() {
    this.uni.setThreadViewRefreshChange(true);
  }

  writeThread() {
    if (this.loadSession()) {
      this.router.navigate(['threads/write'])
    } else {
      this.toastr.error('Please login and continue!');
    }
  }

  writeComment() {
    if (this.loadSession()) {
      this.router.navigate(['threads', this.currentPid, 'write'], { state: { page: this.currentPage } })
    } else {
      this.toastr.error('Please login and continue!');
    }
  }
}
