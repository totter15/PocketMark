import React from "react";
import FolderListItem from "./FolderListItem";
import { IoAddCircleOutline } from "react-icons/io5";
import FolderChildList from "./FolderChildList";
import "./FolderList.css";

const FolderList = ({
  folders,
  folderModalOpen,
  folderSelect,
  selectFolder,
}) => {
  console.log(folders);
  return (
    <div className="folderList">
      <div className="addFolder">
        내 폴더
        <button onClick={folderModalOpen}>
          <IoAddCircleOutline />
        </button>
      </div>

      {folders.length > 0 &&
        folders.map((folder) => {
          if (folder.parent === 0) {
            return (
              <>
                <FolderListItem
                  name={folder.name}
                  folderSelect={folderSelect}
                  id={folder.folderId}
                  select={folder.folderId === selectFolder}
                />
                {folders.map((fol) => {
                  if (fol.parent === folder.folderId) {
                    return (
                      <FolderListItem
                        name={fol.name}
                        child
                        folderSelect={folderSelect}
                        id={fol.folderId}
                        select={fol.folderId === selectFolder}
                      />
                    );
                  }
                })}
              </>
            );
          }
        })}
    </div>
  );
};

export default FolderList;
