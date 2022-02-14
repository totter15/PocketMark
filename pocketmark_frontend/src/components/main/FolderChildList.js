import React from "react";
import FolderListItem from "./FolderListItem";

const FolderChildList = ({ childFolder, selectFolder, folderSelect }) => {
  return childFolder.map((folder) => (
    <FolderListItem
      key={folder.itemId}
      id={folder.itemId}
      name={folder.name}
      folderSelect={folderSelect}
      select={folder.itemId === selectFolder}
      child
    />
  ));
};

export default FolderChildList;
