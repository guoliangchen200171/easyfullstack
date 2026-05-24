import PaginationBar from "./PaginationBar";
import useAdoptionApprovalHook from "../hooks/useAdoptionApprovalHook";

const AdoptionApprovalComponent = () => {
  const {
    requests,
    page,
    totalPages,
    totalElements,
    statusFilter,
    formatDateTime,
    statusLabel,
    handlePageChange,
    handleStatusFilterChange,
    handleApprove,
    handleDeny,
  } = useAdoptionApprovalHook();

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">领养审批</h2>
      <div className="row mb-3">
        <div className="col-md-4">
          <label htmlFor="statusFilter" className="form-label">
            状态筛选
          </label>
          <select
            id="statusFilter"
            className="form-select"
            value={statusFilter}
            onChange={(e) => handleStatusFilterChange(e.target.value)}
          >
            <option value="PENDING">待审批</option>
            <option value="APPROVED">已批准</option>
            <option value="DENIED">已拒绝</option>
            <option value="">全部</option>
          </select>
        </div>
      </div>
      <table className="table table-striped">
        <thead>
          <tr>
            <th>学生</th>
            <th>邮箱</th>
            <th>宠物</th>
            <th>申请时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((item) => (
            <tr key={item.id}>
              <td>{item.studentName}</td>
              <td>{item.studentEmail}</td>
              <td>{item.petName}</td>
              <td>{formatDateTime(item.requestedAt)}</td>
              <td>{statusLabel(item.status)}</td>
              <td>
                {item.status === "PENDING" && (
                  <>
                    <button
                      className="btn btn-sm btn-success me-2"
                      onClick={() => handleApprove(item.id)}
                    >
                      Approve
                    </button>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => handleDeny(item.id)}
                    >
                      Deny
                    </button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <PaginationBar
        page={page}
        totalPages={totalPages}
        totalElements={totalElements}
        onPageChange={handlePageChange}
      />
    </div>
  );
};

export default AdoptionApprovalComponent;
