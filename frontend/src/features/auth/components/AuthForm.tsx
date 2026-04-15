type AuthFormProps = {
  title: string;
  subtitle?: string;
  children: React.ReactNode;
  footer?: React.ReactNode;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
  isLoading?: boolean;
  submitLabel: string;
};

export const AuthForm = ({
  title,
  subtitle,
  children,
  footer,
  onSubmit,
  isLoading,
  submitLabel,
}: AuthFormProps) => {
  return (
    <div>
      <h2>{title}</h2>
      {subtitle && <p>{subtitle}</p>}

      <form onSubmit={onSubmit} className="auth-form">
        {children}

        <button type="submit" disabled={isLoading}>
          {submitLabel}
        </button>
      </form>

      {footer && <div>{footer}</div>}
    </div>
  );
};
