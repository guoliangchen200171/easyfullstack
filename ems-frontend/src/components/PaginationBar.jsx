const PaginationBar = ({ page, totalPages, totalElements, onPageChange }) => {
  if (totalPages <= 1 && totalElements <= 0) {
    return null;
  }

  return (
    <div className="d-flex justify-content-between align-items-center mt-3 mb-4">
      <span className="text-muted">
        第 {page + 1} / {Math.max(totalPages, 1)} 页，共 {totalElements} 条
      </span>
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
    </div>
  );
};

export default PaginationBar;
