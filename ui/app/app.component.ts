import { Component } from "@angular/core";

import { AuthoritiesComponent } from "./authorities.component";
import { CertificatesComponent } from "./certificates.component";
import { CaService } from "./ca.service";

@Component({
  selector: "ca-app",
  template: `
    <div class="container">
      <div class="row">
        <div class="col-md-6">
          <h1>{{title}}</h1>
          <ul class="nav nav-tabs">
            <li class="nav-item">
              <a routerLink="authorities" class="nav-link" routerLinkActive="active">Authorities</a>
            </li>
            <li class="nav-item">
              <a routerLink="certificates" class="nav-link" routerLinkActive="active">Certificates</a>
            </li>
          </ul>
          <router-outlet></router-outlet>
        </div>
      </div>
    </div>
  `,
  providers: [
    CaService
  ]
})
export class AppComponent {
  title = "Certificate authority";
}
