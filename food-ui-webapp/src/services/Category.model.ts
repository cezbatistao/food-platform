export default class Category {
  _uuid: string;
  _code: string;
  _description: string;

  constructor(uuid: string, code: string, description: string) {
    this._uuid = uuid;
    this._code = code;
    this._description = description;
  }

  get uuid(): string {
    return this._uuid;
  }

  get code(): string {
    return this._code;
  }

  get description(): string {
    return this._description;
  }
}