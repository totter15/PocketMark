import React, { useState } from "react";
import { FiEdit3 } from "react-icons/fi";
import { RiDeleteBin6Line } from "react-icons/ri";
import "./BookmarkListItem.css";

const BookmarkListItem = ({ bookmark, editModalOpen, deleteBookmarks }) => {
  const [modal, setModal] = useState(false);

  return (
    <div className="bookmarkListItem">
      <div
        className="edit"
        onClick={() => deleteBookmarks(bookmark.bookmarkId)}
      >
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
      <div className="edit" onClick={() => editModalOpen(bookmark.bookmarkId)}>
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
        <div>
          <div className="name">{bookmark.name}</div>
          <div className="comment">{bookmark.comment}</div>
        </div>

        <div className="url">{bookmark.url}</div>
      </a>
    </div>
  );
};

export default BookmarkListItem;
