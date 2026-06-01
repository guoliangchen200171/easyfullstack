import { useState } from "react";

const PaginationBar = ({ page, totalPages, totalElements, onPageChange }) => {
  const [jumpInput, setJumpInput] = useState("");

  if (totalPages <= 1 && totalElements <= 0) {
    return null;
  }

  const parsed = parseInt(jumpInput, 10);
  const isValid = !isNaN(parsed) && parsed >= 1 && parsed <= totalPages;

  const handleJump = () => {
    if (!isValid) return;
    onPageChange(parsed - 1);
    setJumpInput("");
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleJump();
  };

  return (
    <div className="d-flex justify-content-between align-items-center mt-3 mb-4">
      <span className="text-muted">
        第 {page + 1} / {Math.max(totalPages, 1)} 页，共 {totalElements} 条
      </span>
      <div className="d-flex align-items-center gap-3">
        <div className="btn-group">
          <button
            type="button"
            className="btn btn-outline-primary btn-sm"
            disabled={page <= 0}
            onClick={() => onPageChange(page - 1)}
          >
            上一页
          </button>
          <button
            type="button"
            className="btn btn-outline-primary btn-sm"
            disabled={page >= totalPages - 1}
            onClick={() => onPageChange(page + 1)}
          >
            下一页
          </button>
        </div>
        <div className="input-group input-group-sm" style={{ width: "180px" }}>
          <span className="input-group-text">跳转到第</span>
          <input
            type="number"
            className="form-control"
            min="1"
            max={totalPages}
            value={jumpInput}
            onChange={(e) => setJumpInput(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder={`1-${totalPages}`}
          />
          <button
            type="button"
            className="btn btn-outline-primary"
            disabled={!isValid}
            onClick={handleJump}
          >
            跳转
          </button>
        </div>
      </div>
    </div>
  );
};

export default PaginationBar;
