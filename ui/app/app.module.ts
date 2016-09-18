import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { FormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";


import { AppComponent } from "./app.component";
import { AuthoritiesComponent } from "./authorities.component";
import { CertificatesComponent } from "./certificates.component";
import { DnFormComponent } from "./dn-form.component";
import { routing, appRoutingProviders } from "./app.routing";

@NgModule({
  imports: [BrowserModule, FormsModule, HttpModule, routing],
  declarations: [AppComponent, DnFormComponent, AuthoritiesComponent, CertificatesComponent],
  providers: [appRoutingProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
