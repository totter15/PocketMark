import React from "react";
import { connect } from "react-redux";

const MainContainer = ({
  getData,
  putData,
  deleteData,
  postData,
  folders,
  bookmarks,
}) => {
  return (
    <Main
      folders={folders}
      bookmarks={bookmarks}
      getData={getData}
      putData={putData}
      deleteData={deleteData}
      postData={postData}
    />
  );
};

export default connect(
  ({ data }) => ({
    folders: data.folders,
    bookmarks: data.bookmarks,
  }),
  { getData, putData, deleteData, postData }
)(MainContainer);
