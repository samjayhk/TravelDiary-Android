import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, of, Observer } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';

const endpoint = 'http://localhost:3001/';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class RestService {

  constructor(private http: HttpClient) {}

  private loadSession() {
    if (localStorage.getItem('traveldiaryv1')) {
      const httpJSONTOKEN = {
        headers: new HttpHeaders({
          'Content-Type':  'application/x-www-form-urlencoded',
          'x-token': JSON.parse(localStorage.getItem('traveldiaryv1')).token
        })
      };
      return httpJSONTOKEN;
    }
    return httpOptions;
  }

  private extractData(res: Response) {
    let body = res;
    return body || { };
  }

  login(users): Observable<any> {
    return this.http.post(endpoint + 'users/login', JSON.stringify(users), httpOptions);
  }

  register(users): Observable<any> {
    return this.http.post(endpoint + 'users/register', JSON.stringify(users), httpOptions);
  }

  updatepassword(users): Observable<any> {
    return this.http.put(endpoint + 'users/updatepassword', JSON.stringify(users), httpOptions);
  }

  getTags(): Observable<any> {
      return this.http.get(endpoint + 'thread/tags');
  }

  getThreadList(page): Observable<any> {
    if (page != undefined) {
      return this.http.get(endpoint + 'thread/' + page);
    }
  }

  getThread(pid, page): Observable<any> {
    if (pid != undefined) {
      return this.http.get(endpoint + 'thread/' + pid + '/' + page);
    }
  }

  writeThread(thread): Observable<any> {
    if (thread != null) {
      const body = new HttpParams()
        .set('tid', thread.tid)
        .set('subject', thread.subject)
        .set('content', thread.content);

      return this.http.post(endpoint + 'thread/write', body, this.loadSession());
    }
  }

  editThread(pid, thread): Observable<any> {
    if (thread != null) {
      const body = new HttpParams()
        .set('tid', thread.tid)
        .set('subject', thread.subject)
        .set('content', thread.content);

      return this.http.put(endpoint + 'thread/' + pid + '/update', body, this.loadSession());
    }
  }

  deleteThread(pid): Observable<any> {
    if (pid != null) {
      return this.http.delete(endpoint + 'thread/' + pid + '/delete', this.loadSession());
    }
  }

  writeComment(pid, comment): Observable<any> {
    if (comment != null) {
      const body = new HttpParams()
        .set('comment', comment.comment);

      return this.http.post(endpoint + 'thread/' + pid + '/write', body, this.loadSession());
    }
  }

  editComment(cid, comment): Observable<any> {
    if (comment != null) {
      const body = new HttpParams()
        .set('comment', comment.comment);

      return this.http.put(endpoint + 'thread/comment/' + cid + '/update', body, this.loadSession());
    }
  }

  deleteComment(cid): Observable<any> {
    if (cid != null) {
      return this.http.delete(endpoint + 'thread/comment/' + cid + '/delete', this.loadSession());
    }
  }

  upload(image: File): Observable<any> {
    if (image != null) {
      const headers = new HttpHeaders({ 'enctype': 'multipart/form-data' });
      const formData = new FormData();
      formData.append('files', image);
      return this.http.post(endpoint + 'upload', formData, headers);
    }
  }

  search(keywords, page): Observable<any> {
    return this.http.get(endpoint + 'search/' + keywords + '/' + page);
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}