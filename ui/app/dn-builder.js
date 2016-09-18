System.register(["./countries"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var countries_1;
    var DnBuilder;
    return {
        setters:[
            function (countries_1_1) {
                countries_1 = countries_1_1;
            }],
        execute: function() {
            DnBuilder = (function () {
                function DnBuilder() {
                }
                Object.defineProperty(DnBuilder.prototype, "cn", {
                    get: function () {
                        return this._cn;
                    },
                    set: function (cn) {
                        this._cn = cn;
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(DnBuilder.prototype, "l", {
                    get: function () {
                        return this._l;
                    },
                    set: function (l) {
                        this._l = l;
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(DnBuilder.prototype, "st", {
                    get: function () {
                        return this._st;
                    },
                    set: function (st) {
                        this._st = st;
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(DnBuilder.prototype, "o", {
                    get: function () {
                        return this._o;
                    },
                    set: function (o) {
                        this._o = o;
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(DnBuilder.prototype, "ou", {
                    get: function () {
                        return this._ou;
                    },
                    set: function (ou) {
                        this._ou = ou;
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(DnBuilder.prototype, "c", {
                    get: function () {
                        return this._c;
                    },
                    set: function (c) {
                        this._c = c.toUpperCase();
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(DnBuilder.prototype, "countryName", {
                    get: function () {
                        if (this._c == null)
                            return null;
                        return countries_1.getCountryName(this._c) || (this._c.length < 2 ? "" : "Invalid country code");
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(DnBuilder.prototype, "isValidCountry", {
                    get: function () {
                        if (this._c == null)
                            return null;
                        return countries_1.getCountryName(this._c) !== null;
                    },
                    enumerable: true,
                    configurable: true
                });
                DnBuilder.prototype.toJSON = function () {
                    return { cn: this.cn, l: this.l, st: this.st, o: this.o, ou: this.ou, c: this.c };
                };
                return DnBuilder;
            }());
            exports_1("DnBuilder", DnBuilder);
        }
    }
});

//# sourceMappingURL=dn-builder.js.map
