System.register(["angular2/core", "angular2/http", "rxjs/Observable"], function(exports_1, context_1) {
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
    var core_1, http_1, Observable_1;
    var CaService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            }],
        execute: function() {
            CaService = (function () {
                function CaService(http) {
                    this.http = http;
                    this._caUrl = "http://localhost:8080/certificateAuthorities";
                }
                CaService.prototype.getCas = function () {
                    return this.http.get(this._caUrl)
                        .map(this.extractData)
                        .catch(this.handleError);
                };
                CaService.prototype.addCa = function (subject) {
                    var headers = new http_1.Headers({ "Content-Type": "application/json" });
                    var options = new http_1.RequestOptions({ headers: headers });
                    var data = JSON.stringify(subject);
                    return this.http.post(this._caUrl, data, options)
                        .map(this.extractData)
                        .catch(this.handleError);
                };
                CaService.prototype.extractData = function (res) {
                    if (res.status < 200 || res.status >= 300) {
                        throw new Error("Bad response status: " + res.status);
                    }
                    return res.json() || [];
                };
                CaService.prototype.handleError = function (error) {
                    var errMsg = error.message || "Server error";
                    console.error(errMsg);
                    return Observable_1.Observable.throw(errMsg);
                };
                CaService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [http_1.Http])
                ], CaService);
                return CaService;
            }());
            exports_1("CaService", CaService);
        }
    }
});

//# sourceMappingURL=ca.service.js.map
