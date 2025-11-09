// ...existing code...
'use client';
import clsx from 'clsx';

export default function Button({ children, variant = 'primary', className = '', ...props }) {
  const base = 'inline-flex items-center justify-center px-4 py-2 rounded-md font-semibold text-btn';
  const variants = {
    primary: 'bg-primary text-white hover:brightness-95',
    ghost: 'bg-transparent border border-border text-text',
    danger: 'bg-red-600 text-white hover:bg-red-700',
    secondary: 'bg-gray-100 dark:bg-gray-700 text-text',
  };
  return (
    <button className={clsx(base, variants[variant], className)} {...props}>
      {children}
    </button>
  );
}