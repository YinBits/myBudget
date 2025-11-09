'use client';
import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Sun, Moon, LogOut, Menu, X } from 'lucide-react';
import { useAuth } from '@/context/auth-context';

export default function Header() {
  const { user, logout } = useAuth();
  const router = useRouter();
  const [isDark, setIsDark] = useState(false);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  useEffect(() => {
    if (typeof window === 'undefined') return;
    const saved = localStorage.getItem('theme');
    const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
    const active = saved ? saved === 'dark' : prefersDark;
    setIsDark(active);
    if (active) document.documentElement.classList.add('dark');
    else document.documentElement.classList.remove('dark');
  }, []);

  const toggleTheme = () => {
    const next = !isDark;
    setIsDark(next);
    if (next) document.documentElement.classList.add('dark');
    else document.documentElement.classList.remove('dark');
    localStorage.setItem('theme', next ? 'dark' : 'light');
  };

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  return (
    <>
      <header className="flex items-center justify-between gap-4 px-4 sm:px-6 py-3 sm:py-4 bg-card border-b sticky top-0 z-50" style={{ borderColor: 'var(--color-border)' }}>
        {/* Logo */}
        <div className="flex items-center gap-2 sm:gap-3 flex-shrink-0">
          <Link href="/dashboard" className="flex items-center gap-2 sm:gap-3">
            <div className="w-8 h-8 sm:w-10 sm:h-10 rounded-md bg-primary flex items-center justify-center text-white font-bold text-sm sm:text-base">
              MB
            </div>
            <div className="leading-tight">
              <div className="text-lg sm:text-xl font-bold">MyBudget</div>
              <div className="text-xs text-muted hidden sm:block">Controle financeiro</div>
            </div>
          </Link>
        </div>

        {/* Desktop Actions */}
        <div className="flex items-center gap-2 sm:gap-4">
          <button
            aria-label="Toggle theme"
            onClick={toggleTheme}
            className="p-1.5 sm:p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors flex-shrink-0"
            title="Alternar tema"
          >
            {isDark ? (
              <Sun className="w-4 h-4 sm:w-5 sm:h-5" />
            ) : (
              <Moon className="w-4 h-4 sm:w-5 sm:h-5" />
            )}
          </button>

          {/* Desktop User Info */}
          <div className="hidden sm:flex sm:items-center sm:gap-3">
            <div className="text-sm max-w-[120px] lg:max-w-[200px] truncate">
              {user?.email ?? 'Usuário'}
            </div>
            <button
              onClick={handleLogout}
              className="p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 flex items-center gap-2 text-sm transition-colors"
              title="Sair"
            >
              <LogOut className="w-4 h-4" />
              <span className="hidden lg:inline">Sair</span>
            </button>
          </div>

          {/* Mobile Menu Button */}
          <button
            onClick={toggleMobileMenu}
            className="sm:hidden p-1.5 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors flex-shrink-0"
            aria-label="Menu"
          >
            {isMobileMenuOpen ? (
              <X className="w-5 h-5" />
            ) : (
              <Menu className="w-5 h-5" />
            )}
          </button>
        </div>
      </header>

      {/* Mobile Menu Overlay */}
      {isMobileMenuOpen && (
        <div className="sm:hidden fixed inset-0 z-40 bg-black bg-opacity-50" onClick={toggleMobileMenu} />
      )}

      {/* Mobile Menu */}
      <div className={`
        sm:hidden fixed top-16 right-0 z-50 w-64 bg-card border-l border-b
        transform transition-transform duration-300 ease-in-out
        ${isMobileMenuOpen ? 'translate-x-0' : 'translate-x-full'}
      `} style={{ borderColor: 'var(--color-border)' }}>
        <div className="p-4 space-y-4">
          {/* User Info Mobile */}
          <div className="pb-3 border-b" style={{ borderColor: 'var(--color-border)' }}>
            <div className="text-sm font-medium">Olá,</div>
            <div className="text-sm text-muted truncate">
              {user?.email ?? 'Usuário'}
            </div>
          </div>

          {/* Navigation Links Mobile */}
          <nav className="space-y-2">
            <Link 
              href="/dashboard" 
              className="block px-3 py-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
              onClick={toggleMobileMenu}
            >
              Dashboard
            </Link>
            <Link 
              href="/transactions" 
              className="block px-3 py-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
              onClick={toggleMobileMenu}
            >
              Transações
            </Link>
            <Link 
              href="/reports" 
              className="block px-3 py-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
              onClick={toggleMobileMenu}
            >
              Relatórios
            </Link>
          </nav>

          {/* Theme Toggle Mobile */}
          <button
            onClick={toggleTheme}
            className="w-full flex items-center gap-2 px-3 py-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
          >
            {isDark ? (
              <Sun className="w-4 h-4" />
            ) : (
              <Moon className="w-4 h-4" />
            )}
            <span>{isDark ? 'Modo Claro' : 'Modo Escuro'}</span>
          </button>

          {/* Logout Button Mobile */}
          <button
            onClick={() => {
              toggleMobileMenu();
              handleLogout();
            }}
            className="w-full flex items-center gap-2 px-3 py-2 rounded-md hover:bg-red-50 dark:hover:bg-red-900/20 text-red-600 dark:text-red-400 transition-colors mt-4"
          >
            <LogOut className="w-4 h-4" />
            <span>Sair</span>
          </button>
        </div>
      </div>

      {/* Prevent body scroll when mobile menu is open */}
      <style jsx global>{`
        ${isMobileMenuOpen ? `
          body {
            overflow: hidden;
          }
        ` : ''}
      `}</style>
    </>
  );
}