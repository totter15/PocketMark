import React from "react";
import BookmarkListItem from "./BookmarkListItem";
import "./BookmarkList.css";

const BookmarkList = ({ bookmarks, editModalOpen, deleteBookmarks }) => {
  return (
    <div className="bookmarkList">
      <div className="contents">
        {bookmarks &&
          bookmarks.map((bookmark, index) => (
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
