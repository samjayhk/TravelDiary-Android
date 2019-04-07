import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { ThreadComponent } from './threads/thread.component';
import { ListComponent } from './list/list.component';
import { WriteComponent } from './write/write.component';
import { CommentComponent } from './comment/comment.component';
import { UpdateThreadComponent } from './update-thread/updatethread.component';
import { UpdateCommentComponent } from './update-comment/updatecomment.component';
import { SearchComponent } from './search/search.component';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'thread/1' },
  {path: 'thread/:page', component: ListComponent},
  {path: 'thread/tag/:tid/:page', component: ListComponent},
  {path: 'thread/:pid/:page', component: ThreadComponent},
  {path: 'threads/write', component: WriteComponent},
  {path: 'threads/:pid/write', component: CommentComponent},
  {path: 'threads/:pid/update', component: UpdateThreadComponent},
  {path: 'threads/comment/:cid/update', component: UpdateCommentComponent},
  {path: 'search/:keywords/:page', component: SearchComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    onSameUrlNavigation: 'reload',
    enableTracing: false
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
