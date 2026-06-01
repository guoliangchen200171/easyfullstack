import PaginationBar from "./PaginationBar";
import useListRestockRecordHook from "../hooks/useListRestockRecordHook";

const ListRestockRecordComponent = () => {
  const {
    records,
    page,
    totalPages,
    totalElements,
    formatDateTime,
    handlePageChange,
  } = useListRestockRecordHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">库存变动记录</h2>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">记录ID</th>
            <th scope="col">商品名称</th>
            <th scope="col">商品ID</th>
            <th scope="col">数量</th>
            <th scope="col">操作类型</th>
            <th scope="col">操作时间</th>
          </tr>
        </thead>
        <tbody>
          {records.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>{item.productName}</td>
              <td>{item.productId}</td>
              <td>{item.quantity}</td>
              <td className={item.operationType === "补货" ? "text-success" : "text-danger"}>
                {item.operationType}
              </td>
              <td>{formatDateTime(item.restockedAt)}</td>
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

export default ListRestockRecordComponent;
