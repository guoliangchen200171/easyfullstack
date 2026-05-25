import PaginationBar from "./PaginationBar";
import useStudentProductOrderHistoryHook from "../hooks/useStudentProductOrderHistoryHook";

const StudentProductOrderHistoryComponent = () => {
  const {
    orders,
    page,
    sort,
    totalPages,
    totalElements,
    formatDateTime,
    formatMoney,
    handlePageChange,
    handleSortChange,
  } = useStudentProductOrderHistoryHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">我的购买记录</h2>
      <div className="row mb-3">
        <div className="col-md-4 offset-md-8">
          <label htmlFor="order-sort" className="form-label mb-1">
            排序方式
          </label>
          <select
            id="order-sort"
            className="form-select"
            value={sort}
            onChange={(e) => handleSortChange(e.target.value)}
          >
            <option value="desc">倒序（最新在前）</option>
            <option value="asc">正序（最早在前）</option>
          </select>
        </div>
      </div>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">订单 ID</th>
            <th scope="col">商品</th>
            <th scope="col">数量</th>
            <th scope="col">单价（元）</th>
            <th scope="col">总价（元）</th>
            <th scope="col">下单时间</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((item) => (
            <tr key={item.orderId}>
              <td>{item.orderId}</td>
              <td>{item.productName}</td>
              <td>{item.quantity}</td>
              <td>{formatMoney(item.unitPrice)}</td>
              <td>{formatMoney(item.totalPrice)}</td>
              <td>{formatDateTime(item.orderedAt)}</td>
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

export default StudentProductOrderHistoryComponent;
