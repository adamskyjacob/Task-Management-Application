import User from "../Credentials/User";
import Subtask from "./Subtask";
import Task from "./Task";

export class TaskUser {
    constructor(
        public id: number,
        public task: Task,
        public subtask: Subtask,
        public user: User
    ) { }
}