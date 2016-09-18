import { getCountryName } from "./countries";

export class DnBuilder {
  private _cn: string;
  private _l: string;
  private _st: string;
  private _o: string;
  private _ou: string;
  private _c: string;

  get cn(): string {
    return this._cn;
  }

  set cn(cn: string) {
    this._cn = cn;
  }

  get l(): string {
    return this._l;
  }

  set l(l: string) {
    this._l = l;
  }

  get st(): string {
    return this._st;
  }

  set st(st: string) {
    this._st = st;
  }

  get o(): string {
    return this._o;
  }

  set o(o: string) {
    this._o = o;
  }

  get ou(): string {
    return this._ou;
  }

  set ou(ou: string) {
    this._ou = ou;
  }

  get c(): string {
    return this._c;
  }

  set c(c: string) {
    this._c = c.toUpperCase();
  }

  get countryName(): string {
    if (this._c == null) return null;
    return getCountryName(this._c) || (this._c.length < 2 ? "" : "Invalid country code");
  }

  get isValidCountry(): boolean {
    if (this._c == null) return null;
    return getCountryName(this._c) !== null;
  }

  toJSON(): any {
    return {cn: this.cn, l: this.l, st: this.st, o: this.o, ou: this.ou, c: this.c};
  }
}
