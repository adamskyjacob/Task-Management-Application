
import TaskSuperclass from "./TaskSuperclass";

export default class Subtask extends TaskSuperclass {
    constructor(
        public parent: number,
        id: number,
        users: number[],
        description: string,
        deadline: Date
    ) { super(id, users, description, deadline) }
}