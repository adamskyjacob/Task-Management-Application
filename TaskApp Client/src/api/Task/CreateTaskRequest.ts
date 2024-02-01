import NewTask from "./NewTask";

export class CreateTaskRequest {
    constructor(
        public newTask?: NewTask,
        public token?: string,
        public id?: number
    ) { }

    public filled(): boolean {
        return !(!this.newTask || !this.token || !this.id);
    }
}