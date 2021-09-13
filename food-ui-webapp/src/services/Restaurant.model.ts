export default class Restaurant {
    _uuid: string;
    _name: string;
    _logo: string;
    _description: string;
    _address: string;
  
    constructor(uuid: string, name: string, logo: string, description: string, 
                address: string) {
      this._uuid = uuid;
      this._name = name;
      this._logo = logo;
      this._description = description;
      this._address = address;
    }
  
    get uuid(): string {
      return this._uuid;
    }
  
    get name(): string {
      return this._name;
    }
  
    get logo(): string {
      return this._logo;
    }
  
    get description(): string {
      return this._description;
    }
  
    get address(): string {
      return this._address;
    }
  }