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
import { DomSanitizer } from '@angular/platform-browser'
import { ActivatedRoute, Router } from '@angular/router';
import { UniService } from '../uni.services';
import { ToastrService } from 'ngx-toastr';
import { faPassport, faChevronDown } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})

export class CommentComponent implements OnInit {

  pid = this.actRoute.snapshot.paramMap.get('pid');

  session:boolean;
  localSession:string;

  editorContent: any;
  faChevronDown = faChevronDown;

  currentPage;
  quillEditorRef;
  maxUploadFileSize = 2000000;

  constructor(private sanitized: DomSanitizer, private toastr: ToastrService, public rest:RestService, public uni: UniService, public actRoute: ActivatedRoute, public router: Router) { 
    try {
      this.currentPage = this.router.getCurrentNavigation().extras.state.page
    } catch (er) {
      this.currentPage = 1
    }
  }

  ngOnInit() {
    this.loadSession();
  }
  
  getEditorInstance(editorInstance: any) {
    this.quillEditorRef = editorInstance;
    const toolbar = editorInstance.getModule('toolbar');
    toolbar.addHandler('image', this.imageHandler);
  }

  imageHandler = (image, callback) => {
    const input = <HTMLInputElement> document.getElementById('fileInputField');
    document.getElementById('fileInputField').onchange = () => {
      let file: File;
      file = input.files[0];
      // file type is only image.
      if (/^image\//.test(file.type)) {
        if (file.size > this.maxUploadFileSize) {
          alert('Image needs to be less than 1MB');
        } else {
          return this.rest.upload(file).subscribe(
            result => {
              if (result.result) {
                this.toastr.success(result.message);
                const range = this.quillEditorRef.getSelection();
                const img = '<img src="http://localhost:3001/uploads/' + result.filename + '" />';
                this.quillEditorRef.clipboard.dangerouslyPasteHTML(range.index, img);
              } else {
                this.toastr.error((result.message));
              }
            }
          );
          // const reader  = new FileReader();
          // reader.onload = () =>  {
          //   const range = this.quillEditorRef.getSelection();
          //   const img = '<img src="' + reader.result + '" />';
          //   this.quillEditorRef.clipboard.dangerouslyPasteHTML(range.index, img);
          // };
          // reader.readAsDataURL(file);
          // console.log()
        }
      } else {
          alert('You could only upload images.');
      }
    };

    input.click();
  }

  loadSession() {
    if (localStorage.getItem('traveldiaryv1')) {
      this.session = true;
      this.localSession = JSON.parse(localStorage.getItem('traveldiaryv1'));
    } else {
      localStorage.removeItem('traveldiaryv1');
      this.session = false;
      console.log("Please login and continue.")
      this.router.navigate(['thread/1'])
      this.toastr.error('Please login and continue!');
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

  public native2ascii(str) {
    var character = str;
    var ascii = "";
    for (var i = 0; i < character.length; i++) {
      var code = Number(character[i].charCodeAt(0));
      if (code > 127) {
        var charAscii = code.toString(16);
        charAscii = new String("0000").substring(charAscii.length, 4) + charAscii;
        ascii += "\\u" + charAscii
      } else {
        ascii += character[i];
      }
    }
    return ascii;
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

  submit() {
    if (this.editorContent != '' && this.editorContent != undefined && this.editorContent != null) {
          const comments = {comment: this.native2ascii(this.editorContent)}
          return this.rest.writeComment(this.pid, comments).subscribe(
            comment => {
              if (comment.result) {
                this.router.navigate(['thread/' + this.pid + '/' + this.currentPage])
                this.toastr.success(comment.message);
              } else {
                this.toastr.error(comment.message);
              }
            }
          );
    } else {
      this.toastr.error('Please fill in box!');
    }
  }
}
