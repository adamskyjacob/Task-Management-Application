export default class TaskSuperclass {
    constructor(
        public users: number[],
        public description: string,
        public deadline: string,
        public id?: number
    ) { }
}
