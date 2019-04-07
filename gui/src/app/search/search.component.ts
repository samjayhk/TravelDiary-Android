import { Component, OnInit } from '@angular/core';
import { RestService } from '../service';
import { ActivatedRoute, Router } from '@angular/router';
import { UniService } from '../uni.services';
import { faPassport, faCommentAlt } from '@fortawesome/free-solid-svg-icons'
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})

export class SearchComponent implements OnInit {

  keywords = this.actRoute.snapshot.params['keywords'];
  page = this.actRoute.snapshot.params['page'];
  
  faComment = faCommentAlt;
  session:boolean;
  localSession:string;

  tags: any;
  threadList: any;

  constructor(private toastr: ToastrService, public rest:RestService, public uni: UniService, public actRoute: ActivatedRoute, public router: Router) {
  }

  ngOnInit() {
    this.loadSession();
    this.getThreadList();
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

  public getThreadList() {
    return this.rest.search(this.keywords, this.page).subscribe(
      threadList => {
        if (threadList.result) {
          this.threadList = threadList;
        } else {
          this.router.navigate(['thread/1'])
          this.toastr.error(threadList.message);
        }
      }
    );
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

  public ascii2native(str) {
      var character = str.split("\\u");
      var native = character[0];
      for (var i = 1; i < character.length; i++) {
          var code = character[i];
          native += String.fromCharCode(parseInt("0x" + code.substring(0, 4)));
          if (code.length > 4) {
              native += code.substring(4, code.length);
          }
      };
      return native;
  }
  
  arr(number) {
    var i:number; 
    var arrys = [];
    for(i = 0; i<number; i++) {
      arrys.push(i)
    }
    return arrys;
  }

  viewThread(pid, page) {
    this.router.navigate(['thread/' + pid + '/' + page])
  }
}
