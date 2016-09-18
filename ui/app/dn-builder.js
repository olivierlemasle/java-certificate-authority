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
                function DnBuilder(cn, l, st, o, ou, c) {
                    this._c = c;
                }
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
                return DnBuilder;
            }());
            exports_1("DnBuilder", DnBuilder);
        }
    }
});

//# sourceMappingURL=dn-builder.js.map
