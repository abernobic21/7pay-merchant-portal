import { AuthForm } from "./AuthForm";

export const ForgotPasswordPage = () => {
  return (
    <AuthForm
      title="Forgot password"
      subtitle="Enter your email to receive reset instructions"
    >
      <input type="email" placeholder="Email" />

      <button type="submit">Send reset link</button>
    </AuthForm>
  );
};
