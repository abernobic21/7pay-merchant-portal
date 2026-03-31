import i18n from "i18next";
import { initReactI18next } from "react-i18next";

import merchantsEn from "@/features/core/merchants/i18n/en.json";
import merchantsHr from "@/features/core/merchants/i18n/hr.json";
import salepointsEn from "@/features/core/salepoints/i18n/en.json";
import salepointsHr from "@/features/core/salepoints/i18n/hr.json";

i18n.use(initReactI18next).init({
  lng: "en",
  fallbackLng: "en",
  resources: {
    en: {
      merchants: merchantsEn,
      salepoints: salepointsEn,
    },
    hr: {
      merchants: merchantsHr,
      salepoints: salepointsHr,
    },
  },
});

export default i18n;
