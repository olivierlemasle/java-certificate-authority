System.register(["angular2/core", "./dn-builder"], function(exports_1, context_1) {
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
    var core_1, dn_builder_1;
    var DnFormComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dn_builder_1_1) {
                dn_builder_1 = dn_builder_1_1;
            }],
        execute: function() {
            DnFormComponent = (function () {
                function DnFormComponent() {
                    this.model = new dn_builder_1.DnBuilder("", "", "", "", "", "");
                    this.active = true;
                }
                DnFormComponent.prototype.clear = function () {
                    var _this = this;
                    this.model = new dn_builder_1.DnBuilder("", "", "", "", "", "");
                    this.active = false;
                    setTimeout(function () { return _this.active = true; }, 0);
                };
                DnFormComponent.prototype.onSubmit = function () {
                    this.clear();
                };
                DnFormComponent = __decorate([
                    core_1.Component({
                        selector: "dn-form",
                        templateUrl: "app/dn-form.component.html"
                    }), 
                    __metadata('design:paramtypes', [])
                ], DnFormComponent);
                return DnFormComponent;
            }());
            exports_1("DnFormComponent", DnFormComponent);
        }
    }
});

//# sourceMappingURL=dn-form.component.js.map
