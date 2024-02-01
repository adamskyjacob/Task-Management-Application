import { useContext, useEffect, useState } from "react";
import { apiURL } from "../../api/Common/Common";
import { TokenValidationRequest } from "../../api/Credentials/TokenValidationRequest";
import { TaskContext } from "../..";

export default function Home() {
    const [authed, setAuthed] = useState<boolean>(false);
    const [identifier, setIdentifier] = useState<string | null>(sessionStorage.getItem("identifier"));
    const [token, setToken] = useState<string | null>(sessionStorage.getItem("token"));

    const { redirect } = useContext(TaskContext);

    useEffect(() => {
        if (!token || !identifier) {
            setAuthed(false);
            return;
        }
        if (token && identifier) {
            fetch(`${apiURL}/validateToken`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: new TokenValidationRequest(token, identifier).asPayloadString()
            }).then((res) => {
                if (!res.ok) {
                    if (window.confirm("Invalid token, log back in?") && redirect) {
                        redirect("/Login");
                    }
                } else {
                    res.json().then((json: { valid: boolean }) => {
                        setAuthed(json.valid);
                    });
                }
            }).catch((err) => {
            })
        }
    }, [token, identifier]);


    function HomeAuthCheck() {
        if (authed) {
            return (
                <>
                    <div>Logged in!</div>
                    <button onClick={() => {
                        sessionStorage.removeItem("token");
                        setToken(null);
                        sessionStorage.removeItem("identifier");
                        setIdentifier(null);

                    }}>Log Out</button>
                </>
            )
        } else {
            return (
                <div>Not logged in!</div>
            )
        }
    }

    return <HomeAuthCheck />
}