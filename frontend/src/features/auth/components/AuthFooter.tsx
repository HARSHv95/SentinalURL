import { Link } from "react-router-dom";

interface AuthFooterProps {
  text: string;
  linkText: string;
  to: string;
}

const AuthFooter = ({
  text,
  linkText,
  to,
}: AuthFooterProps) => {
  return (
    <p className="text-center text-sm text-muted-foreground">
      {text}{" "}
      <Link
        to={to}
        className="font-medium text-primary hover:underline"
      >
        {linkText}
      </Link>
    </p>
  );
};

export default AuthFooter;