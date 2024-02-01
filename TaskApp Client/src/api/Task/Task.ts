import Subtask from "./Subtask";
import TaskSuperclass from "./TaskSuperclass";

export default class Task extends TaskSuperclass {
    constructor(
        public owner: number,
        public subtasks: Subtask[],
        users: number[],
        description: string,
        deadline: Date,
        id?: number
    ) { super(users, description, deadline.toISOString().split("T")[0], id) }
}
