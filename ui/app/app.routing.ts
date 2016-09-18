import { ModuleWithProviders } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { AuthoritiesComponent } from "./authorities.component";
import { CertificatesComponent } from "./certificates.component";

const appRoutes: Routes = [
    {
        path: "",
        redirectTo: "authorities",
        pathMatch: "full"
    },
    { path: "authorities", component: AuthoritiesComponent },
    { path: "certificates", component: CertificatesComponent }
];

export const appRoutingProviders: any[] = [

];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
