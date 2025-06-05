import { API_BASE_URL } from "../../utils/ApiBaseUrl";

export const fetchWithAuth = async (inputReq: RequestInfo, init?: RequestInit): Promise<Response> => {
  let res = await fetch(inputReq, {
    ...init,
    credentials: 'include',
  });

  if (res.status === 401) {
    // Try refreshing
    const refreshRes = await fetch(`${API_BASE_URL}/auth/refresh`, {
      method: 'POST',
      credentials: 'include',
    });

    if (!refreshRes.ok) {
      throw new Error('Refresh token invalid or expired');
    }

    // Retry original request
    res = await fetch(inputReq, {
      ...init,
      credentials: 'include',
    });
  }

  return res;
};