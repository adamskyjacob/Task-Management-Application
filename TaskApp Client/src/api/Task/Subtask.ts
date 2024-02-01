
import TaskSuperclass from "./TaskSuperclass";

export default class Subtask extends TaskSuperclass {
    constructor(
        public parent: number,
        users: number[],
        description: string,
        deadline: Date,
        id?: number
    ) { super(users, description, deadline.toISOString().split("T")[0], id) }
}