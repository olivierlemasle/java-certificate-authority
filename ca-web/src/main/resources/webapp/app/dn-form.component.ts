import { Component } from "angular2/core";
import { NgForm } from "angular2/common";

import { DnBuilder } from "./dn-builder";

@Component({
  selector: "dn-form",
  templateUrl: "app/dn-form.component.html"
})
export class DnFormComponent {
  model = new DnBuilder("", "", "", "", "", "");
  active = true;

  clear(): void {
    this.model = new DnBuilder("", "", "", "", "", "");
    this.active = false;
    setTimeout(() => this.active = true, 0);
  }

  onSubmit(): void {
    this.clear();
  }
}
