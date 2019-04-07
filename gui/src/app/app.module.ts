import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { ToastrModule } from 'ngx-toastr';
import { QuillModule } from 'ngx-quill'
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';

import {NgbDropdownModule} from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UserComponent } from './users/user.component';
import { ThreadComponent } from './threads/thread.component';
import { ListComponent } from './list/list.component';
import { WriteComponent } from './write/write.component';
import { CommentComponent } from './comment/comment.component';
import { UpdateThreadComponent } from './update-thread/updatethread.component';
import { UpdateCommentComponent } from './update-comment/updatecomment.component';
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    ListComponent,
    ThreadComponent,
    WriteComponent,
    CommentComponent,
    UpdateThreadComponent,
    UpdateCommentComponent,
    SearchComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FontAwesomeModule,
    QuillModule,
    NgbDropdownModule,
    ToastrModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
