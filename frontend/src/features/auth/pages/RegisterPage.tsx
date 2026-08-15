import AuthCard from "../components/AuthCard";
import RegisterForm from "../components/RegisterForm";

const RegisterPage = () => {
  return (
    <AuthCard
      title="Create Account"
      description="Create your SentinelURL account."
    >
      <RegisterForm />
    </AuthCard>
  );
};

export default RegisterPage;