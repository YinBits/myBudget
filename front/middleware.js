import { NextResponse } from 'next/server';

export function middleware(request) {
  const token = request.cookies.get('token')?.value;
  const { pathname } = request.nextUrl;

  
  const publicRoutes = ['/login', '/register'];
  const isPublicRoute = publicRoutes.some(route => pathname.startsWith(route));


  return NextResponse.next();
}

export const config = {
  matcher: [
    '/((?!api|_next/static|_next/image|favicon.ico).*)',
  ],
};