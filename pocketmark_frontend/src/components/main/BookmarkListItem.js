import React, { useState } from "react";
import { FiEdit3 } from "react-icons/fi";
import { RiDeleteBin6Line } from "react-icons/ri";
import "./BookmarkListItem.css";

const BookmarkListItem = ({ bookmark, editModalOpen, deleteBookmarks }) => {
  return (
    <div className="bookmarkListItem">
      <div className="edit" onClick={() => deleteBookmarks(bookmark.itemId)}>
        <RiDeleteBin6Line
          style={{
            position: "absolute",
            right: 20,
            color: "darkgray",
            width: "18px",
            height: "18px",
          }}
        />
      </div>
      <div className="edit" onClick={() => editModalOpen(bookmark.itemId)}>
        <FiEdit3
          style={{
            position: "absolute",
            right: 50,
            color: "darkgray",
            width: "18px",
            height: "18px",
          }}
        />
      </div>

      <a href={bookmark.url} target={"_blank"}>
        <div className="name">{bookmark.name}</div>
        <div className="comment">{bookmark.comment}</div>

        <div className="url">{bookmark.url}</div>
      </a>

      <div style={{ flexDirection: "row", display: "flex" }}>
        {bookmark.tags &&
          bookmark.tags.map((tag) => (
            <div key={tag.name} style={{ marginRight: 8 }}>
              #{tag.name}
            </div>
          ))}
      </div>
    </div>
  );
};

export default BookmarkListItem;
