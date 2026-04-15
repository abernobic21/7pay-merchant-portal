import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useUsers } from "../hooks/useUsers";
import type { UserFilters } from "../types/User";
import { UsersTable } from "./UsersTable";
import { CreateUserModal } from "./CreateUserModal";

export const UsersPage = () => {
  const { t } = useTranslation("users");

  const [filters, setFilters] = useState<UserFilters>({
    page: 0,
    size: 10,
  });

  const [isCreateOpen, setIsCreateOpen] = useState(false);

  const { data, isLoading, isError } = useUsers(filters);

  const handlePageChange = (page: number) => {
    setFilters((prev) => ({ ...prev, page }));
  };

  const handleSizeChange = (size: number) => {
    setFilters((prev) => ({ ...prev, page: 0, size }));
  };

  const handleStatusFilter = (status: UserFilters["status"]) => {
    setFilters((prev) => ({ ...prev, page: 0, status }));
  };

  const handleRoleFilter = (role: UserFilters["role"]) => {
    setFilters((prev) => ({ ...prev, page: 0, role }));
  };

  if (isError) return <p>{t("errors.fetch")}</p>;

  return (
    <div>
      <div>
        <h1>{t("title")}</h1>
        <button onClick={() => setIsCreateOpen(true)}>
          {t("actions.create")}
        </button>
      </div>

      <div>
        <select
          value={filters.status ?? ""}
          onChange={(e) =>
            handleStatusFilter(
              (e.target.value as UserFilters["status"]) || undefined,
            )
          }
        >
          <option value="">{t("filters.all")}</option>
          <option value="active">{t("status.active")}</option>
          <option value="deactivated">{t("status.deactivated")}</option>
        </select>

        <select
          value={filters.role ?? ""}
          onChange={(e) =>
            handleRoleFilter(
              (e.target.value as UserFilters["role"]) || undefined,
            )
          }
        >
          <option value="">{t("filters.all")}</option>
          <option value="admin">{t("role.admin")}</option>
          <option value="user">{t("role.user")}</option>
        </select>

        <select
          value={filters.size}
          onChange={(e) => handleSizeChange(Number(e.target.value))}
        >
          <option value={10}>10</option>
          <option value={25}>25</option>
          <option value={50}>50</option>
        </select>
      </div>

      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <>
          <UsersTable
            users={data?.content ?? []}
            onPageChange={handlePageChange}
            currentPage={filters.page ?? 0}
            totalPages={data?.page.totalPages ?? 0}
            totalElements={data?.page.totalElements ?? 0}
          />
        </>
      )}

      {isCreateOpen && (
        <CreateUserModal onClose={() => setIsCreateOpen(false)} />
      )}
    </div>
  );
};
