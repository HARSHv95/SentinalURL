import { z } from "zod";

export const scanSchema = z.object({
    url: z
        .string()
        .url("Please enter a valid URL."),
});

export type ScanFormData =
    z.infer<typeof scanSchema>;