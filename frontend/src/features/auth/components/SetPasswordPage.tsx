import { AuthForm } from "./AuthForm";

export const SetPasswordPage = () => {
  return (
    <AuthForm
      title="Set your password"
      subtitle="Create a password to activate your account"
    >
      <input type="password" placeholder="New password" />
      <input type="password" placeholder="Confirm password" />

      <button type="submit">Set Password</button>
    </AuthForm>
  );
};
