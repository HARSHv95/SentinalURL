import { useEffect } from "react";
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
    defaultUrl?: string;
}

export default function UrlScanForm({
    onSubmit,
    isLoading,
    defaultUrl,
}: Props) {

    const {
        register,
        handleSubmit,
        setValue,
        formState: { errors },
    } = useForm<ScanFormData>({
        resolver: zodResolver(scanSchema),
    });

    useEffect(() => {
        if (defaultUrl) {
            setValue("url", defaultUrl, { shouldValidate: true });
        }
    }, [defaultUrl, setValue]);

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