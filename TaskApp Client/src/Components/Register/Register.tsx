import "./Register.css";

import { apiURL } from "../../api/Common";
import RegisterPayload from "../../api/RegisterPayload";
import { FormEvent } from "react";

export default function Register() {
    function handleRegister(evt: FormEvent) {
        evt.preventDefault();
        const identifier = (document.querySelector(".identifier") as HTMLInputElement).value
        const isEmail = (document.querySelector(".identifier") as HTMLInputElement).type === "email";
        const password = (document.querySelector(".password") as HTMLInputElement).value;

        fetch(`${apiURL}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: new RegisterPayload(identifier, isEmail, password).asPayloadString()
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
        }).then(data => {
            console.log('Registration successful:', data);
            // Handle the successful registration response
        }).catch(error => {
            console.error('Error during registration:', error);
            // Handle errors, e.g., duplicate user, server errors, etc.
        });
    }

    function checkValid() {
        const button = document.querySelector(".register_button") as HTMLButtonElement;
        button.disabled = !(document.querySelector("form")?.checkValidity());
    }

    return (
        <form className="register_form" onSubmit={handleRegister}>
            <button type="button" onClick={() => {
                fetch(`${apiURL}/test`, {
                    method: 'POST',
                    mode: 'no-cors'
                })
            }}>TEST</button>
            <div className="register_title">Member Registration</div>
            <div className="register_type_div">
                <input className="identifier" required type="email" placeholder="Email" onBlur={checkValid} onChange={checkValid} onInput={checkValid} />
                <div className="register_type" data-checked="false" onClick={function (evt) {
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
            <button className="register_button" type="submit" disabled={true}>Register</button>
        </form>
    )
}