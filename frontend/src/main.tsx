import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import "./index.css";

import App from "./App";
import AppProviders from "./app/providers/AppProviders";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

createRoot(document.getElementById("root")!).render(
    <StrictMode>
        <AppProviders>
            <QueryClientProvider client={queryClient}>
            <App />
            </QueryClientProvider>
        </AppProviders>
    </StrictMode>
);