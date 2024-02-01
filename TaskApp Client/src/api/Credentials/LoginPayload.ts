import { PayloadStringable } from "./PayloadStringable";

export default class LoginPayload extends PayloadStringable {
    constructor(
        public identifier: string,
        public isEmail: boolean,
        public password: string
    ) { super() }
}