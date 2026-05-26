import useStudentProductPurchaseHook from "../hooks/useStudentProductPurchaseHook";

const StudentProductPurchaseComponent = () => {
  const {
    products,
    deposit,
    membershipPoints,
    quantities,
    setQuantity,
    handlePurchase,
    formatPrice,
    formatDeposit,
    isProductPriced,
  } = useStudentProductPurchaseHook();

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">商品购买</h2>
      <div className="alert alert-info">
        当前存款：{formatDeposit(deposit)} 元 · 会员积分：{membershipPoints ?? 0}
      </div>
      <table className="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>描述</th>
            <th>价格（元）</th>
            <th>库存</th>
            <th>购买数量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => {
            const quantity = quantities[product.id] ?? 0;
            const priced = isProductPriced(product);
            const canPurchase = quantity > 0 && product.stock > 0 && priced;
            return (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>{product.description || "-"}</td>
                <td>
                  {priced ? formatPrice(product.price) : "未定价"}
                </td>
                <td>{product.stock}</td>
                <td>
                  <input
                    type="number"
                    min="0"
                    className="form-control"
                    style={{ width: "100px" }}
                    value={quantity}
                    onChange={(e) => setQuantity(product.id, e.target.value)}
                  />
                </td>
                <td>
                  <button
                    type="button"
                    className="btn btn-outline-success"
                    disabled={!canPurchase}
                    onClick={() => handlePurchase(product)}
                  >
                    购买
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default StudentProductPurchaseComponent;
