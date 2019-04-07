import { Component, OnInit } from '@angular/core';
import { RestService } from '../service';
import { ActivatedRoute, Router } from '@angular/router';
import { UniService } from '../uni.services';
import { faPassport } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})

export class UserComponent implements OnInit {

  title = 'TravelDiary';
  faPassport = faPassport;
  register = 0;

  username:string;
  oldPassword:string;
  newPassword:string;
  email:string;

  session:boolean;
  localSession:string;

  constructor(public rest:RestService, public uni: UniService) { }

  ngOnInit() {
    this.loadSession();
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

  btnRegister() {
    if (this.register===0) {
      this.register = 1;
    } else {
      this.register = 0;
    }
  }

  btnForgot() {
    this.register = 2;
  }

  btnSubmit() {
    if (!this.session) {
      if (this.register == 0) {
        return this.rest.login({username: this.username, password: this.oldPassword}).subscribe(
          result => {
            if (result.result) {
              localStorage.setItem('traveldiaryv1', JSON.stringify(result));
              this.loadSession();
              console.log(result.message);
            } else {
              console.log(result.message);
              return false;
            }

            this.username = '';
            this.oldPassword = '';
            this.newPassword = '';
            this.email = '';
          }
        );
      } else if (this.register == 1) {
        this.username = '';
        this.oldPassword = '';
        this.newPassword = '';
        this.email = '';
      } else {
        this.username = '';
        this.oldPassword = '';
        this.newPassword = '';
        this.email = '';
      }
    } else {
      localStorage.removeItem('traveldiaryv1');
      this.session = false;
    }
  }

  closeUser() {
    this.uni.setUserViewChange(false);
  }
}
