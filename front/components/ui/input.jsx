// ...existing code...
'use client';
import React from 'react';

export default function Input({ label, name, error, className = '', ...props }) {
  return (
    <div className={className}>
      {label && <label htmlFor={name} className="block mb-1 text-aux muted">{label}</label>}
      <input
        id={name}
        name={name}
        className="w-full border border-border rounded px-3 py-2 bg-card text-text"
        {...props}
      />
      {error && <div className="text-red-600 text-sm mt-1">{error}</div>}
    </div>
  );
}