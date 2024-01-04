
import Payload from '../api/Payload';
import { SuccessMessage, RequestMessage, ErrorMessage } from "../api/MessageTypes";

export default class SocketHandler extends WebSocket {
    connected: boolean;
    constructor(url: string) {
        super(url);
        this.connected = false;

        this.addEventListener("open", (evt) => {
            console.log("Socket has been opened successfully!");
            this.connected = true;
        })

        this.addEventListener("message", (msg) => {
            if (this.readyState !== WebSocket.OPEN) {
                return;
            }

            try {
                const data = JSON.parse(msg.data);
                const type: SuccessMessage | RequestMessage | ErrorMessage = data.type;

                switch (type) {
                    default: {
                        console.log(data);
                        break;
                    }
                }
            } catch (err) {
                console.log("Invalid message structure.");
            }
        });

        this.addEventListener("error", (err) => {
            if (this.readyState !== WebSocket.OPEN) {
                return;
            }
            throw new Error(err.type);
        });
    }

    public sendPayload(payload: Payload) {
        this.send(payload.asPayloadString());
    }
}