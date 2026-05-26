import PaginationBar from "./PaginationBar";
import useListProductOrderHook from "../hooks/useListProductOrderHook";

const ListProductOrderComponent = () => {
  const {
    orders,
    page,
    sort,
    searchInput,
    fromDateInput,
    toDateInput,
    minPriceInput,
    maxPriceInput,
    hasAppliedFilters,
    totalPages,
    totalElements,
    formatDateTime,
    formatMoney,
    handlePageChange,
    handleSortChange,
    handleSearchSubmit,
    handleClearSearch,
    setSearchInput,
    setFromDateInput,
    setToDateInput,
    setMinPriceInput,
    setMaxPriceInput,
  } = useListProductOrderHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">下单记录</h2>
      <form onSubmit={handleSearchSubmit}>
        <div className="row mb-3 g-3 align-items-end">
          <div className="col-md-8">
            <label htmlFor="admin-order-product-search" className="form-label mb-1">
              按商品名称搜索
            </label>
            <input
              id="admin-order-product-search"
              type="text"
              className="form-control"
              placeholder="输入商品名称"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
            />
          </div>
          <div className="col-md-4">
            <label htmlFor="admin-order-sort" className="form-label mb-1">
              排序方式
            </label>
            <select
              id="admin-order-sort"
              className="form-select"
              value={sort}
              onChange={(e) => handleSortChange(e.target.value)}
            >
              <option value="desc">倒序（最新在前）</option>
              <option value="asc">正序（最早在前）</option>
            </select>
          </div>
        </div>
        <div className="row mb-3 g-3 align-items-end">
          <div className="col-md-3">
            <label htmlFor="admin-order-from-date" className="form-label mb-1">
              开始日期
            </label>
            <input
              id="admin-order-from-date"
              type="date"
              className="form-control"
              value={fromDateInput}
              onChange={(e) => setFromDateInput(e.target.value)}
            />
          </div>
          <div className="col-md-3">
            <label htmlFor="admin-order-to-date" className="form-label mb-1">
              结束日期
            </label>
            <input
              id="admin-order-to-date"
              type="date"
              className="form-control"
              value={toDateInput}
              onChange={(e) => setToDateInput(e.target.value)}
            />
          </div>
          <div className="col-md-3">
            <label htmlFor="admin-order-min-price" className="form-label mb-1">
              最低总价（元）
            </label>
            <input
              id="admin-order-min-price"
              type="number"
              min="0"
              step="0.01"
              className="form-control"
              placeholder="0.00"
              value={minPriceInput}
              onChange={(e) => setMinPriceInput(e.target.value)}
            />
          </div>
          <div className="col-md-3">
            <label htmlFor="admin-order-max-price" className="form-label mb-1">
              最高总价（元）
            </label>
            <input
              id="admin-order-max-price"
              type="number"
              min="0"
              step="0.01"
              className="form-control"
              placeholder="0.00"
              value={maxPriceInput}
              onChange={(e) => setMaxPriceInput(e.target.value)}
            />
          </div>
        </div>
        <div className="row mb-3 g-2">
          <div className="col-auto">
            <button type="submit" className="btn btn-outline-primary">
              搜索
            </button>
          </div>
          {hasAppliedFilters && (
            <div className="col-auto">
              <button
                type="button"
                className="btn btn-outline-secondary"
                onClick={handleClearSearch}
              >
                清空
              </button>
            </div>
          )}
        </div>
      </form>
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
