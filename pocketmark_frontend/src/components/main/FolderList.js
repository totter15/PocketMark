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
  childFolder,
}) => {
  console.log(childFolder, "child");
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
          console.log(childFolder[0]);
          return (
            <>
              <FolderListItem
                key={folder.folderId}
                id={folder.folderId}
                name={folder.name}
                folderSelect={folderSelect}
                select={folder.folderId === selectFolder}
              />
              {childFolder[0] && childFolder[0].parent === folder.folderId ? (
                <FolderChildList
                  childFolder={childFolder}
                  selectFolder={selectFolder}
                  folderSelect={folderSelect}
                />
              ) : (
                <></>
              )}
              {/* {folders.map((fol) => {
                  if (fol.parent === folder.folderId) {
                    return (
                      <FolderListItem
                        key={fol.folderId}
                        name={fol.name}
                        child
                        folderSelect={folderSelect}
                        id={fol.folderId}
                        select={fol.folderId === selectFolder}
                      />
                    );
                  }
                })} */}
            </>
          );
        })}
    </div>
  );
};

export default FolderList;
