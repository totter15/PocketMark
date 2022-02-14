import React from "react";
import FolderListItem from "./FolderListItem";
import { IoAddCircleOutline } from "react-icons/io5";
import FolderChildList from "./FolderChildList";
import { BsFillFolderFill } from "react-icons/bs";
import { AiOutlinePlus } from "react-icons/ai";
import "./FolderList.css";

const FolderList = ({
  folders,
  folderModalOpen,
  folderSelect,
  selectFolder,
}) => {
  return (
    <div className="folderList">
      <div
        className={selectFolder === 0 ? "addFolder select" : "addFolder"}
        onClick={() => folderSelect(0)}
      >
        <div>
          <BsFillFolderFill />내 폴더
        </div>
        <button onClick={folderModalOpen}>
          <AiOutlinePlus />
        </button>
      </div>

      {folders.length > 0 &&
        folders.map((folder) => {
          if (folder.parentId === 0)
            return (
              <div key={folder.itemId}>
                <FolderListItem
                  id={folder.itemId}
                  name={folder.name}
                  folderSelect={folderSelect}
                  select={folder.itemId === selectFolder}
                />
                <FolderChildList
                  childFolder={folders.filter(
                    (f) => f.parentId === folder.itemId
                  )}
                  selectFolder={selectFolder}
                  folderSelect={folderSelect}
                  select={folder.folderId === selectFolder}
                />
              </div>
            );
        })}
    </div>
  );
};

export default FolderList;
