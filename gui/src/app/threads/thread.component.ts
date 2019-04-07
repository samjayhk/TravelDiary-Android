import { DomSanitizer } from '@angular/platform-browser'
import { Component, OnInit, PipeTransform, Pipe } from '@angular/core';
import { RestService } from '../service';
import { ActivatedRoute, Router } from '@angular/router';
import { UniService } from '../uni.services';
import { ToastrService } from 'ngx-toastr';
import { faEdit, faTrashAlt, faPassport, faChevronDown } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'thread',
  templateUrl: './thread.component.html',
  styleUrls: ['./thread.component.css']
})

@Pipe({ name: 'safeHtml'})
export class ThreadComponent implements OnInit, PipeTransform {

  param;
  pid = this.actRoute.snapshot.paramMap.get('pid');
  page = this.actRoute.snapshot.paramMap.get('page');
  

  thread:any;
  session:boolean;
  localSession;
  faChevronDown = faChevronDown;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;

  constructor(private toastr: ToastrService, private sanitized: DomSanitizer, public rest:RestService, public uni: UniService, public actRoute: ActivatedRoute, public router: Router) {
    
  }

  ngOnDestroy() {
    this.uni.setCurrentPid('1');
    this.uni.setCurrentPage('1');
  }

  ngOnInit() {
    this.loadSession();
    this.viewThread();
  }

  loadSession() {
    if (localStorage.getItem('traveldiaryv1')) {
      this.session = true;
      this.localSession = JSON.parse(localStorage.getItem('traveldiaryv1'));
    } else {
      localStorage.removeItem('traveldiaryv1');
      this.session = false;
    }
  }

  public dateDiff(time) {
    // time difference
    const cal = Math.abs(Date.now() - time);
    const minsDiff = Math.ceil(cal / (1000 * 60))
    const hoursDiff = Math.ceil(cal / (1000 * 3600))
    // days difference
    const diffDays = Math.ceil(cal / (1000 * 3600 * 24));

    if (minsDiff-1 < 60) {
      return minsDiff-1 + "m";
    } else if (hoursDiff-1 < 24) {
      return hoursDiff-1 + "h";
    } else if (diffDays-1 < 30) {
      return diffDays-1 + "d";
    } else {
      return (diffDays-1/30) + "m";
    }
  }

  timeConverter(UNIX_timestamp){
    var a = new Date(UNIX_timestamp * 1000);
    var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    var year = a.getFullYear();
    var month = months[a.getMonth()];
    var date = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec ;
    return time;
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
    this.router.navigate(['thread/' + this.pid + '/' + p])
  }

  public ascii2native(str, ac) {
      var character = str.split("\\u");
      var native = character[0];
      for (var i = 1; i < character.length; i++) {
          var code = character[i];
          native += String.fromCharCode(parseInt("0x" + code.substring(0, 4)));
          if (code.length > 4) {
              native += code.substring(4, code.length);
          }
      };
      if (ac) {
        return this.sanitized.bypassSecurityTrustHtml(native);
      } else {
        return native;
      }
  }

  viewThread() {
    return this.rest.getThread(this.pid, this.page).subscribe(
      thread => {
        if (thread.result) {
          this.thread = thread;
          this.uni.setCurrentPid(this.pid);
          this.uni.setCurrentPage(this.page);
          this.uni.setThreadTitle(this.ascii2native(thread.subject, false));
          this.uni.setThreadViewChange(true);
        } else {
          this.uni.setThreadTitle('Travel Diary');
          this.uni.setThreadViewChange(false);
          this.router.navigate(['thread/1'])
        }
      }
    );
  }

  editThread() {
    try {
      if (this.thread.thread[0].username===this.localSession.username) {
        this.router.navigate(['threads', this.thread.thread[0].pid, 'update'], { state: { thread: this.thread.thread[0] } })
      } else {
        this.toastr.error('Only thread owner can edit!');
      }
    } catch (er) {
      this.toastr.error('Please login and continue!');
    }
  }

  deleteThread() {
    try {
      if (this.thread.thread[0].username===this.localSession.username) {
        return this.rest.deleteThread(this.pid).subscribe(
          thread => {
            if (thread.result) {
              this.router.navigate(['thread/1'])
              this.ngOnInit()
              this.toastr.success(thread.message);
            } else {
              this.toastr.error(thread.message);
            }
          }
        );
      } else {
        this.toastr.error('Only thread owner can delete!');
      }
    } catch (er) {
      this.toastr.error('Please login and delete!');
    }
  }

  editComment(i, cid) {
    try {
      if (this.thread.comment[i].username===this.localSession.username) {
        this.router.navigate(['threads/comment', cid, 'update'], { state: { pid: this.pid, page: this.page, comment: this.thread.comment[i].comment } })
      } else {
        this.toastr.error('Only comment owner can edit!');
      }
    } catch (er) {
      this.toastr.error('Please login and continue!');
    }
  }

  deleteComment(i, cid) {
    try {
      if (this.thread.comment[i].username===this.localSession.username) {
        return this.rest.deleteComment(cid).subscribe(
          comment => {
            if (comment.result) {
              this.router.navigate(['thread', this.pid, this.page])
              this.ngOnInit()
              this.toastr.success(comment.message);
            } else {
              this.toastr.error(comment.message);
            }
          }
        );
      } else {
        this.toastr.error('Only comment owner can delete!');
      }
    } catch (er) {
      this.toastr.error('Please login and delete!');
    }
  }


}
