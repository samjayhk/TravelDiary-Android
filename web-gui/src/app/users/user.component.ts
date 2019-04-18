/**
 * THIS PROJECT IS A UNIVERSITY PROJECT AND MADE BY SCHOOL FOR HIGHER AND PROFESSIONAL EDUCATION WITH COVENTRY UNIVERSITY (UK).
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * AUTHOR INFORMATION:
 * NAME: TSANG Long Fung (187107130)
 * CONTACT NUMBER: (+852) 6679 2339
 * CONTACT EMAIL: 187107130@stu.vtc.edu.hk
 */

import { Component, OnInit } from '@angular/core';
import { RestService } from '../service';
import { ActivatedRoute, Router } from '@angular/router';
import { UniService } from '../uni.services';
import { ToastrService } from 'ngx-toastr';
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
  last: Date;

  session:boolean;
  localSession:string;

  constructor(private toastr: ToastrService, public rest:RestService, public uni: UniService) { }

  ngOnInit() {
    this.loadSession();
  }

  loadSession() {
    if (localStorage.getItem('traveldiaryv1')) {
      this.session = true;
      this.localSession = JSON.parse(localStorage.getItem('traveldiaryv1'));
      this.last = this.timeConverter(this.localSession.last)
    } else {
      localStorage.removeItem('traveldiaryv1');
      this.session = false;
    }
  }

  timeConverter(UNIX_timestamp){
    
    return new Date(UNIX_timestamp);
  }

  btnRegister() {
    if (!this.session) {
      if (this.register===0) {
        this.register = 1;
      } else {
        this.register = 0;
      }
    } else {
      if (this.oldPassword!==this.newPassword) {
        return this.rest.updatepassword({oldPassword: this.oldPassword, newPassword: this.newPassword}).subscribe(
          result => {
            if (result.result) {
              this.toastr.success(result.message);
              localStorage.removeItem('traveldiaryv1');
              this.session = false;
            } else {
              this.toastr.error(result.message);
              return false;
            }

            this.username = '';
            this.oldPassword = '';
            this.newPassword = '';
            this.email = '';
          }
        );
      } else {
        this.toastr.error('Password not change!');
      }

      this.username = '';
      this.oldPassword = '';
      this.newPassword = '';
      this.email = '';
    }
    
  }

  // btnForgot() {
  //   this.register = 2;
  // }

  btnSubmit() {
    if (!this.session) {
      if (this.register == 0) {
        return this.rest.login({username: this.username, password: this.oldPassword}).subscribe(
          result => {
            if (result.result) {
              localStorage.setItem('traveldiaryv1', JSON.stringify(result));
              this.loadSession();
              console.log(result.message);
              this.toastr.success(result.message);
            } else {
              console.log(result.message);
              this.toastr.error(result.message);
              return false;
            }

            this.username = '';
            this.oldPassword = '';
            this.newPassword = '';
            this.email = '';
          }
        );
      } else if (this.register == 1) {
        if (this.oldPassword ===this.newPassword) {
          return this.rest.register({username: this.username, password: this.newPassword, email: this.email}).subscribe(
            result => {
              if (result.result) {
                this.toastr.success(result.message);
              } else {
                this.toastr.error(result.message);
                return false;
              }
  
              this.username = '';
              this.oldPassword = '';
              this.newPassword = '';
              this.email = '';
            }
          );
        } else {
          this.toastr.error('Password not match!');
        }

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
