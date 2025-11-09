'use client';

export default function Modal({ open, onClose, title, children, footer }) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      {/* Overlay */}
      <div className="fixed inset-0 bg-black/30 transition-opacity" onClick={onClose} />
      
      {/* Modal */}
      <div className="flex min-h-full items-end justify-center p-4 sm:items-center sm:p-0">
        <div className="relative transform overflow-hidden card w-full max-w-lg">
          {/* Header */}
          <div className="px-6 py-4 border-b border-border">
            <h3 className="text-subtitle">{title}</h3>
          </div>

          {/* Content */}
          <div className="px-6 py-4">
            {children}
          </div>

          {/* Footer */}
          {footer && (
            <div className="px-6 py-4 border-t border-border flex justify-end gap-3">
              {footer}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}