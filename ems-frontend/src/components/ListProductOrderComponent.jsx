import PaginationBar from "./PaginationBar";
import useListProductOrderHook from "../hooks/useListProductOrderHook";

const ListProductOrderComponent = () => {
  const {
    orders,
    page,
    totalPages,
    totalElements,
    formatDateTime,
    formatMoney,
    handlePageChange,
  } = useListProductOrderHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">下单记录</h2>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">订单 ID</th>
            <th scope="col">学生</th>
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
              <td>{item.studentName}</td>
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

export default ListProductOrderComponent;
