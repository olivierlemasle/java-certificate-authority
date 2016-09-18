System.register(["@angular/router", "./authorities.component", "./certificates.component"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var router_1, authorities_component_1, certificates_component_1;
    var appRoutes, appRoutingProviders, routing;
    return {
        setters:[
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (authorities_component_1_1) {
                authorities_component_1 = authorities_component_1_1;
            },
            function (certificates_component_1_1) {
                certificates_component_1 = certificates_component_1_1;
            }],
        execute: function() {
            appRoutes = [
                {
                    path: "",
                    redirectTo: "authorities",
                    pathMatch: "full"
                },
                { path: "authorities", component: authorities_component_1.AuthoritiesComponent },
                { path: "certificates", component: certificates_component_1.CertificatesComponent }
            ];
            exports_1("appRoutingProviders", appRoutingProviders = []);
            exports_1("routing", routing = router_1.RouterModule.forRoot(appRoutes));
        }
    }
});

//# sourceMappingURL=app.routing.js.map
