import { AnyMessage } from "../Common/MessageTypes";
import { PayloadStringable } from "./PayloadStringable";

export default class Payload extends PayloadStringable {
    constructor(
        public type: AnyMessage,
        public body: string | Record<any, any>
    ) { super() }
}