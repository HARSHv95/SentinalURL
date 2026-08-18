import { z } from "zod";

export const registerSchema = z
  .object({
    firstName: z
      .string()
      .min(2, "First name must contain at least 2 characters."),

    lastName: z
      .string()
      .min(2, "Last name must contain at least 2 characters."),

    email: z.email("Please enter a valid email."),

    password: z
      .string()
      .min(8, "Password must be at least 8 characters.")
      .regex(
        /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}$/,
        "Password must contain an uppercase letter, lowercase letter, digit, and special character (@#$%^&+=!)."
      ),

    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    path: ["confirmPassword"],
    message: "Passwords do not match.",
  });

export type RegisterFormData = z.infer<typeof registerSchema>;