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
  registerSchema,
  type RegisterFormData,
} from "../schemas/registerSchema";
import { useRegister } from "../hooks/useRegister";

const RegisterForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  const registerMutation = useRegister();
  const navigate = useNavigate();

  const onSubmit = async (data: RegisterFormData) => {
    try {
      await registerMutation.mutateAsync(data);
      navigate(`${ROUTES.LOGIN}?registered=true`);
    } catch {
      // surfaced below via registerMutation.isError
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="firstName">First Name</Label>
          <Input
            id="firstName"
            placeholder="John"
            {...register("firstName")}
          />
          {errors.firstName && (
            <p className="text-sm text-destructive">
              {errors.firstName.message}
            </p>
          )}
        </div>

        <div className="space-y-2">
          <Label htmlFor="lastName">Last Name</Label>
          <Input
            id="lastName"
            placeholder="Doe"
            {...register("lastName")}
          />
          {errors.lastName && (
            <p className="text-sm text-destructive">
              {errors.lastName.message}
            </p>
          )}
        </div>
      </div>

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

      <div className="space-y-2">
        <Label htmlFor="confirmPassword">
          Confirm Password
        </Label>

        <PasswordInput
    id="confirmPassword"
    placeholder="Confirm Password"
    {...register("confirmPassword")}
/>

{errors.confirmPassword && (
    <p className="text-sm text-destructive">
        {errors.confirmPassword.message}
    </p>
)}
      </div>

      {registerMutation.isError && (
        <p className="text-sm text-destructive">
          Couldn't create your account. Please check your details and try again.
        </p>
      )}

      <Button
    type="submit"
    className="w-full"
    disabled={isSubmitting || registerMutation.isPending}
>
    {isSubmitting || registerMutation.isPending ? "Creating Account..." : "Create Account"}
</Button>

      <AuthFooter
        text="Already have an account?"
        linkText="Login"
        to={ROUTES.LOGIN}
      />

    </form>
  );
};

export default RegisterForm;
