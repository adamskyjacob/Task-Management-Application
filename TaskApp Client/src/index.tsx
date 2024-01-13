import './index.css';

import { Context, Dispatch, SetStateAction, StrictMode, createContext, useEffect, useState } from 'react';
import { BrowserRouter, NavigateFunction, Route, Routes, useNavigate } from 'react-router-dom';
import { Client, IFrame } from '@stomp/stompjs';
import ReactDOM from 'react-dom/client';
import Home from './Components/Home/Home';
import Register from './Components/Register/Register';
import Login from './Components/Login/Login';
import TaskView from './Components/TaskView/TaskView';
import TaskCreation from './Components/TaskCreation/TaskCreation';

interface TaskContextInterface {
    socket: Client;
    setSocket: Dispatch<SetStateAction<Client>>;
    redirect: NavigateFunction;
}

export var TaskContext: Context<TaskContextInterface> = createContext<TaskContextInterface>({
    socket: new Client(),
    setSocket: () => { },
    redirect: () => { },
});

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

root.render(
    <StrictMode>
        <BrowserRouter>
            <StateProvider>
                <Routes>
                    <Route path="/Home" element={<Home />} />
                    <Route path="/Register" element={<Register />} />
                    <Route path="/Login" element={<Login />} />
                    <Route path="/TaskView" element={<TaskView />} />
                    <Route path="/TaskCreation" element={<TaskCreation />} />
                </Routes>
            </StateProvider>
        </BrowserRouter>
    </StrictMode>
);

function StateProvider(props: { children: any }) {
    const [stompClient, setStompClient] = useState<Client>(new Client());
    const redirect = useNavigate();

    useEffect(() => {
        const socket = new WebSocket('ws://localhost:8080/ws');
        const stomp = new Client({
            webSocketFactory: () => socket
        });

        stomp.onConnect = () => {
            console.log("WebSocket connected successfully!");
            setStompClient(stomp);

            stomp.onStompError = stomp.onWebSocketError = handleError;

            stomp.subscribe('/topic/taskCreated', (response) => {
                const message = response.body;
                console.log('Received message:', message);
            });
        };

        function handleError(err: IFrame | any) {
            console.log(err);
        }

        stomp.activate();

        return () => {
            if (stomp.connected) {
                stomp.deactivate();
            }
        };
    }, []);

    return (
        <TaskContext.Provider children={props.children} value={{ socket: stompClient, setSocket: setStompClient, redirect }} />
    )

}