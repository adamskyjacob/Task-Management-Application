import "./Register.css";

import RegisterPayload from "../../api/Credentials/RegisterPayload";
import { apiURL, passRegex } from "../../api/Common/Common";
import { FormEvent, useContext, useEffect } from "react";
import { TaskContext } from "../..";

export default function Register() {
    const { redirect } = useContext(TaskContext);

    function handleRegister(evt: FormEvent) {
        evt.preventDefault();
        const identifier = (document.querySelector(".identifier") as HTMLInputElement).value
        const isEmail = (document.querySelector(".identifier") as HTMLInputElement).type === "email";
        const password = (document.querySelector(".password") as HTMLInputElement).value;

        fetch(`${apiURL}/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: new RegisterPayload(identifier, isEmail, password).asPayloadString()
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        }).then((json) => {
            sessionStorage.setItem("token", json.token);
            sessionStorage.setItem("user_id", json.id);
            sessionStorage.setItem("identifier", isEmail ? identifier.split("@")[0] : identifier);
            if (redirect) {
                redirect("/Home");
            }
        }).catch(error => {
            console.error("Error during registration:", error);
        });
    }

    function checkValid() {
        const button = document.querySelector(".register_button") as HTMLButtonElement;
        button.disabled = !(document.querySelector("form")?.checkValidity());
    }

    useEffect(() => {
        (document.querySelector(".register_form") as HTMLFormElement).checkValidity = () => {
            const identifierInput = document.querySelector(".identifier") as HTMLInputElement;
            const passwordInput = document.querySelector(".password") as HTMLInputElement;

            if (identifierInput.value === "") {
                return false;
            }

            return passRegex.test(passwordInput.value);
        }
    }, []);

    return (
        <div className="register_body">
            <div className="register">
                <div className="login_redirect">
                    <img src="./swap.svg" />
                    <a href="/Login">Login instead?</a>
                </div>
                <form className="register_form" onSubmit={handleRegister}>
                    <div className="register_title">
                        <u>Member Registration</u>
                    </div>
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
                    <input className="password" required type="password" onBlur={checkValid} onInput={checkValid} minLength={12} placeholder="Password" />
                    <button className="register_button" type="submit" disabled={true}>Register</button>
                </form>
            </div>
        </div>
    )
}