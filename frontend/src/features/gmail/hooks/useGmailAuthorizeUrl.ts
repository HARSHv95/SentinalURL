import { useMutation } from "@tanstack/react-query";

import { getAuthorizeUrl } from "../api/gmailApi";

export function useGmailAuthorizeUrl() {
  return useMutation({
    mutationFn: () => getAuthorizeUrl(),

    onSuccess(url) {
      window.location.href = url;
    },
  });
}
