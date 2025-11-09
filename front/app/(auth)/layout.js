export default function AuthLayout({ children }) {
  return (
    <div className="min-h-screen flex items-center justify-center bg-bg transition-colors duration-200 p-4">
      <div className="w-full max-w-md">
        {children}
      </div>
    </div>
  );
}