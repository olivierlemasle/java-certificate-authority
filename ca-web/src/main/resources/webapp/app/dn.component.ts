import {Component, Input} from "angular2/core";

@Component({
  selector: "dn-editor",
  templateUrl: "app/dn.component.html"
})
export class DnComponent {
  cn: string;
  ou: string;
  o: string;

  getDn(): string {
    return "cn=" + this.cn + ",ou=" + this.ou + ",o=" + this.o;
  }
}
