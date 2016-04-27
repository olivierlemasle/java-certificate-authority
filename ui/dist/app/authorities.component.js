System.register(["angular2/core", "./ca.service", "./dn-form.component"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, ca_service_1, dn_form_component_1;
    var AuthoritiesComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (ca_service_1_1) {
                ca_service_1 = ca_service_1_1;
            },
            function (dn_form_component_1_1) {
                dn_form_component_1 = dn_form_component_1_1;
            }],
        execute: function() {
            AuthoritiesComponent = (function () {
                function AuthoritiesComponent(_caService) {
                    this._caService = _caService;
                }
                AuthoritiesComponent.prototype.ngOnInit = function () { this.getCas(); };
                AuthoritiesComponent.prototype.getCas = function () {
                    var _this = this;
                    this._caService.getCas()
                        .subscribe(function (cas) { return _this.cas = cas; }, function (error) { return _this.errorMessage = error; });
                };
                AuthoritiesComponent.prototype.addCa = function (subject) {
                    var _this = this;
                    if (!subject) {
                        return;
                    }
                    this._caService.addCa(subject)
                        .subscribe(function (hero) { return _this.cas.push(hero); }, function (error) { return _this.errorMessage = error; });
                };
                AuthoritiesComponent = __decorate([
                    core_1.Component({
                        selector: "authorities",
                        templateUrl: "app/authorities.component.html",
                        directives: [dn_form_component_1.DnFormComponent]
                    }), 
                    __metadata('design:paramtypes', [ca_service_1.CaService])
                ], AuthoritiesComponent);
                return AuthoritiesComponent;
            }());
            exports_1("AuthoritiesComponent", AuthoritiesComponent);
        }
    }
});

//# sourceMappingURL=authorities.component.js.map
