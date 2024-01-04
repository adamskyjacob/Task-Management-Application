export type SuccessMessage = "LOGIN_SUCCESS" | "REGISTER_SUCCESS"
export type RequestMessage = "CREATE_TASK" | "LOGIN_REQUEST" | "REGISTER_REQUEST"
export type ErrorMessage = "LOGIN_ERROR" | "PASSWORD_ERROR" | "REGISTER_ERROR"
export type AnyMessage = SuccessMessage | RequestMessage | ErrorMessage;