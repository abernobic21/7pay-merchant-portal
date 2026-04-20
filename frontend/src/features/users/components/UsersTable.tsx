import { useTranslation } from "react-i18next";
import type { User } from "../types/User";
import { getPages } from "@/shared/components/getPages";
import { SortableHeader } from "@/shared/components/SortableHeader";

interface UsersTableProps {
  users: User[];
  currentPage: number;
  totalPages: number;
  totalElements: number;
  currentSort?: string;
  onPageChange: (page: number) => void;
  onEdit: (user: User) => void;
  onDelete: (user: User) => void;
  onStatus: (user: User) => void;
  onSort: (sort: string) => void;
}

export const UsersTable = ({
  users,
  currentPage,
  totalPages,
  totalElements,
  currentSort,
  onPageChange,
  onEdit,
  onDelete,
  onStatus,
  onSort,
}: UsersTableProps) => {
  const { t } = useTranslation(["users", "common"]);
  const pages = getPages(currentPage, totalPages);

  return (
    <div>
      <h2>{t("title")}</h2>
      <p>
        {t("totalUsers")} {totalElements}
      </p>

      <table>
        <thead>
          <tr>
            <SortableHeader
              label={t("table.firstName")}
              field="firstName"
              currentSort={currentSort}
              onSort={onSort}
            />
            <SortableHeader
              label={t("table.lastName")}
              field="lastName"
              currentSort={currentSort}
              onSort={onSort}
            />
            <th>{t("table.email")}</th>
            <th>{t("table.phone")}</th>
            <th>{t("table.merchantId")}</th>
            <th>{t("table.role")}</th>
            <th>{t("table.accessLevel")}</th>
            <th>{t("table.status")}</th>
            <SortableHeader
              label={t("table.createdAt")}
              field="createdAt"
              currentSort={currentSort}
              onSort={onSort}
            />{" "}
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

        {pages.map((p, i) =>
          p === "..." ? (
            <span key={i}>...</span>
          ) : (
            <button
              key={p}
              disabled={p === currentPage}
              onClick={() => onPageChange(p as number)}
            >
              {(p as number) + 1}
            </button>
          ),
        )}

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
