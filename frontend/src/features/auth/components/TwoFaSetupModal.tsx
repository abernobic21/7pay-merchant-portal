import { useTranslation } from "react-i18next";
import { QRCodeSVG } from "qrcode.react";
import { getApiError } from "@/shared/utils/getApiError";
import { useTwoFaSetup } from "../hooks/useTwoFaSetup";

interface Props {
  onClose: () => void;
}

export const TwoFaSetupModal = ({ onClose }: Props) => {
  const { t } = useTranslation("users");
  const { step, setupQuery, verifyMutation, handleVerify, handleDone } =
    useTwoFaSetup(onClose);

  const handleVerifySubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const code = new FormData(e.currentTarget).get("code") as string;
    handleVerify(code);
  };

  if (setupQuery.isLoading) return <p>{t("twoFa.loading")}</p>;
  if (setupQuery.isError) return <p>{t("twoFa.setupError")}</p>;

  const { secret, qrCodeUrl, backupCodes } = setupQuery.data!;

  return (
    <div>
      <h2>{t("twoFa.setupTitle")}</h2>

      {step === "qr" && (
        <div>
          <p>{t("twoFa.scanQr")}</p>
          <QRCodeSVG value={qrCodeUrl} size={200} />
          <p>{t("twoFa.manualSecret")}</p>
          <code>{secret}</code>
          <button type="button" onClick={() => verifyMutation.reset()}></button>
          <form onSubmit={handleVerifySubmit}>
            <input
              name="code"
              type="text"
              placeholder={t("twoFa.codePlaceholder")}
              autoComplete="one-time-code"
              required
            />
            {verifyMutation.error && (
              <p>{getApiError(verifyMutation.error, "twoFa")}</p>
            )}
            <button type="button" onClick={onClose}>
              {t("common:cancel")}
            </button>
            <button type="submit" disabled={verifyMutation.isPending}>
              {t("twoFa.verify")}
            </button>
          </form>
        </div>
      )}

      {step === "codes" && (
        <div>
          <p>{t("twoFa.saveBackupCodes")}</p>
          <ul>
            {backupCodes.map((code) => (
              <li key={code}>
                <code>{code}</code>
              </li>
            ))}
          </ul>
          <button type="button" onClick={handleDone}>
            {t("twoFa.done")}
          </button>
        </div>
      )}
    </div>
  );
};
