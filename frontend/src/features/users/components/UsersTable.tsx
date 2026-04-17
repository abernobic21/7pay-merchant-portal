import { useTranslation } from "react-i18next";
import type { User } from "../types/User";

interface UsersTableProps {
  users: User[];
  currentPage: number;
  totalPages: number;
  totalElements: number;
  onPageChange: (page: number) => void;
  onEdit: (user: User) => void;
  onDelete: (user: User) => void;
  onStatus: (user: User) => void;
}

export const UsersTable = ({
  users,
  currentPage,
  totalPages,
  totalElements,
  onPageChange,
  onEdit,
  onDelete,
  onStatus,
}: UsersTableProps) => {
  const { t } = useTranslation(["users", "common"]);

  return (
    <div>
      <h2>{t("title")}</h2>
      <p>
        {t("totalUsers")} {totalElements}
      </p>

      <table>
        <thead>
          <tr>
            <th>{t("table.firstName")}</th>
            <th>{t("table.lastName")}</th>
            <th>{t("table.email")}</th>
            <th>{t("table.phone")}</th>
            <th>{t("table.merchantId")}</th>
            <th>{t("table.role")}</th>
            <th>{t("table.accessLevel")}</th>
            <th>{t("table.status")}</th>
            <th>{t("table.createdAt")}</th>
            <th>{t("table.firstLoginFlag")}</th>
            <th>{t("table.actions")}</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.firstName}</td>
              <td>{user.lastName}</td>
              <td>{user.email}</td>
              <td>{user.phone}</td>
              <td>{user.merchantId ?? "-"}</td>
              <td>{t(`role.${user.role}`)}</td>
              <td>{t(`accessLevel.${user.accessLevel}`)}</td>
              <td>
                <button onClick={() => onStatus(user)}>
                  {t(`status.${user.status.toLowerCase()}`)}
                </button>
              </td>
              <td>{new Date(user.createdAt).toLocaleDateString()}</td>
              <td>{user.firstLoginFlag ? t("common:yes") : t("common:no")}</td>
              <td>
                <button onClick={() => onEdit(user)}>
                  {t("actions.edit")}
                </button>
                <button onClick={() => onDelete(user)}>
                  {t("actions.delete")}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div>
        <button
          disabled={currentPage === 0}
          onClick={() => onPageChange(currentPage - 1)}
        >
          ←
        </button>

        {Array.from({ length: totalPages }, (_, i) => (
          <button
            key={i}
            disabled={i === currentPage}
            onClick={() => onPageChange(i)}
          >
            {i + 1}
          </button>
        ))}

        <button
          disabled={currentPage === totalPages - 1}
          onClick={() => onPageChange(currentPage + 1)}
        >
          →
        </button>
      </div>
    </div>
  );
};
