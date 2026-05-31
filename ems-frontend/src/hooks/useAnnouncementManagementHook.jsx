import { useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";
import {
  createAnnouncement,
  deleteAnnouncement,
  listAnnouncements,
  updateAnnouncement,
} from "../services/AnnouncementService";

const emptyForm = {
  title: "",
  content: "",
  active: true,
};

const useAnnouncementManagementHook = () => {
  const [announcements, setAnnouncements] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [editDraft, setEditDraft] = useState(emptyForm);

  const fetchAnnouncements = useCallback(async () => {
    try {
      const response = await listAnnouncements();
      setAnnouncements(response.data);
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to load announcements");
    }
  }, []);

  useEffect(() => {
    fetchAnnouncements();
  }, [fetchAnnouncements]);

  const validateDraft = (draft) => {
    if (!draft.title.trim()) {
      toast.error("Announcement title cannot be empty");
      return false;
    }
    if (!draft.content.trim()) {
      toast.error("Announcement content cannot be empty");
      return false;
    }
    return true;
  };

  const toPayload = (draft) => ({
    title: draft.title.trim(),
    content: draft.content.trim(),
    active: draft.active,
  });

  const handleCreate = async (e) => {
    e.preventDefault();
    if (!validateDraft(form)) {
      return;
    }
    try {
      await createAnnouncement(toPayload(form));
      toast.success("Announcement created");
      setForm(emptyForm);
      fetchAnnouncements();
    } catch (err) {
      toast.error(err.response?.data?.message || "Create failed");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this announcement?")) {
      return;
    }
    try {
      await deleteAnnouncement(id);
      toast.success("Announcement deleted");
      if (editingId === id) {
        setEditingId(null);
      }
      fetchAnnouncements();
    } catch (err) {
      toast.error(err.response?.data?.message || "Delete failed");
    }
  };

  const startEdit = (announcement) => {
    setEditingId(announcement.id);
    setEditDraft({
      title: announcement.title || "",
      content: announcement.content || "",
      active: Boolean(announcement.active),
    });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditDraft(emptyForm);
  };

  const handleSaveEdit = async (announcement) => {
    if (!validateDraft(editDraft)) {
      return;
    }
    try {
      await updateAnnouncement(announcement.id, toPayload(editDraft));
      toast.success("Announcement updated");
      setEditingId(null);
      fetchAnnouncements();
    } catch (err) {
      toast.error(err.response?.data?.message || "Update failed");
    }
  };

  return {
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
  };
};

export default useAnnouncementManagementHook;
