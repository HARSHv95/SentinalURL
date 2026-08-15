import { z } from "zod";

export const watchlistSchema = z.object({
    url: z
        .string()
        .url("Please enter a valid URL."),
});

export type WatchlistFormData =
    z.infer<typeof watchlistSchema>;
