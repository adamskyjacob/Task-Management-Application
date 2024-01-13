import Subtask from "./Subtask";
import TaskSuperclass from "./TaskSuperclass";

export default class Task extends TaskSuperclass {
    constructor(
        public owner: number,
        public subtasks: Subtask[],
        id: number,
        users: number[],
        description: string,
        deadline: Date
    ) { super(id, users, description, deadline) }
}
