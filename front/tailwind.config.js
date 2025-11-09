/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: [
    "./app/**/*.{js,jsx,ts,tsx}",
    "./components/**/*.{js,jsx,ts,tsx}",
    "./lib/**/*.{js,ts}",
  ],
  theme: {
    extend: {
      colors: {
        bg: 'var(--color-bg)',
        card: 'var(--color-card)',
        text: 'var(--color-text)',
        muted: 'var(--color-muted)',
        primary: 'var(--color-primary)',
        border: 'var(--color-border)',
        success: '#10B981',
        danger: '#EF4444',
      },
      fontSize: {
        'title': ['24px', { lineHeight: '32px', fontWeight: '700' }], // principal
        'subtitle': ['18px', { lineHeight: '24px', fontWeight: '600' }], // subtítulos
        'base-sm': ['14px', { lineHeight: '20px', fontWeight: '400' }], // texto normal
        'aux': ['12px', { lineHeight: '16px', fontWeight: '400' }], // texto auxiliar
        'btn': ['14px', { lineHeight: '20px', fontWeight: '600' }], // botões
      }
    },
  },
  plugins: [],
}