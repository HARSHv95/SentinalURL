import { useSearchParams } from "react-router-dom";

import AuthCard from "../components/AuthCard";
import LoginForm from "../components/LoginForm";

const LoginPage = () => {
  const [searchParams] = useSearchParams();
  const justRegistered = searchParams.get("registered") === "true";

  return (
    <AuthCard
      title="Welcome Back"
      description="Sign in to continue to SentinalURL."
    >
      {justRegistered && (
        <p className="mb-4 text-center text-sm text-green-700 dark:text-green-400">
          Account created — please sign in.
        </p>
      )}

      <LoginForm />
    </AuthCard>
  );
};

export default LoginPage;