import useProductManagementHook from "../hooks/useProductManagementHook";

const ProductManagementComponent = () => {
  const {
    products,
    name,
    setName,
    description,
    setDescription,
    price,
    setPrice,
    stock,
    setStock,
    stockProductId,
    setStockProductId,
    stockQuantity,
    setStockQuantity,
    editingId,
    editDraft,
    setEditDraft,
    handleCreate,
    handleDelete,
    handleAddStock,
    handleDeductStock,
    startEdit,
    cancelEdit,
    handleSaveEdit,
  } = useProductManagementHook();

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">商品管理</h2>

      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">新增商品</h5>
          <form className="row g-3" onSubmit={handleCreate}>
            <div className="col-md-3">
              <label htmlFor="productName" className="form-label">
                名称
              </label>
              <input
                id="productName"
                type="text"
                className="form-control"
                placeholder="商品名称"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </div>
            <div className="col-md-3">
              <label htmlFor="productDescription" className="form-label">
                描述
              </label>
              <input
                id="productDescription"
                type="text"
                className="form-control"
                placeholder="商品描述"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>
            <div className="col-md-2">
              <label htmlFor="productPrice" className="form-label">
                价格（元）
              </label>
              <input
                id="productPrice"
                type="number"
                min="0"
                step="0.01"
                className="form-control"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
              />
            </div>
            <div className="col-md-2">
              <label htmlFor="productStock" className="form-label">
                库存
              </label>
              <input
                id="productStock"
                type="number"
                min="0"
                className="form-control"
                value={stock}
                onChange={(e) => setStock(e.target.value)}
              />
            </div>
            <div className="col-md-2 d-flex align-items-end">
              <button type="submit" className="btn btn-primary w-100">
                新增
              </button>
            </div>
          </form>
        </div>
      </div>

      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">库存操作</h5>
          <div className="row g-3 align-items-end">
            <div className="col-md-3">
              <label htmlFor="stockProductId" className="form-label">
                商品 ID
              </label>
              <input
                id="stockProductId"
                type="number"
                min="1"
                className="form-control"
                placeholder="请输入商品 ID"
                value={stockProductId}
                onChange={(e) => setStockProductId(e.target.value)}
              />
            </div>
            <div className="col-md-3">
              <label htmlFor="stockQuantity" className="form-label">
                数量
              </label>
              <input
                id="stockQuantity"
                type="number"
                min="1"
                className="form-control"
                value={stockQuantity}
                onChange={(e) => setStockQuantity(e.target.value)}
              />
            </div>
            <div className="col-auto">
              <button
                type="button"
                className="btn btn-outline-success"
                onClick={handleAddStock}
              >
                加库存
              </button>
            </div>
            <div className="col-auto">
              <button
                type="button"
                className="btn btn-outline-warning"
                onClick={handleDeductStock}
              >
                扣库存
              </button>
            </div>
          </div>
        </div>
      </div>

      <table className="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>描述</th>
            <th>价格</th>
            <th>库存</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {products.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>
                {editingId === item.id ? (
                  <input
                    type="text"
                    className="form-control form-control-sm"
                    value={editDraft.name}
                    onChange={(e) =>
                      setEditDraft((prev) => ({
                        ...prev,
                        name: e.target.value,
                      }))
                    }
                  />
                ) : (
                  item.name
                )}
              </td>
              <td>
                {editingId === item.id ? (
                  <input
                    type="text"
                    className="form-control form-control-sm"
                    value={editDraft.description}
                    onChange={(e) =>
                      setEditDraft((prev) => ({
                        ...prev,
                        description: e.target.value,
                      }))
                    }
                  />
                ) : (
                  item.description || "—"
                )}
              </td>
              <td>
                {editingId === item.id ? (
                  <input
                    type="number"
                    min="0"
                    step="0.01"
                    className="form-control form-control-sm"
                    style={{ width: "100px" }}
                    value={editDraft.price}
                    onChange={(e) =>
                      setEditDraft((prev) => ({
                        ...prev,
                        price: e.target.value,
                      }))
                    }
                  />
                ) : (
                  `¥${Number(item.price ?? 0).toFixed(2)}`
                )}
              </td>
              <td>{item.stock}</td>
              <td>
                {editingId === item.id ? (
                  <>
                    <button
                      className="btn btn-sm btn-success me-2"
                      onClick={() => handleSaveEdit(item)}
                    >
                      保存信息
                    </button>
                    <button
                      className="btn btn-sm btn-outline-secondary me-2"
                      onClick={cancelEdit}
                    >
                      取消
                    </button>
                  </>
                ) : (
                  <button
                    className="btn btn-sm btn-outline-primary me-2"
                    onClick={() => startEdit(item)}
                  >
                    编辑
                  </button>
                )}
                <button
                  className="btn btn-sm btn-outline-danger"
                  onClick={() => handleDelete(item.id)}
                >
                  删除
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductManagementComponent;
