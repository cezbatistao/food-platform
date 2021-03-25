export default class Category {
  _id: string;
  _description: string;

  constructor(id: string, description: string) {
    this._id = id;
    this._description = description;
  }

  get id(): string {
    return this._id;
  }

  get description(): string {
    return this._description;
  }
}