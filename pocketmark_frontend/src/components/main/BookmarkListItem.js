import React from "react";
import { FiEdit3 } from "react-icons/fi";
import "./BookmarkListItem.css";

const BookmarkListItem = ({ bookmark }) => {
  return (
    <div className="bookmarkListItem">
      <div className="edit" onClick={() => console.log(bookmark)}>
        <FiEdit3
          style={{
            position: "absolute",
            right: 20,
            color: "darkgray",
            width: "20px",
            height: "20px",
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
