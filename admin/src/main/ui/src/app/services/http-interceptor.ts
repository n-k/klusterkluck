import {Injectable} from '@angular/core';
import {
  Http,
  Headers,
  ConnectionBackend,
  RequestOptions,
  RequestOptionsArgs,
  Response,
  Request
} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';

var INSTANCE: HttpInterceptor;

@Injectable()
export class HttpInterceptor extends Http {

  private bearerToken: string;
  private statusCallbacks: {[key: number]: {(status: number, res: Response): void;}[]} = {};

  private constructor(backend: ConnectionBackend, defaultOptions: RequestOptions,) {
    super(backend, defaultOptions);
  }

  static newInstance(backend: ConnectionBackend, defaultOptions: RequestOptions,): HttpInterceptor {
    if (INSTANCE == null) {
      INSTANCE = new HttpInterceptor(backend, defaultOptions);
    }
    return INSTANCE;
  }

  static getInstance() {
    return INSTANCE;
  }

  public setBearerToken(token: string) {
    this.bearerToken = token;
  }

  public registerStatusCallback(status: number, cb: {(status: number, res: Response): void;}) {
    if (!this.statusCallbacks[status]) {
      this.statusCallbacks[status] = [];
    }
    this.statusCallbacks[status].push(cb);
  }

  request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
    if (options == null) {
      options = new RequestOptions();
    }
    if (options.headers == null) {
      options.headers = new Headers({});
    }
    if (!!this.bearerToken) {
      options.headers.set('Authorization', `Bearer ${this.bearerToken}`);
    }
    return super.request(url, options)
      .do(
        (res: Response) => {
          if (this.statusCallbacks[res.status]) {
            this.statusCallbacks[res.status].forEach(cb => cb(res.status, res));
          }
        },
        (res: Response) => {
          if (this.statusCallbacks[res.status]) {
            this.statusCallbacks[res.status].forEach(cb => cb(res.status, res));
          }
        })
      .finally(() => {});
  }

}
