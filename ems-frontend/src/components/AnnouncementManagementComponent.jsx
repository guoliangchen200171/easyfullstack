import useAnnouncementManagementHook from "../hooks/useAnnouncementManagementHook";

const formatDateTime = (value) => {
  if (!value) {
    return "-";
  }
  return new Date(value).toLocaleString();
};

const AnnouncementManagementComponent = () => {
  const {
    announcements,
    form,
    setForm,
    editingId,
    editDraft,
    setEditDraft,
    handleCreate,
    handleDelete,
    startEdit,
    cancelEdit,
    handleSaveEdit,
  } = useAnnouncementManagementHook();

  const activeCount = announcements.filter((item) => item.active).length;

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">Announcement Management</h2>

      <div className="alert alert-info">
        Active announcements: {activeCount}/3
      </div>

      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">Create Announcement</h5>
          <form className="row g-3" onSubmit={handleCreate}>
            <div className="col-md-4">
              <label htmlFor="announcementTitle" className="form-label">
                Title
              </label>
              <input
                id="announcementTitle"
                type="text"
                className="form-control"
                value={form.title}
                onChange={(e) =>
                  setForm((prev) => ({ ...prev, title: e.target.value }))
                }
              />
            </div>
            <div className="col-md-6">
              <label htmlFor="announcementContent" className="form-label">
                Content
              </label>
              <textarea
                id="announcementContent"
                className="form-control"
                rows="2"
                value={form.content}
                onChange={(e) =>
                  setForm((prev) => ({ ...prev, content: e.target.value }))
                }
              />
            </div>
            <div className="col-md-2 d-flex flex-column justify-content-end">
              <div className="form-check mb-2">
                <input
                  id="announcementActive"
                  type="checkbox"
                  className="form-check-input"
                  checked={form.active}
                  onChange={(e) =>
                    setForm((prev) => ({ ...prev, active: e.target.checked }))
                  }
                />
                <label htmlFor="announcementActive" className="form-check-label">
                  Active
                </label>
              </div>
              <button type="submit" className="btn btn-primary">
                Create
              </button>
            </div>
          </form>
        </div>
      </div>

      <table className="table table-striped align-middle">
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Content</th>
            <th>Status</th>
            <th>Updated</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {announcements.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>
                {editingId === item.id ? (
                  <input
                    type="text"
                    className="form-control form-control-sm"
                    value={editDraft.title}
                    onChange={(e) =>
                      setEditDraft((prev) => ({
                        ...prev,
                        title: e.target.value,
                      }))
                    }
                  />
                ) : (
                  item.title
                )}
              </td>
              <td style={{ minWidth: "260px" }}>
                {editingId === item.id ? (
                  <textarea
                    className="form-control form-control-sm"
                    rows="2"
                    value={editDraft.content}
                    onChange={(e) =>
                      setEditDraft((prev) => ({
                        ...prev,
                        content: e.target.value,
                      }))
                    }
                  />
                ) : (
                  item.content
                )}
              </td>
              <td>
                {editingId === item.id ? (
                  <div className="form-check">
                    <input
                      id={`active-${item.id}`}
                      type="checkbox"
                      className="form-check-input"
                      checked={editDraft.active}
                      onChange={(e) =>
                        setEditDraft((prev) => ({
                          ...prev,
                          active: e.target.checked,
                        }))
                      }
                    />
                    <label
                      htmlFor={`active-${item.id}`}
                      className="form-check-label"
                    >
                      Active
                    </label>
                  </div>
                ) : item.active ? (
                  <span className="badge text-bg-success">Active</span>
                ) : (
                  <span className="badge text-bg-secondary">Draft</span>
                )}
              </td>
              <td>{formatDateTime(item.updatedAt)}</td>
              <td>
                {editingId === item.id ? (
                  <>
                    <button
                      type="button"
                      className="btn btn-sm btn-success me-2"
                      onClick={() => handleSaveEdit(item)}
                    >
                      Save
                    </button>
                    <button
                      type="button"
                      className="btn btn-sm btn-outline-secondary me-2"
                      onClick={cancelEdit}
                    >
                      Cancel
                    </button>
                  </>
                ) : (
                  <button
                    type="button"
                    className="btn btn-sm btn-outline-primary me-2"
                    onClick={() => startEdit(item)}
                  >
                    Edit
                  </button>
                )}
                <button
                  type="button"
                  className="btn btn-sm btn-outline-danger"
                  onClick={() => handleDelete(item.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
          {announcements.length === 0 && (
            <tr>
              <td colSpan="6" className="text-center text-muted py-4">
                No announcements yet
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default AnnouncementManagementComponent;
