export default class Error {
  
  _code: string;
  _message: string;
  _error: any;

  constructor(code: string, message: string, error?: any  | null) {
    this._code = code;
    this._message = message;
    this._error = error;
  }

  get code(): string {
    return this._code;
  }

  get message(): string {
    return this._message;
  }

  get error(): string {
    return this._error;
  }
}