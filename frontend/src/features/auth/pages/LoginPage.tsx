import AuthCard from "../components/AuthCard";
import LoginForm from "../components/LoginForm";

const LoginPage = () => {
  return (
    <AuthCard
      title="Welcome Back"
      description="Sign in to continue to SentinelURL."
    >
      <LoginForm />
    </AuthCard>
  );
};

export default LoginPage;