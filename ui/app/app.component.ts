import { Component } from "angular2/core";
import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from "angular2/router";
import { HTTP_PROVIDERS } from "angular2/http";

import { AuthoritiesComponent } from "./authorities.component";
import { CaService } from "./ca.service";

@Component({
  selector: "ca-app",
  template: `
    <h1>{{title}}</h1>
    <nav>
      <a [routerLink]="['Authorities']">Authorities</a>
    </nav>
    <router-outlet></router-outlet>
  `,
  directives: [ ROUTER_DIRECTIVES ],
  providers: [
    ROUTER_PROVIDERS,
    HTTP_PROVIDERS,
    CaService
  ]
})
@RouteConfig([
  {
    path: "/authorities",
    name: "Authorities",
    component: AuthoritiesComponent,
    useAsDefault: true
  }
])
export class AppComponent {
  title = "Certificate authority";
}
