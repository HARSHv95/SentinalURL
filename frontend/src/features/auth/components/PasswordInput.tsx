import { useState } from "react";
import { Eye, EyeOff } from "lucide-react";

import { Input } from "../../../components/ui/input";
import { Button } from "../../../components/ui/button";

import type { InputHTMLAttributes } from "react";

interface PasswordInputProps
    extends InputHTMLAttributes<HTMLInputElement> {}

const PasswordInput = ({
    ...props
}: PasswordInputProps) => {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="relative">
      <Input
    type={showPassword ? "text" : "password"}
    {...props}
/>

      <Button
        type="button"
        variant="ghost"
        size="icon"
        onClick={() => setShowPassword((prev) => !prev)}
        className="absolute right-2 top-1/2 -translate-y-1/2"
      >
        {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
      </Button>
    </div>
  );
};

export default PasswordInput;