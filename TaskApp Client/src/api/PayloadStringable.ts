export abstract class PayloadStringable {

    public asPayloadString() {
        return JSON.stringify(this);
    }
}