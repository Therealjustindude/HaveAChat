import jwt from 'jsonwebtoken';
import { parse } from 'cookie';

export function getUserFromRequest(request: Request) {
  const cookieHeader = request.headers.get('cookie') || '';
  const cookies = parse(cookieHeader);
  const token = cookies['access_token'];
  if (!token) return null;

  try {
    const secretBase64 = import.meta.env.VITE_JWT_SECRET;
    const secret = Buffer.from(secretBase64, 'base64');
    if (!secret) throw new Error('Missing JWT_SECRET environment variable');

    const decoded = jwt.verify(token, secret);
    return decoded;
  } catch (err) {
    console.error('[getUserFromRequest] Invalid JWT:', err);
    return null;
  }
}