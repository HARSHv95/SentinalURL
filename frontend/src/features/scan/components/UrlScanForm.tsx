import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "../../../components/ui/button";
import { Input } from "../../../components/ui/input";

import {
    scanSchema,
    type ScanFormData,
} from "../schemas/scanSchema";

interface Props {
    onSubmit: (data: ScanFormData) => void;
    isLoading: boolean;
}

export default function UrlScanForm({
    onSubmit,
    isLoading,
}: Props) {

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<ScanFormData>({
        resolver: zodResolver(scanSchema),
    });

    return (
        <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-4"
        >

            <Input
                placeholder="https://example.com"
                {...register("url")}
            />

            {errors.url && (
                <p className="text-sm text-red-500">
                    {errors.url.message}
                </p>
            )}

            <Button
                type="submit"
                disabled={isLoading}
                className="w-full"
            >
                {isLoading ? "Creating Scan..." : "Scan URL"}
            </Button>

        </form>
    );
}