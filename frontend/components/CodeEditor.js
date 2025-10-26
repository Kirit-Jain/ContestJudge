export default function CodeEditor({ value, onChange }) {
  return (
    <div className="bg-white rounded-lg shadow">
      <div className="px-4 py-2 border-b border-gray-200">
        <h3 className="text-lg font-medium">Code Editor (Java)</h3>
      </div>
      <textarea
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full h-96 font-mono text-sm p-4 focus:outline-none resize-none"
        spellCheck="false"
      />
    </div>
  );
}