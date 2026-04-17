import i18n from "i18next";
import { initReactI18next } from "react-i18next";

import merchantsEn from "@/features/core/merchants/i18n/en.json";
import merchantsHr from "@/features/core/merchants/i18n/hr.json";
import salepointsEn from "@/features/core/salepoints/i18n/en.json";
import salepointsHr from "@/features/core/salepoints/i18n/hr.json";
import authEn from "@/features/auth/i18n/en.json";
import authHr from "@/features/auth/i18n/hr.json";
import usersEn from "@/features/users/i18n/en.json";
import usersHr from "@/features/users/i18n/hr.json";
import commonEn from "@/shared/i18n/en/common.json";
import commonHr from "@/shared/i18n/hr/common.json";
import errorEn from "@/shared/i18n/en/errors.json";
import errorHr from "@/shared/i18n/hr/errors.json";
import dashboardEn from "@/features/core/dashboard/i18n/en.json";
import dashboardHr from "@/features/core/dashboard/i18n/hr.json";
import terminalsEn from "@/features/core/terminals/i18n/en.json";
import terminalsHr from "@/features/core/terminals/i18n/hr.json";
import transactionsEn from "@/features/core/transactions/i18n/en.json";
import transactionsHr from "@/features/core/transactions/i18n/hr.json";

i18n.use(initReactI18next).init({
  lng: "en",
  fallbackLng: "en",
  resources: {
    en: {
      common: commonEn,
      errors: errorEn,
      auth: authEn,
      users: usersEn,
      dashboard: dashboardEn,
      merchants: merchantsEn,
      salepoints: salepointsEn,
      terminals: terminalsEn,
      transactions: transactionsEn,
    },
    hr: {
      common: commonHr,
      errors: errorHr,
      auth: authHr,
      users: usersHr,
      dashboard: dashboardHr,
      merchants: merchantsHr,
      salepoints: salepointsHr,
      terminals: terminalsHr,
      transactions: transactionsHr,
    },
  },
});

export default i18n;
