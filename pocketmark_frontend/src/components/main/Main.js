import React, { useState, useRef, useEffect } from "react";
import { Link } from "react-router-dom";
import { getCookis } from "../../lib/cookie";
import "./Main.css";
import { IoAddCircleOutline } from "react-icons/io5";
import produce from "immer";
import AddFolderModal from "./AddFolderModal";
import AddModal from "./AddModal";
import FolderList from "./FolderList";
import BookmarkList from "./BookmarkList";

const Main = () => {
  const [search, setSearch] = useState("");
  const [folders, setFolders] = useState([]);
  const [bookmarks, setBookmarks] = useState(null);
  const [folderModal, setFolderModal] = useState(false);
  const [addModal, setAddModal] = useState(false);
  const [bookmarkList, setBookmarkList] = useState(null);
  const [selectFolder, setSelectFolder] = useState(0);
  const forderId = useRef(1);

  const makeFolder = (folderName, parent, depth) => {
    setFolders([
      ...folders,
      {
        folderId: forderId.current,
        parent: parent,
        depth: depth,
        name: folderName,
      },
    ]);
    forderId.current++;
  };

  const makeBookmarks = (bookmarkName, url, comment, folderId) => {
    if (!bookmarks)
      return setBookmarks([
        {
          name: bookmarkName,
          url: url,
          comment: comment,
          folderId: folderId,
        },
      ]);
    setBookmarks([
      ...bookmarks,
      {
        name: bookmarkName,
        url: url,
        comment: comment,
        folderId: folderId,
      },
    ]);
    modalClose();
  };

  const folderSelect = (folderId) => {
    setSelectFolder(folderId);
    bookmarks &&
      setBookmarkList(
        bookmarks.filter((bookmark) => bookmark.folderId === folderId)
      );
  };

  const folderModalOpen = () => {
    setFolderModal(true);
  };

  const folderModalClose = () => {
    setFolderModal(false);
  };

  const modalClose = () => {
    setAddModal(false);
  };

  const modalOpen = () => {
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
          {/* <section className="foldList boxalign">
            <button onClick={folderModalOpen}>
              <IoAddCircleOutline />
            </button>
            <h3>폴더</h3>
            <ul>
              <li>
                <Link to="">folder name 1</Link>
              </li>
              <li>
                <Link to="">folder name 2</Link>
              </li>
              <li>
                <Link to="">folder name 3</Link>
              </li>
            </ul>
          </section> */}
          {/* 폴더 내부 */}
          <BookmarkList bookmarkList={bookmarkList} />
          {/* <section className="quickAccess boxalign">
            <ul>
              <li>url1</li>
              <li>url2</li>
            </ul>
          </section> */}
        </main>
      </div>
      <AddFolderModal
        folderModalClose={folderModalClose}
        open={folderModal}
        makeFolder={makeFolder}
        folders={folders}
      />
      <AddModal
        modalClose={modalClose}
        open={addModal}
        makeBookmarks={makeBookmarks}
        folders={folders}
      />
    </>
  );
};

export default Main;
