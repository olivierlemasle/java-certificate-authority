System.register(["@angular/core", "rxjs/add/observable/throw", "./ca.service"], function(exports_1, context_1) {
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
    var core_1, ca_service_1;
    var AuthoritiesComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (_1) {},
            function (ca_service_1_1) {
                ca_service_1 = ca_service_1_1;
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
                AuthoritiesComponent.prototype.addCa = function (dnForm) {
                    var _this = this;
                    var subject = dnForm.model;
                    if (!subject) {
                        return;
                    }
                    dnForm.clear();
                    this._caService.addCa(subject)
                        .subscribe(function (ca) { return _this.cas.push(ca); }, function (error) { return _this.errorMessage = error; });
                };
                AuthoritiesComponent = __decorate([
                    core_1.Component({
                        selector: "authorities",
                        templateUrl: "app/authorities.component.html"
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
