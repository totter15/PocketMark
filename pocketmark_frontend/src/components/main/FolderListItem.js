import React from "react";
import FolderChildList from "./FolderChildList";
import "./FolderListItem.css";

const FolderListItem = ({ name, child, folderSelect, id, select }) => {
  return (
    <div
      className={select ? "select folderListItem" : "folderListItem"}
      onClick={() => folderSelect(id)}
    >
      {child ? ` - ${name}` : name}
    </div>
  );
};

export default FolderListItem;
