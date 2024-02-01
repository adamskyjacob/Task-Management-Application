import { useContext, useEffect, useState } from "react";
import { TaskContext } from "../..";
import { useLocation } from "react-router-dom";
import Task from "../../api/Task/Task";

export default function TaskView() {
    const { redirect, socket } = useContext(TaskContext);
    const [tasks, setTasks] = useState<any[]>([]);

    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const id = searchParams.get("id");

    if (!id) {
        if (redirect) {
            redirect("/Tasks");
        }
        return <></>;
    }

    if (!socket || !socket.connected) {
        return <>Socket connection unsuccessful</>;
    }

    socket.subscribe("/topic/taskList", (message) => {
        setTasks(JSON.parse(message.body) as any[]);
    })

    return (
        <>
            <button onClick={() => {
                socket.publish({
                    destination: "/app/getTasks",
                    body: id ?? "-1"
                });
                console.log('clicked')
            }}>Get Tasks</button>
            {
                tasks.map(task => {
                    return <pre>{JSON.stringify(task, null, 2)}</pre>
                })
            }
        </>
    )
}