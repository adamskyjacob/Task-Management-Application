export default class TaskSuperclass {
    constructor(
        public id: number,
        public users: number[],
        public description: string,
        public deadline: Date
    ) { }
}
