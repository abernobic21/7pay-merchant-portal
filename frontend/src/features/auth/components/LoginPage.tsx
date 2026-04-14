import { Link } from "react-router-dom";
import { AuthForm } from "./AuthForm";
import { ROUTES } from "@/router/routes";

export const LoginPage = () => {
  return (
    <AuthForm
      title="Login"
      footer={<Link to={ROUTES.FORGOT_PASSWORD}>Forgot password?</Link>}
    >
      <input type="email" placeholder="Email" />
      <input type="password" placeholder="Password" />

      <button type="submit">Login</button>
    </AuthForm>
  );
};
