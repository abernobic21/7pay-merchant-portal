import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useMe } from "../hooks/useMe";
import { ChangePasswordModal } from "./ChangePasswordModal";

export const ProfilePage = () => {
  const { t } = useTranslation(["users", "common"]);
  const { data: profile, isLoading, isError } = useMe();
  const [isChangePasswordOpen, setIsChangePasswordOpen] = useState(false);

  if (isLoading) return <p>{t("common:loading")}</p>;
  if (isError || !profile) return <p>{t("errors.fetch")}</p>;

  return (
    <div>
      <h1>{t("profile.title")}</h1>

      <div>
        <p>
          {t("table.firstName")}: {profile.firstName}
        </p>
        <p>
          {t("table.lastName")}: {profile.lastName}
        </p>
        <p>
          {t("table.email")}: {profile.email}
        </p>
        <p>
          {t("table.phone")}: {profile.phone}
        </p>
        <p>
          {t("table.role")}: {t(`role.${profile.role}`)}
        </p>
        <p>
          {t("table.accessLevel")}: {t(`accessLevel.${profile.accessLevel}`)}
        </p>
        <p>
          {t("table.status")}: {t(`status.${profile.status}`)}
        </p>
      </div>

      <button onClick={() => setIsChangePasswordOpen(true)}>
        {t("profile.changePassword")}
      </button>

      {isChangePasswordOpen && (
        <ChangePasswordModal onClose={() => setIsChangePasswordOpen(false)} />
      )}
    </div>
  );
};
