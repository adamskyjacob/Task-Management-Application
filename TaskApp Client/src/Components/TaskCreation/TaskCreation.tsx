import { useState } from "react";

export default function TaskCreation() {
    const [currentUser, setCurrentUser] = useState<string | null>(sessionStorage.getItem("identifier"));
    const [token, setToken] = useState<string | null>(sessionStorage.getItem("token"));
    const [description, setDescription] = useState<string>("");

    function getTask() {
        let task = new Task()
    }


    if (!token || !currentUser) {
        return (
            <div>User or token is missing.</div>
        )
    }

    return (
        <div>
            <input type="text" onInput={(evt) => { setDescription((evt.target as HTMLInputElement).value) }} placeholder="Description" />
        </div>
    )
}