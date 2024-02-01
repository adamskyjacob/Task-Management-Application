import { PayloadStringable } from "./PayloadStringable";

export class TokenValidationRequest extends PayloadStringable {
    constructor(
        public token: string,
        public identifier: string
    ) { super() }
}