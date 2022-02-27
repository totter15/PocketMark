import React from "react";
import { FiEdit3 } from "react-icons/fi";
import { RiDeleteBin6Line } from "react-icons/ri";
import "./FolderRoute.css";

const FolderRoute = ({
  route,
  folders,
  selectFolderId,
  editFolderModalOpen,
  deleteFolders,
}) => {
  const selectFolder = folders.find((f) => f.itemId === selectFolderId);
  return (
    <div className="route">
      <div>
        내 폴더 {route}
        <div className="folderTags">
          {selectFolder &&
            selectFolder.tags &&
            selectFolder.tags.map((t) => (
              <div key={`${t.name}`} className="tag">
                #{t.name}
              </div>
            ))}
        </div>
      </div>
      {selectFolderId !== 0 && (
        <div className="icon">
          <button onClick={() => editFolderModalOpen(selectFolderId)}>
            <FiEdit3 />
          </button>
          <button onClick={() => deleteFolders(selectFolderId)}>
            <RiDeleteBin6Line />
          </button>
        </div>
      )}
    </div>
  );
};

export default FolderRoute;
