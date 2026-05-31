import { useEffect, useState } from "react";
import { listActiveAnnouncements } from "../services/AnnouncementService";

const StudentAnnouncements = () => {
  const [announcements, setAnnouncements] = useState([]);

  useEffect(() => {
    listActiveAnnouncements()
      .then((response) => setAnnouncements(response.data))
      .catch(() => setAnnouncements([]));
  }, []);

  if (announcements.length === 0) {
    return null;
  }

  return (
    <div className="container mt-3">
      {announcements.map((announcement) => (
        <div key={announcement.id} className="alert alert-warning mb-2">
          <strong>{announcement.title}</strong>
          <div>{announcement.content}</div>
        </div>
      ))}
    </div>
  );
};

export default StudentAnnouncements;
