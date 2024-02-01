import { PayloadStringable } from "./PayloadStringable";

export default class RegisterPayload extends PayloadStringable {
    constructor(
        public identifier: string,
        public isEmail: boolean,
        public password: string
    ) { super() }
}