import PaginationBar from "./PaginationBar";
import useListAnnouncementHistoryHook from "../hooks/useListAnnouncementHistoryHook";

const operationTypeClass = (type) => {
  if (type === "发布") return "text-success";
  if (type === "更改") return "text-primary";
  if (type === "删除") return "text-danger";
  return "";
};

const ListAnnouncementHistoryComponent = () => {
  const {
    records,
    page,
    totalPages,
    totalElements,
    formatDateTime,
    handlePageChange,
  } = useListAnnouncementHistoryHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">公告操作历史</h2>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">记录ID</th>
            <th scope="col">公告ID</th>
            <th scope="col">公告标题</th>
            <th scope="col">操作类型</th>
            <th scope="col">操作时间</th>
          </tr>
        </thead>
        <tbody>
          {records.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>{item.announcementId}</td>
              <td>{item.title}</td>
              <td className={operationTypeClass(item.operationType)}>
                {item.operationType}
              </td>
              <td>{formatDateTime(item.operatedAt)}</td>
            </tr>
          ))}
          {records.length === 0 && (
            <tr>
              <td colSpan="5" className="text-center text-muted py-4">
                暂无记录
              </td>
            </tr>
          )}
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

export default ListAnnouncementHistoryComponent;
