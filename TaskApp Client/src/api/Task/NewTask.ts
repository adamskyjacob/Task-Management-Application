export default class NewTask {
    constructor(
        public owner: number,
        public description: string,
        public deadline: string,
        public users: number[]
    ) { }
}