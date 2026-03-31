import i18n from "i18next";
import { initReactI18next } from "react-i18next";

import merchantsEn from "@/features/core/merchants/i18n/en.json";
import merchantsHr from "@/features/core/merchants/i18n/hr.json";

i18n.use(initReactI18next).init({
  lng: "en",
  fallbackLng: "en",
  resources: {
    en: {
      merchants: merchantsEn,
    },
    hr: {
      merchants: merchantsHr,
    },
  },
});

export default i18n;
