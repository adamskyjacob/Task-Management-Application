import { useEffect, useState } from "react";
import Task from "../../api/Task/Task";
import { apiURL } from "../../api/Common";

export default function TaskView() {
    const [tasks, setTasks] = useState<any[]>([]);

    async function fetchTasks() {

        fetch(`${apiURL}/get_tasks?username=guy`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        }).then((json) => {
            console.log(json)
        }).catch(error => {
            console.error('Error during registration:', error);
            // Handle errors, e.g., duplicate user, server errors, etc.
        });
    }

    useEffect(() => {
        fetchTasks();
    }, [])

    return (
        <>
            {
                tasks.map(task => {
                    return <div>{JSON.stringify(task)}</div>
                })
            }
        </>
    )
}