import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { listPets, searchPetsByName } from "../services/PetService";
import {
  adoptPetForMe,
  returnPetForMe,
  getMyProfile,
  getMyPendingAdoptionRequest,
} from "../services/StudentPortalService";

const StudentPetListComponent = () => {
  const [pets, setPets] = useState([]);
  const [profile, setProfile] = useState(null);
  const [pendingRequest, setPendingRequest] = useState(null);
  const [searchName, setSearchName] = useState("");
  const [isSearching, setIsSearching] = useState(false);

  const loadProfile = async () => {
    const response = await getMyProfile();
    setProfile(response.data);
  };

  const loadPendingRequest = async () => {
    const response = await getMyPendingAdoptionRequest();
    if (response.status === 204 || !response.data) {
      setPendingRequest(null);
      return;
    }
    setPendingRequest(response.data);
  };

  const loadPets = async () => {
    const response = await listPets();
    setPets(response.data);
  };

  useEffect(() => {
    loadPets();
    loadProfile();
    loadPendingRequest();
  }, []);

  const searchPets = async (e) => {
    e.preventDefault();
    if (!searchName.trim()) {
      toast.error("请输入宠物名称");
      return;
    }
    const response = await searchPetsByName(searchName.trim());
    setPets(response.data);
    setIsSearching(true);
    if (response.data.length === 0) {
      toast.info("未找到匹配的宠物");
    }
  };

  const resetSearch = () => {
    setSearchName("");
    setIsSearching(false);
    loadPets();
  };

  const hasPendingRequest = pendingRequest?.status === "PENDING";

  const handleApply = async (pet) => {
    if (pet.adopted) {
      toast.error("宠物已经被领养");
      return;
    }
    if (profile?.petId) {
      toast.error("最多只能有一只宠物");
      return;
    }
    if (hasPendingRequest) {
      toast.error("您已有待审批的领养申请");
      return;
    }
    try {
      await adoptPetForMe(pet.id);
      toast.success("申请已提交，等待管理员审批");
      loadPendingRequest();
    } catch (err) {
      toast.error(err.response?.data?.message || "申请失败");
    }
  };

  const handleReturn = async () => {
    if (!profile?.petId) {
      toast.error("该学生没有宠物");
      return;
    }
    try {
      await returnPetForMe();
      toast.success("归还成功!");
      loadPets();
      loadProfile();
    } catch (err) {
      toast.error(err.response?.data?.message || "归还失败");
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">宠物领养</h2>
      {hasPendingRequest && (
        <div className="alert alert-warning">
          审批中：{pendingRequest.petName}
        </div>
      )}
      {profile?.petId && (
        <div className="alert alert-info">
          当前宠物：{profile.petName}
          <button
            className="btn btn-sm btn-outline-warning ms-3"
            onClick={handleReturn}
          >
            Return
          </button>
        </div>
      )}
      <form className="row g-2 align-items-end mb-3" onSubmit={searchPets}>
        <div className="col-md-6">
          <label htmlFor="studentPetSearch" className="form-label">
            按名称查询
          </label>
          <input
            id="studentPetSearch"
            type="text"
            className="form-control"
            placeholder="请输入宠物名称"
            value={searchName}
            onChange={(e) => setSearchName(e.target.value)}
          />
        </div>
        <div className="col-auto">
          <button type="submit" className="btn btn-outline-primary">
            查询
          </button>
        </div>
        {isSearching && (
          <div className="col-auto">
            <button
              type="button"
              className="btn btn-outline-secondary"
              onClick={resetSearch}
            >
              重置
            </button>
          </div>
        )}
      </form>
      <table className="table table-striped">
        <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Age</th>
            <th>Category</th>
            <th>Adopted</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {pets.map((item) => (
            <tr key={item.id}>
              <td>{item.name}</td>
              <td>{item.description}</td>
              <td>{item.age}</td>
              <td>{item.category}</td>
              <td>{item.adopted ? "Yes" : "No"}</td>
              <td>
                <button
                  className="btn btn-outline-success"
                  onClick={() => handleApply(item)}
                  disabled={
                    item.adopted || profile?.petId || hasPendingRequest
                  }
                >
                  申请领养
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default StudentPetListComponent;
