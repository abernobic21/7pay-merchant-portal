export const ChangePasswordPage = () => {
  return (
    <div>
      <h2>Change Password</h2>

      <form>
        <input type="password" placeholder="Current password" />
        <input type="password" placeholder="New password" />
        <input type="password" placeholder="Confirm password" />

        <button type="submit">Change password</button>
      </form>
    </div>
  );
};
