import React from "react";
import FolderListItem from "./FolderListItem";

const FolderChildList = ({ childFolder, selectFolder, folderSelect }) => {
  return childFolder.map((folder) => (
    <FolderListItem
      key={folder.folderId}
      id={folder.folderId}
      name={folder.name}
      folderSelect={folderSelect}
      select={folder.folderId === selectFolder}
      child
    />
  ));
};

export default FolderChildList;
