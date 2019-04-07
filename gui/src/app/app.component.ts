import { Component, OnInit } from '@angular/core';
import { RestService } from './service';
import { ActivatedRoute, Router } from '@angular/router';
import { UniService } from './uni.services';
import { ToastrService } from 'ngx-toastr';

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

  keywords;

  _subscriptionPid;
  _subscriptionPage;
  _subscriptionTitle;
  _subscriptionThread;
  _subscriptionUser;
  _subscriptionWrite;

  constructor(private toastr: ToastrService, public rest:RestService, public uni: UniService, public actRoute: ActivatedRoute, public router: Router) { 
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
      this._subscriptionTitle = uni.pageChange.subscribe((value) => {
        this.currentPage = value;
      });
    }
    if (uni.writeViewChange.observers.length === 0) {
      this._subscriptionWrite = uni.writeViewChange.subscribe((value) => {
        this.writeView = value;
      });
    }
  }

  ngOnDestroy() {
    this._subscriptionTitle.unsubscribe();
    this._subscriptionThread.unsubscribe();
    this._subscriptionUser.unsubscribe();
  }

  ngOnInit() {
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
    this.router.navigate(['thread/1'])
  }

  backFun() {
    if (this.searchView) {
      this.searchView = false;
    }

    if (this.writeView) {
      this.uni.setWriteViewChange(false);
      this.router.navigate(['thread/1'])
    }
  }

  searchList() {
    if (!this.searchView) {
      this.searchView = true;
    } else {
      this.router.navigate(['search', this.keywords, 1])
    }
  }

  refreshList() {
    this.router.navigate(['thread', 1])
    this.ngOnInit()
  }

  refreshThread() {
    this.router.navigate(['thread', this.currentPid, this.currentPage])
    this.ngOnInit()
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
