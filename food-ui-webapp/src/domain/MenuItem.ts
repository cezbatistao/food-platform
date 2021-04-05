export default class MenuItem {
  _uuid: string;
  _name: string;
  _description: string;
  _value: string;

  constructor(uuid: string, name: string, description: string, 
              value: string) {
    this._uuid = uuid;
    this._name = name;
    this._description = description;
    this._value = value;
  }

  get uuid(): string {
    return this._uuid;
  }

  get name(): string {
    return this._name;
  }

  get description(): string {
    return this._description;
  }

  get value(): string {
    return this._value;
  }
}