import ButtonLink from "./ButtonLink";
import useListAdoptionHistoryHook from "../hooks/useListAdoptionHistoryHook";

const ListAdoptionHistoryComponent = () => {
  const { history, formatDateTime } = useListAdoptionHistoryHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">Adoption History</h2>
      <ButtonLink text="Back to Students" toAction="/students" />
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">Student</th>
            <th scope="col">Pet</th>
            <th scope="col">Category</th>
            <th scope="col">Adopted At</th>
            <th scope="col">Returned At</th>
            <th scope="col">Status</th>
          </tr>
        </thead>
        <tbody>
          {history.map((item) => (
            <tr key={item.id}>
              <td>{item.studentName}</td>
              <td>{item.petName}</td>
              <td>{item.category}</td>
              <td>{formatDateTime(item.adoptedAt)}</td>
              <td>{formatDateTime(item.returnedAt)}</td>
              <td>{item.returnedAt ? "已归还" : "领养中"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ListAdoptionHistoryComponent;
