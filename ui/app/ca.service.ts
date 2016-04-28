import { Injectable } from "angular2/core";
import { Http, Response, Headers, RequestOptions } from "angular2/http";
import { Observable } from "rxjs/Observable";

import { Authority } from "./authority";
import { DnBuilder } from "./dn-builder";

@Injectable()
export class CaService {
  constructor (private http: Http) {}

  private _caUrl = "/api/certificateAuthorities";

  getCas(): Observable<Authority[]> {
    return this.http.get(this._caUrl)
                    .map(this.extractData)
                    .catch(this.handleError);
  }

  addCa (subject: DnBuilder): Observable<Authority>  {
    let headers = new Headers({ "Content-Type": "application/json" });
    let options = new RequestOptions({ headers: headers });
    let data = JSON.stringify(subject);

    return this.http.post(this._caUrl, data, options)
                    .map(this.extractData)
                    .catch(this.handleError);
  }

  private extractData(res: Response) {
    if (res.status < 200 || res.status >= 300) {
      throw new Error("Bad response status: " + res.status);
    }
    return res.json() || [];
  }

  private handleError (error: any) {
    let errMsg = error.message || "Server error";
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
