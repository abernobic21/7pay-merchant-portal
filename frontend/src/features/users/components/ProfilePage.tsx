import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useMe } from "../hooks/useMe";
import { ChangePasswordModal } from "./ChangePasswordModal";
import { TwoFaSetupModal } from "@/features/auth/components/TwoFaSetupModal";
import { TwoFaDisableModal } from "@/features/auth/components/TwoFaDisableModal";

export const ProfilePage = () => {
  const { t } = useTranslation(["users", "common"]);
  const { data: profile, isLoading, isError } = useMe();
  const [isChangePasswordOpen, setIsChangePasswordOpen] = useState(false);
  const [is2FaSetupOpen, setIs2FaSetupOpen] = useState(false);
  const [is2FaDisableOpen, setIs2FaDisableOpen] = useState(false);

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

      <div>
        <h2>{t("twoFa.sectionTitle")}</h2>
        <p>{t(profile.twoFaEnabled ? "twoFa.enabled" : "twoFa.disabled")}</p>
        {profile.twoFaEnabled ? (
          <button onClick={() => setIs2FaDisableOpen(true)}>
            {t("twoFa.disable")}
          </button>
        ) : (
          <button onClick={() => setIs2FaSetupOpen(true)}>
            {t("twoFa.setup")}
          </button>
        )}
      </div>

      {isChangePasswordOpen && (
        <ChangePasswordModal onClose={() => setIsChangePasswordOpen(false)} />
      )}
      {is2FaSetupOpen && (
        <TwoFaSetupModal onClose={() => setIs2FaSetupOpen(false)} />
      )}
      {is2FaDisableOpen && (
        <TwoFaDisableModal onClose={() => setIs2FaDisableOpen(false)} />
      )}
    </div>
  );
};
