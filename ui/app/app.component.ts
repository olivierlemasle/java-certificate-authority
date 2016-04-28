import { Component } from "angular2/core";
import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from "angular2/router";
import { HTTP_PROVIDERS } from "angular2/http";

import { AuthoritiesComponent } from "./authorities.component";
import { CertificatesComponent } from "./certificates.component";
import { CaService } from "./ca.service";

@Component({
  selector: "ca-app",
  template: `
    <div class="component">
      <div class="row">
        <div class="col-md-6 col-md-offset-3">
          <h1>{{title}}</h1>
          <ul class="nav nav-tabs">
            <li class="nav-item">
              <a [routerLink]="['Authorities']" class="nav-link">Authorities</a>
            </li>
            <li class="nav-item">
              <a [routerLink]="['Certificates']" class="nav-link">Certificates</a>
            </li>
          </ul>
          <router-outlet></router-outlet>
        </div>
      </div>
    </div>
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
  },
  {
    path: "/certificates",
    name: "Certificates",
    component: CertificatesComponent
  }
])
export class AppComponent {
  title = "Certificate authority";
}
