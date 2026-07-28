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
const RegisterForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  const onSubmit = async (data: RegisterFormData) => {
    console.log(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      <div className="space-y-2">
        <Label htmlFor="name">Full Name</Label>
        <Input
          id="name"
          placeholder="John Doe"
          {...register("name")}
        />
        {errors.name && (
          <p className="text-sm text-red-500">
            {errors.name.message}
          </p>
        )}
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
    <p className="text-sm text-red-500">
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
    <p className="text-sm text-red-500">
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
    <p className="text-sm text-red-500">
        {errors.confirmPassword.message}
    </p>
)}
      </div>

      <Button
    type="submit"
    className="w-full"
    disabled={isSubmitting}
>
    {isSubmitting ? "Creating Account..." : "Create Account"}
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