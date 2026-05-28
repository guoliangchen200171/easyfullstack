import useMembershipLevelManagementHook from "../hooks/useMembershipLevelManagementHook";

const MembershipLevelManagementComponent = () => {
  const {
    levels,
    form,
    setForm,
    editingId,
    editDraft,
    setEditDraft,
    loading,
    loadError,
    handleCreate,
    startEdit,
    cancelEdit,
    handleSaveEdit,
    handleDelete,
    refetch,
  } = useMembershipLevelManagementHook();

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">会员等级管理</h2>
      <p className="text-muted text-center mb-4">
        按最低积分门槛划分等级；修改后将自动重算所有会员等级
      </p>

      {loadError && (
        <div className="alert alert-danger" role="alert">
          {loadError}
          <button
            type="button"
            className="btn btn-sm btn-outline-danger ms-3"
            onClick={refetch}
          >
            重试
          </button>
        </div>
      )}

      {!loadError && !loading && levels.length === 0 && (
        <div className="alert alert-warning" role="alert">
          暂无等级配置。请确认 membership-service 已启动；重启后会自动初始化
          BRONZE / SILVER / GOLD，或在此新增等级。
        </div>
      )}

      {loading && (
        <p className="text-center text-muted mb-3">加载等级规则中...</p>
      )}

      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">新增等级</h5>
          <form className="row g-3" onSubmit={handleCreate}>
            <div className="col-md-2">
              <label className="form-label">等级代码</label>
              <input
                type="text"
                className="form-control"
                placeholder="如 GOLD"
                value={form.levelCode}
                onChange={(e) =>
                  setForm({ ...form, levelCode: e.target.value })
                }
                required
              />
            </div>
            <div className="col-md-2">
              <label className="form-label">等级名称</label>
              <input
                type="text"
                className="form-control"
                placeholder="如 金牌会员"
                value={form.levelName}
                onChange={(e) =>
                  setForm({ ...form, levelName: e.target.value })
                }
                required
              />
            </div>
            <div className="col-md-2">
              <label className="form-label">最低积分</label>
              <input
                type="number"
                min="0"
                className="form-control"
                value={form.minPoints}
                onChange={(e) =>
                  setForm({ ...form, minPoints: e.target.value })
                }
                required
              />
            </div>
            <div className="col-md-2">
              <label className="form-label">排序</label>
              <input
                type="number"
                min="0"
                className="form-control"
                value={form.sortOrder}
                onChange={(e) =>
                  setForm({ ...form, sortOrder: e.target.value })
                }
              />
            </div>
            <div className="col-md-2">
              <label className="form-label">备注</label>
              <input
                type="text"
                className="form-control"
                value={form.description}
                onChange={(e) =>
                  setForm({ ...form, description: e.target.value })
                }
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

      <div className="card">
        <div className="card-body">
          <h5 className="card-title">等级列表</h5>
          <table className="table table-bordered table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>代码</th>
                <th>名称</th>
                <th>最低积分</th>
                <th>排序</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              {levels.map((level) =>
                editingId === level.id ? (
                  <tr key={level.id}>
                    <td>{level.id}</td>
                    <td>
                      <input
                        className="form-control form-control-sm"
                        value={editDraft.levelCode}
                        onChange={(e) =>
                          setEditDraft({
                            ...editDraft,
                            levelCode: e.target.value,
                          })
                        }
                      />
                    </td>
                    <td>
                      <input
                        className="form-control form-control-sm"
                        value={editDraft.levelName}
                        onChange={(e) =>
                          setEditDraft({
                            ...editDraft,
                            levelName: e.target.value,
                          })
                        }
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        min="0"
                        className="form-control form-control-sm"
                        value={editDraft.minPoints}
                        onChange={(e) =>
                          setEditDraft({
                            ...editDraft,
                            minPoints: e.target.value,
                          })
                        }
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        min="0"
                        className="form-control form-control-sm"
                        value={editDraft.sortOrder}
                        onChange={(e) =>
                          setEditDraft({
                            ...editDraft,
                            sortOrder: e.target.value,
                          })
                        }
                      />
                    </td>
                    <td>
                      <input
                        className="form-control form-control-sm"
                        value={editDraft.description}
                        onChange={(e) =>
                          setEditDraft({
                            ...editDraft,
                            description: e.target.value,
                          })
                        }
                      />
                    </td>
                    <td>
                      <button
                        type="button"
                        className="btn btn-success btn-sm me-2"
                        onClick={() => handleSaveEdit(level.id)}
                      >
                        保存
                      </button>
                      <button
                        type="button"
                        className="btn btn-secondary btn-sm"
                        onClick={cancelEdit}
                      >
                        取消
                      </button>
                    </td>
                  </tr>
                ) : (
                  <tr key={level.id}>
                    <td>{level.id}</td>
                    <td>{level.levelCode}</td>
                    <td>{level.levelName}</td>
                    <td>{level.minPoints}</td>
                    <td>{level.sortOrder}</td>
                    <td>{level.description || "-"}</td>
                    <td>
                      <button
                        type="button"
                        className="btn btn-warning btn-sm me-2"
                        onClick={() => startEdit(level)}
                      >
                        编辑
                      </button>
                      <button
                        type="button"
                        className="btn btn-danger btn-sm"
                        onClick={() => handleDelete(level.id)}
                      >
                        删除
                      </button>
                    </td>
                  </tr>
                )
              )}
              {levels.length === 0 && (
                <tr>
                  <td colSpan="7" className="text-center text-muted">
                    暂无等级配置
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default MembershipLevelManagementComponent;
