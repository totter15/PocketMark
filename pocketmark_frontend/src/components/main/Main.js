import React, { useState, useRef, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import { IoAddCircleOutline } from "react-icons/io5";
import AddFolderModal from "./AddFolderModal";
import AddModal from "./AddModal";
import FolderList from "./FolderList";
import BookmarkList from "./BookmarkList";
import { FiEdit3 } from "react-icons/fi";
import { RiDeleteBin6Line } from "react-icons/ri";
import "./Main.css";
import { DeleteData, PostData, PutData, GetData } from "../../lib/Axios";

const Main = () => {
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState("");

  //folders/bookmarks
  const [folders, setFolders] = useState([
    {
      folderId: 0,
      depth: 0,
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
  const [editFolder, setEditFolder] = useState(null);
  const [edit, setEdit] = useState(null);
  const folderId = useRef(1);
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

  const { post, put, del } = server.current;

  useEffect(() => {
    GetData(0).then((res) => {
      setFolders(res.data.data.folders);
      setBookmarks(res.data.data.bookmarks);
      getId(res.data.data);
    });
  }, []);

  useEffect(() => {
    setInterval(() => axios(server.current), 1000 * 60 * 5); //5분
  }, [server]);

  useEffect(() => {
    folderSelect(selectFolder);
  }, [selectFolder, bookmarks]);

  const axios = useCallback((server) => {
    const { post, put, del } = server;
    PostData(post)
      .then(() => {
        if (put.folders.length > 0 || put.bookmarks.length > 0) PutData(put);
      })
      .then(() => {
        if (del.folderIdList.length > 0 || del.bookmarkIdList.length > 0)
          DeleteData(del);
      })
      .then(() => GetData(0))
      .then((res) => {
        setFolders(res.data.data.folders);
        setBookmarks(res.data.data.bookmarks);
        getId(res.data.data);
      })
      .then(() => {
        server.post = {
          folders: [],
          bookmarks: [],
        };
        server.put = {
          folders: [],
          bookmarks: [],
        };
        server.del = {
          folderIdList: [],
          bookmarkIdList: [],
        };
      })
      .catch((e) => console.log(e));
  }, []);

  const makeFolder = useCallback(
    (folderName, parent) => {
      setFolders([
        ...folders,
        {
          folderId: folderId.current,
          parent: parent,
          name: folderName,
        },
      ]);
      //서버용
      post.folders = [
        ...post.folders,
        {
          folderId: folderId.current,
          parent: parent,
          name: folderName,
        },
      ];
      folderId.current++;
      folderModalClose();
    },
    [folders, post]
  );

  const editFolders = useCallback((folderName, parent) => {
    setFolders(
      folders.map((folder) =>
        folder.folderId === edit
          ? {
              name: folderName,
              parent: parent,
              folderId: edit,
            }
          : folder
      )
    );
    //서버용
    put.folders = [
      ...put.folders,
      {
        folderId: edit,
        parent: parent,
        name: folderName,
        visitCount: 0,
      },
    ];
  });

  const makeBookmarks = useCallback(
    (bookmarkName, url, comment, folderId) => {
      setBookmarks([
        ...bookmarks,
        {
          name: bookmarkName,
          url: url,
          comment: comment,
          folderId: folderId,
          id: bookmarkId.current,
        },
      ]);
      //서버용
      post.bookmarks = [
        ...post.bookmarks,
        {
          bookmarkId: bookmarkId.current,
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
          bookmarkId: edit,
          name: bookmarkName,
          url: url,
          comment: comment,
          folderId: "",
          visitCount: 0,
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

  //선택한 폴더의 북마크 가져오기
  const getSelectFolder = useCallback((folderId) => {
    GetData(folderId).then((res) => {
      setBookmarks([...bookmarks, ...res.data.data.bookmarks]);
    });
  }, []);

  //북마크 리스트에 선택한 폴더의 북마크 추가
  useEffect(() => {
    getSelectFolder(selectFolder);
  }, [selectFolder]);

  const folderSelect = useCallback(
    (folderId) => {
      setSelectFolder(folderId);
      setBookmarkList(
        bookmarks.filter((bookmark) => bookmark.folderId === folderId)
      );
    },
    [bookmarks]
  );

  //id값 구하기
  const getId = ({ folders, bookmarks }) => {
    let lastFolderId =
      folders.length > 0 && folders[folders.length - 1].folderId;
    let lastBookmarkId =
      bookmarks.length > 0 && bookmarks[bookmarks.length - 1].bookmarkId;
    folderId.current = lastFolderId ? ++lastFolderId : 1;
    bookmarkId.current = lastBookmarkId ? ++lastBookmarkId : 1;
    console.log(bookmarkId, folderId, "id");
  };

  window.addEventListener("beforeunload", axios); //브라우저 종료전 서버통신

  // 폴더 모달 열기
  const folderModalOpen = () => {
    setFolderModal(true);
  };

  //폴더 수정 모달 열기
  const editFolderModalOpen = (folderId) => {
    setEdit(folderId);
    setEditFolder(folders.filter((f) => f.folderId === folderId));
    setFolderModal(true);
  };

  // 폴더 모달 닫기
  const folderModalClose = () => {
    setFolderModal(false);
  };

  //북마크 모달 열기
  const modalOpen = () => {
    setAddModal(true);
  };

  // 북마크 수정 모달 열기
  const editModalOpen = (bookmarkId) => {
    setEdit(bookmarkId);
    setEditBookmark(bookmarks.filter((b) => b.bookmarkId === bookmarkId));
    setAddModal(true);
  };

  // 북마크 모달 닫기
  const modalClose = () => {
    setAddModal(false);
    setEdit(null);
    setEditBookmark(null);
  };

  const getRoute = (folderId) => {
    const selectFolderData = folders.find(
      (folder) => folder.folderId === folderId
    );
    if (folderId === 0) return;
    else
      return selectFolderData.parent == 0
        ? ` / ${selectFolderData.name}`
        : ` / 
        ${
          folders.find((folder) => folder.folderId === selectFolderData.parent)
            .name
        } 
          / ${selectFolderData.name}`;
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

          <section className="bookmark">
            <div className="route">
              내 폴더 {getRoute(selectFolder)}
              <div>
                <button onClick={() => editFolderModalOpen(selectFolder)}>
                  <FiEdit3
                    style={{
                      color: "darkgray",
                      width: "18px",
                      height: "18px",
                    }}
                  />
                </button>
                <button>
                  <RiDeleteBin6Line
                    style={{
                      color: "darkgray",
                      width: "18px",
                      height: "18px",
                    }}
                  />
                </button>
              </div>
            </div>
            {/* 북마크 리스트 */}
            <BookmarkList
              bookmarks={bookmarks}
              bookmarkList={bookmarkList}
              editModalOpen={editModalOpen}
              deleteBookmarks={deleteBookmarks}
              selectFolder={selectFolder}
            />
          </section>
        </main>
      </div>
      {/* 폴더 추가/수정 */}
      <AddFolderModal
        folderModalClose={folderModalClose}
        open={folderModal}
        makeFolder={makeFolder}
        folders={folders}
        editFolder={editFolder}
        edit={edit}
        editFolders={editFolders}
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
