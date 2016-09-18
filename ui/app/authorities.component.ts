import { Component, OnInit } from "@angular/core";

import "rxjs/add/observable/throw";

import { Authority } from "./authority";
import { CaService } from "./ca.service";
import { DnBuilder } from "./dn-builder";
import { DnFormComponent } from "./dn-form.component";

@Component({
  selector: "authorities",
  templateUrl: "app/authorities.component.html"
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

  addCa (dnForm: DnFormComponent) {
    let subject = dnForm.model;
    if (!subject) { return; }
    dnForm.clear();
    this._caService.addCa(subject)
                   .subscribe(
                     ca  => this.cas.push(ca),
                     error =>  this.errorMessage = error);
  }
}
