import { useNavigate } from "react-router-dom";

import { Button } from "../../../components/ui/button";
import { Input } from "../../../components/ui/input";
import { Label } from "../../../components/ui/label";

import PasswordInput from "./PasswordInput";
import AuthFooter from "./AuthFooter";
import { ROUTES } from "../../../shared/lib/routes";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";


import {
  loginSchema,
  type LoginFormData,
} from "../schemas/loginSchema";
import useAuth from "../hooks/useAuth";
import { useLogin } from "../hooks/useLogin";

const LoginForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors , isSubmitting},
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

const auth = useAuth();
const navigate = useNavigate();
const loginMutation = useLogin();

const onSubmit = async (data: LoginFormData) => {

    try {

        const response = await loginMutation.mutateAsync(data);

        auth.login(response);

        navigate(ROUTES.DASHBOARD);

    } catch (error) {

        console.error(error);

    }

};

  return (
    <form
    onSubmit={handleSubmit(onSubmit)}
    className="space-y-6"
>

      <div className="space-y-2">
        <Label htmlFor="email">Email</Label>

        <Input
    id="email"
    type="email"
    placeholder="john@example.com"
    {...register("email")}
/>

{errors.email && (
    <p className="text-sm text-destructive">
        {errors.email.message}
    </p>
)}
      </div>

      <div className="space-y-2">
        <Label htmlFor="password">Password</Label>

        <PasswordInput
    id="password"
    placeholder="Password"
    {...register("password")}
/>

{errors.password && (
    <p className="text-sm text-destructive">
        {errors.password.message}
    </p>
)}
      </div>

      <label className="flex items-center gap-2 text-sm">
        <input type="checkbox" />
        Remember me
      </label>

      {loginMutation.isError && (
        <p className="text-sm text-destructive">
          Incorrect email or password. Please try again.
        </p>
      )}

      <Button
    type="submit"
    className="w-full"
    disabled={isSubmitting}
>
    {isSubmitting ? "Signing In..." : "Sign In"}
</Button>

      <AuthFooter
        text="Don't have an account?"
        linkText="Register"
        to={ROUTES.REGISTER}
      />

    </form>
  );
};

export default LoginForm;