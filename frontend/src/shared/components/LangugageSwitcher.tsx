import { useTranslation } from "react-i18next";

export const LanguageSwitcher = () => {
  const { i18n } = useTranslation();

  const toggleLanguage = () => {
    i18n.changeLanguage(i18n.language === "en" ? "hr" : "en");
  };

  return (
    <button onClick={toggleLanguage}>
      {i18n.language === "en" ? "HR" : "EN"}
    </button>
  );
};
