import "./Login.css";

import { FormEvent, useContext } from "react";
import { apiURL } from "../../api/Common";
import LoginPayload from "../../api/LoginPayload";
import { TaskContext } from "../..";

export default function Login() {
    const { redirect } = useContext(TaskContext);

    function handleLogin(evt: FormEvent) {
        evt.preventDefault();
        const identifier = (document.querySelector(".identifier") as HTMLInputElement).value
        const isEmail = (document.querySelector(".identifier") as HTMLInputElement).type === "email";
        const password = (document.querySelector(".password") as HTMLInputElement).value;

        fetch(`${apiURL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: new LoginPayload(identifier, isEmail, password).asPayloadString()
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        }).then((json: { token: string }) => {
            sessionStorage.setItem("token", json.token);
            sessionStorage.setItem("identifier", isEmail ? identifier.split("@")[0] : identifier);
            redirect("/Home");
        }).catch(error => {
            console.error('Error during registration:', error);
            // Handle errors, e.g., duplicate user, server errors, etc.
        });
    }

    function checkValid() {
        const button = document.querySelector(".login_button") as HTMLButtonElement;
        button.disabled = !(document.querySelector("form")?.checkValidity());
    }

    return (
        <div className="login_body">
            <div className="login">
                <div className="register_redirect">
                    <img src="./swap.svg" />
                    <a href="/Register">Register instead?</a>
                </div>
                <form className="login_form" onSubmit={handleLogin}>
                    <div className="login_title">
                        <u>Member Login</u>
                    </div>
                    <div className="login_type_div">
                        <input className="identifier" required type="email" placeholder="Email" onBlur={checkValid} onChange={checkValid} onInput={checkValid} />
                        <div className="login_type" data-checked="false" onClick={function (evt) {
                            const input = (document.querySelector(".identifier") as HTMLInputElement);
                            input.placeholder = input.placeholder === "Email" ? "Username" : "Email";
                            input.type = input.type === "email" ? "text" : "email";
                            input.value = "";

                            const img = (evt.target as HTMLDivElement).querySelector("img") as HTMLImageElement;
                            img.src = img.src.includes("/email.svg") ? "./user.svg" : "./email.svg";
                        }}>
                            <img src="./email.svg"></img>
                        </div>
                    </div>
                    <input className="password" required type="password" onBlur={checkValid} onChange={checkValid} onInput={checkValid} minLength={12} placeholder="Password" />
                    <div className="password_requirements"></div>
                    <button className="login_button" type="submit" disabled={true}>Login</button>
                </form>
            </div>
        </div>
    )
}