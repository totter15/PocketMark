import React from "react";
import FolderChildList from "./FolderChildList";
import "./FolderListItem.css";

const FolderListItem = ({ name, child, folderSelect, id, select }) => {
  return (
    <div
      className={child ? "child folderListItem" : "folderListItem"}
      onClick={() => folderSelect(id)}
      style={{
        backgroundColor: select ? "rgb(241, 245, 248)" : "white",
      }}
    >
      {child ? ` - ${name}` : name}
    </div>
  );
};

export default FolderListItem;
