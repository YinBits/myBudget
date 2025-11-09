// ...existing code...
'use client';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { Home, List, FileText } from 'lucide-react';

const navItems = [
  { href: '/dashboard', label: 'Dashboard', icon: Home },
  { href: '/transactions', label: 'Transações', icon: List },
  { href: '/reports', label: 'Relatórios', icon: FileText },
];

export default function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-64 bg-card border-r hidden md:flex flex-col p-4" style={{ borderColor: 'var(--color-border)' }}>
      <nav className="flex flex-col gap-2">
        {navItems.map((item) => {
          const active = pathname?.startsWith(item.href);
          const Icon = item.icon;
          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center gap-3 px-3 py-2 rounded-md ${active ? 'bg-primary text-white' : 'text-text hover:bg-gray-50 dark:hover:bg-gray-800'}`}
            >
              <Icon size={18} />
              <span className="font-medium">{item.label}</span>
            </Link>
          );
        })}
      </nav>

      <div className="mt-auto text-aux muted text-sm">
        <div className="pt-6">© {new Date().getFullYear()} MyBudget</div>
      </div>
    </aside>
  );
}