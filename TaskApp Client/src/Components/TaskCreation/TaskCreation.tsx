import "./TaskCreation.css"

import { FormEvent, useContext, useState } from "react";
import Task from "../../api/Task/Task";
import { TaskContext } from "../..";
import { CreateTaskRequest } from "../../api/Task/CreateTaskRequest";

export default function TaskCreation(){
    const { socket } = useContext(TaskContext);

    const [currentUser, setCurrentUser] = useState<string | null>(sessionStorage.getItem("identifier"));
    const [token, setToken] = useState<string | null>(sessionStorage.getItem("token"));
    const [description, setDescription] = useState<string>("");
    const [users, setUsers] = useState<JSX.Element[]>([]);
    /*    function createTask(evt: FormEvent<HTMLFormElement>) {
            evt.preventDefault();
            const data: FormData = new FormData(evt.currentTarget);
    
    
            const json: CreateTaskRequest = {
                newTask: { description: "", deadline: ""    , owner: -1, users: [] },
                token: "",
                id: 0
            };
    
    
            let token, id;
            if ((token = sessionStorage.getItem("token")) && (id = sessionStorage.getItem("user_id"))) {
                json["token"] = token;
                json["id"] = Number(id);
            } else {
                throw new Error("No user token or ID.");
            }
    
            let description, date;
            if ((description = data.get("description")) && (date = data.get("date"))) {
                json.newTask = {
                    description: description.toString(), deadline: date.toString(), owner: Number(id), users: []
                }
            }
    
            if (socket) {
                socket.publish({
                    destination: "/app/createTask",
                    body: JSON.stringify(json)
                });
                console.log(JSON.stringify(json, null, 2))
            } else {
                throw new Error("Socket not initialized.");
            }
        }*/

    function getCreateTaskRequest(evt: FormEvent<HTMLFormElement>) {
        const formData: FormData = new FormData(evt.currentTarget);

    }


    if (!token || !currentUser) {
        return (
            <div>User or token is missing.</div>
        )
    }



    function addAnotherUser() {
        setUsers(prev => [...prev, createUser()]);

        function createUser() {
            return (
                <div id={`user-${users.length}`} style={{ display: "inline-flex" }}>
                    <div>This will be a search</div>
                    <button onClick={(evt) => { (evt.target as HTMLElement).parentElement?.remove(); }}>Remove</button>
                    <input hidden type="text" />
                </div>
            )
        }

    }

    return (
        <form className="task_form" onSubmit={console.log}>
            <div className="name_input">
                <input type="text" id="name" name="name" />
            </div>
            <br />
            <div className="owner_label">Owner: {currentUser}</div>

            <div className="description_input" style={{ display: "grid", marginTop: "1vw", textAlign: "center" }}>
                <label htmlFor="description">Description:</label>
                <textarea id="description" name="description" style={{ width: "35vw", height: "20vh" }} />
            </div>

            <div className="deadline_input" style={{ marginTop: "1vw" }}>
                <label htmlFor="date">Deadline:</label>
                <input type="date" id="deadline" name="deadline" />
            </div>
            <div className="users" style={{ display: "grid", marginTop: "1vw" }}>
                <button onClick={addAnotherUser} type="button" style={{ width: "max-content", margin: "0 auto" }}>Add User</button>
                {users}
            </div>

            <button>Submit</button>
        </form>
    )
}