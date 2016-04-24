import { Component, OnInit } from "angular2/core";

import {Authority} from "./authority";
import {CaService} from "./ca.service";
import { DnComponent } from "./dn.component";


@Component({
  selector: "authorities",
  templateUrl: "app/authorities.component.html",
  directives: [DnComponent]
})
export class AuthoritiesComponent implements OnInit {

  constructor(private _caService: CaService) {}

  errorMessage: string;
  cas: Authority[];

  ngOnInit() { this.getCas(); }

  getCas() {
    this._caService.getCas()
                   .subscribe(
                     cas => this.cas = cas,
                     error =>  this.errorMessage = <any>error);
  }

  addCa (subject: string) {
    if (!subject) { return; }
    this._caService.addCa(subject)
                   .subscribe(
                     hero  => this.cas.push(hero),
                     error =>  this.errorMessage = <any>error);
  }
}
