import React from "react";
import BookmarkListItem from "./BookmarkListItem";
import "./BookmarkList.css";

const BookmarkList = ({ bookmarkList }) => {
  return (
    <div className="bookmarkList">
      <div className="route">내폴더 / 폴더1</div>
      <div className="contents">
        {bookmarkList &&
          bookmarkList.map((bookmark, index) => (
            <BookmarkListItem
              key={`${index}_${bookmark.name}`}
              bookmark={bookmark}
            />
          ))}
      </div>
    </div>
  );
};

export default BookmarkList;
