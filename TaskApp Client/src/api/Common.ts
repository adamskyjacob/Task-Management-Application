export const apiURL = "http://localhost:8080";
export const usernameRegex = /^[A-Za-z0-9.]*$/;
export const validateEmail = (email: string) => {
    const split = email.split("@");
    if (split.length !== 2) {
        return false;
    }

    if (!usernameRegex.test(split[0])) {
        return false;
    }

    const split2 = split[1].split(".");
    if (split2.length !== 2) {
        return false;
    }

    if (split2[1].length >= 2) {
        return true;
    }
}