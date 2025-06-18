interface DefaultCatchBoundaryProps {
  error: Error;
}

export function DefaultCatchBoundary({ error }: DefaultCatchBoundaryProps) {
  return (
    <div className="p-8 text-center">
      <h1 className="text-2xl font-bold text-red-600">Something went wrong</h1>
      <p className="mt-4 text-gray-700">{error.message}</p>
    </div>
  );
}