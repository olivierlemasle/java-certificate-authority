import { getCountryName } from "./countries";

export class DnBuilder {
  private _c: string;

  constructor(
    cn: string,
    l: string,
    st: string,
    o: string,
    ou: string,
    c: string
  ) { 
    this._c = c;
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
}
