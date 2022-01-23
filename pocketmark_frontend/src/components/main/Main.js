import React, { useState, useRef, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import { IoAddCircleOutline } from "react-icons/io5";
import AddFolderModal from "./AddFolderModal";
import AddModal from "./AddModal";
import FolderList from "./FolderList";
import BookmarkList from "./BookmarkList";
import "./Main.css";
import { DeleteData, PostData, PutData, GetData } from "../../lib/Axios";
import { getCookis } from "../../lib/cookie";

const Main = () => {
  const [search, setSearch] = useState("");

  //folders/bookmarks
  const [folders, setFolders] = useState([
    {
      folderId: 0,
      name: "내 폴더",
    },
  ]);
  const [bookmarks, setBookmarks] = useState([]);

  //modal
  const [folderModal, setFolderModal] = useState(false);
  const [addModal, setAddModal] = useState(false);

  const [bookmarkList, setBookmarkList] = useState(null);
  const [selectFolder, setSelectFolder] = useState(0);
  const [editBookmark, setEditBookmark] = useState(null);
  const [edit, setEdit] = useState(null);
  const forderId = useRef(1);
  const bookmarkId = useRef(1);
  const server = useRef({
    post: {
      folders: [],
      bookmarks: [],
    },
    put: {
      folders: [],
      bookmarks: [],
    },
    del: {
      folderIdList: [],
      bookmarkIdList: [],
    },
  });

  console.log(folders);

  useEffect(() => {
    // GetData().then((res) => {
    //   setFolders(res.data.folders);
    //   setBookmarks(res.data.bookmarks);
    // });
  }, []);

  useEffect(() => {
    // setInterval(axios, 1000*60*5); //5분
  }, [server]);

  useEffect(() => {
    folderSelect(selectFolder);
  }, [selectFolder, bookmarks]);

  const { post, put, del } = server.current;

  const axios = () => {
    PostData(post)
      .then(() => PutData(put))
      .then(() => DeleteData(del))
      .then(() => GetData())
      .then((res) => {
        setFolders(res.data.folders);
        setBookmarks(res.data.bookmarks);
      })
      .catch((e) => console.log(e));
  };

  const makeFolder = useCallback(
    (folderName, parent, depth) => {
      setFolders([
        ...folders,
        {
          folderId: forderId.current,
          parent: parent,
          depth: depth,
          name: folderName,
        },
      ]);
      //서버용
      post.folders = [
        ...post.folders,
        {
          folderId: forderId.current,
          parent: parent,
          depth: depth,
          name: folderName,
        },
      ];
      forderId.current++;
      folderModalClose();
    },
    [folders, post]
  );

  const makeBookmarks = useCallback(
    (bookmarkName, url, comment, folderId) => {
      setBookmarks([
        ...bookmarks,
        {
          name: bookmarkName,
          url: url,
          comment: comment,
          folderId: folderId,
          bookmarkId: bookmarkId.current,
        },
      ]);
      //서버용
      post.bookmarks = [
        ...post.bookmarks,
        {
          name: bookmarkName,
          url: url,
          comment: comment,
          folderId: folderId,
        },
      ];
      bookmarkId.current++;
      modalClose();
    },
    [bookmarks, post]
  );

  const editBookmarks = useCallback(
    (bookmarkName, url, comment, folderId) => {
      setBookmarks(
        bookmarks.map((bookmark) =>
          bookmark.bookmarkId === edit
            ? {
                name: bookmarkName,
                url: url,
                comment: comment,
                folderId: folderId,
                bookmarkId: edit,
              }
            : bookmark
        )
      );
      //서버용
      put.bookmarks = [
        ...put.bookmarks,
        {
          name: bookmarkName,
          url: url,
          comment: comment,
          folderId: folderId,
          bookmarkId: edit,
        },
      ];
      modalClose();
    },
    [bookmarks, put, edit]
  );

  const deleteBookmarks = useCallback(
    (bookmarkId) => {
      setBookmarks(
        bookmarks.filter((bookmark) => bookmark.bookmarkId !== bookmarkId)
      );
      //서버용
      del.bookmarkIdList = [...del.bookmarkIdList, bookmarkId];
    },
    [bookmarks, del]
  );

  const folderSelect = useCallback(
    (folderId) => {
      setSelectFolder(folderId);
      bookmarks &&
        setBookmarkList(
          bookmarks.filter((bookmark) => bookmark.folderId === folderId)
        );
    },
    [bookmarks]
  );

  const folderModalOpen = () => {
    setFolderModal(true);
  };

  const folderModalClose = () => {
    setFolderModal(false);
  };

  const modalClose = () => {
    setAddModal(false);
    setEdit(null);
    setEditBookmark(null);
  };

  const modalOpen = () => {
    setAddModal(true);
  };

  const editModalOpen = (bookmarkId) => {
    setEdit(bookmarkId);
    setEditBookmark(bookmarks.filter((b) => b.bookmarkId === bookmarkId));
    setAddModal(true);
  };

  return (
    <>
      <div className="main">
        <header>
          <div className="search">
            <h2>PocketMark</h2>
            <form>
              <div className="searchButton">
                <button>검색</button>
              </div>
              <input
                type={"search"}
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </form>
            <button onClick={modalOpen}>
              <IoAddCircleOutline />
            </button>
          </div>

          <div className="nav">
            <Link to="/">My</Link>
            <Link to="/">Logout</Link>
          </div>
        </header>

        <main>
          {/* 폴더 네비게이션 */}
          <FolderList
            folders={folders}
            folderModalOpen={folderModalOpen}
            folderSelect={folderSelect}
            selectFolder={selectFolder}
          />

          {/* 폴더 내부 */}
          <BookmarkList
            bookmarks={bookmarks}
            bookmarkList={bookmarkList}
            editModalOpen={editModalOpen}
            deleteBookmarks={deleteBookmarks}
          />
        </main>
      </div>
      {/* 폴더 추가/수정 */}
      <AddFolderModal
        folderModalClose={folderModalClose}
        open={folderModal}
        makeFolder={makeFolder}
        folders={folders}
      />
      {/* 북마크 추가/수정 */}
      <AddModal
        modalClose={modalClose}
        open={addModal}
        makeBookmarks={makeBookmarks}
        folders={folders}
        editBookmarks={editBookmarks}
        edit={edit}
        editBookmark={editBookmark}
      />
    </>
  );
};

export default Main;
