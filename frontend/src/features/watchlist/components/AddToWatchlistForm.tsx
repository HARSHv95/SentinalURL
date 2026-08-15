import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "../../../components/ui/button";
import { Input } from "../../../components/ui/input";

import {
    watchlistSchema,
    type WatchlistFormData,
} from "../schemas/watchlistSchema";

interface Props {
    onSubmit: (data: WatchlistFormData) => void;
    isLoading: boolean;
}

export default function AddToWatchlistForm({
    onSubmit,
    isLoading,
}: Props) {

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm<WatchlistFormData>({
        resolver: zodResolver(watchlistSchema),
    });

    const submit = handleSubmit((data) => {
        onSubmit(data);
        reset();
    });

    return (
        <form
            onSubmit={submit}
            className="flex flex-col gap-3 sm:flex-row sm:items-start"
        >
            <div className="flex-1">
                <Input
                    placeholder="https://example.com"
                    {...register("url")}
                />

                {errors.url && (
                    <p className="mt-1 text-sm text-red-500">
                        {errors.url.message}
                    </p>
                )}
            </div>

            <Button
                type="submit"
                disabled={isLoading}
            >
                {isLoading ? "Adding..." : "Add to Watchlist"}
            </Button>

        </form>
    );
}
