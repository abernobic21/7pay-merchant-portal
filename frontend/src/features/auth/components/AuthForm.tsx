type AuthFormProps = {
  title: string;
  subtitle?: string;
  children: React.ReactNode;
  footer?: React.ReactNode;
};

export const AuthForm = ({
  title,
  subtitle,
  children,
  footer,
}: AuthFormProps) => {
  return (
    <div>
      <h2>{title}</h2>
      {subtitle && <p>{subtitle}</p>}

      <form>{children}</form>

      {footer && <div>{footer}</div>}
    </div>
  );
};
