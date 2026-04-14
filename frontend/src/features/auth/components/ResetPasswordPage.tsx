import { AuthForm } from "./AuthForm";

export const ResetPasswordPage = () => {
  return (
    <AuthForm title="Reset password" subtitle="Enter your new password">
      <input type="password" placeholder="New password" />
      <input type="password" placeholder="Confirm password" />

      <button type="submit">Reset password</button>
    </AuthForm>
  );
};
