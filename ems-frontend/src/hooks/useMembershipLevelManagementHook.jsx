import { useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";
import {
  createMembershipLevel,
  deleteMembershipLevel,
  listMembershipLevels,
  updateMembershipLevel,
} from "../services/MembershipLevelService";

const emptyForm = () => ({
  levelCode: "",
  levelName: "",
  minPoints: "0",
  description: "",
  sortOrder: "0",
});

const useMembershipLevelManagementHook = () => {
  const [levels, setLevels] = useState([]);
  const [form, setForm] = useState(emptyForm());
  const [editingId, setEditingId] = useState(null);
  const [editDraft, setEditDraft] = useState(null);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState(null);

  const fetchLevels = useCallback(async () => {
    setLoading(true);
    setLoadError(null);
    try {
      const response = await listMembershipLevels();
      setLevels(response.data ?? []);
    } catch (err) {
      const message =
        err.response?.data?.message || "加载会员等级失败，请确认已用 Admin 登录且后端已启动";
      setLoadError(message);
      setLevels([]);
      toast.error(message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchLevels();
  }, [fetchLevels]);

  const resetForm = () => setForm(emptyForm());

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await createMembershipLevel({
        levelCode: form.levelCode.trim().toUpperCase(),
        levelName: form.levelName.trim(),
        minPoints: Number(form.minPoints),
        description: form.description.trim() || null,
        sortOrder: Number(form.sortOrder) || Number(form.minPoints),
      });
      toast.success("会员等级已创建");
      resetForm();
      fetchLevels();
    } catch (err) {
      toast.error(err.response?.data?.message || "创建失败");
    }
  };

  const startEdit = (level) => {
    setEditingId(level.id);
    setEditDraft({
      levelCode: level.levelCode,
      levelName: level.levelName,
      minPoints: String(level.minPoints),
      description: level.description || "",
      sortOrder: String(level.sortOrder ?? level.minPoints),
    });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditDraft(null);
  };

  const handleSaveEdit = async (id) => {
    try {
      await updateMembershipLevel(id, {
        levelCode: editDraft.levelCode.trim().toUpperCase(),
        levelName: editDraft.levelName.trim(),
        minPoints: Number(editDraft.minPoints),
        description: editDraft.description.trim() || null,
        sortOrder: Number(editDraft.sortOrder) || Number(editDraft.minPoints),
      });
      toast.success("会员等级已更新");
      cancelEdit();
      fetchLevels();
    } catch (err) {
      toast.error(err.response?.data?.message || "更新失败");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("确定删除该会员等级？")) {
      return;
    }
    try {
      await deleteMembershipLevel(id);
      toast.success("已删除");
      fetchLevels();
    } catch (err) {
      toast.error(err.response?.data?.message || "删除失败");
    }
  };

  return {
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
    refetch: fetchLevels,
  };
};

export default useMembershipLevelManagementHook;
