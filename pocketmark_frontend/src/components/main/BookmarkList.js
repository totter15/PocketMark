import React from "react";
import BookmarkListItem from "./BookmarkListItem";
import "./BookmarkList.css";

const BookmarkList = ({
  bookmarkList,
  editModalOpen,
  deleteBookmarks,
  selectFolder,
}) => {
  return (
    <div className="bookmarkList">
      <div className="contents">
        {bookmarkList &&
          bookmarkList.map((bookmark, index) => (
            <BookmarkListItem
              key={`${index}_${bookmark.name}`}
              bookmark={bookmark}
              editModalOpen={editModalOpen}
              deleteBookmarks={deleteBookmarks}
            />
          ))}
      </div>
    </div>
  );
};

export default BookmarkList;
