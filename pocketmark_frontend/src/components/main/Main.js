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
import {
  DeleteData,
  PostData,
  PutData,
  GetData,
  GetAllFolders,
} from "../../lib/Axios";
import { getCookis, setCookie } from "../../lib/cookie";

const Main = () => {
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState("");

  //folders/bookmarks
  const [folders, setFolders] = useState([]); //전체 폴더
  const [selectFolder, setSelectFolder] = useState(0); //선택 폴더 아이디
  const [childFolder, setChildFolder] = useState([]); //선택된 폴더의 자식폴더
  const [bookmarks, setBookmarks] = useState([]); //선택된 폴더의 북마크

  //modal
  const [folderModal, setFolderModal] = useState(false); //폴더 모달
  const [addModal, setAddModal] = useState(false); //북마크 모달

  const [bookmarkList, setBookmarkList] = useState(null);
  const [editBookmark, setEditBookmark] = useState(null);
  const [editFolder, setEditFolder] = useState(null);
  const [edit, setEdit] = useState(null);

  const itemId = useRef(Number(getCookis("lastId")) + 1);
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
      setBookmarks(res.data.data.bookmarks);
      console.log("dd", selectFolder);
    });
    GetAllFolders().then((res) => {
      setFolders(res.data.data.folders.slice(1));
    });
  }, []);

  useEffect(() => {
    setInterval(() => axios(server.current), 1000 * 10); //5분
  }, [server]);

  useEffect(() => {
    folderSelect(selectFolder);
    getRoute(selectFolder);
  }, [selectFolder]);

  const axios = useCallback(
    (server) => {
      const { post, put, del } = server;
      PostData(post)
        .then(() => {
          if (put.folders.length > 0 || put.bookmarks.length > 0) PutData(put);
        })
        .then(() => {
          if (del.folderIdList.length > 0 || del.bookmarkIdList.length > 0)
            DeleteData(del);
        })
        .then(() => GetData(selectFolder))
        .then((res) => {
          console.log(res.data.data, "bookmarks", selectFolder);
          setBookmarks(res.data.data.bookmarks);
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
        .then(() =>
          GetAllFolders().then((res) => {
            setFolders(res.data.data.folders.slice(1));
          })
        )
        .then(() => setCookie("lastId", itemId.current - 1)) //다음 id상태라
        .catch((e) => console.log(e));
    },
    [selectFolder]
  );

  const makeFolder = useCallback(
    (folderName, parent) => {
      setFolders([
        ...folders,
        {
          itemId: itemId.current,
          parentId: parent,
          name: folderName,
          visitCount: 0,
          tags: null,
        },
      ]);
      //서버용
      post.folders = [
        ...post.folders,
        {
          itemId: itemId.current,
          parentId: parent,
          name: folderName,
        },
      ];
      itemId.current++;
      folderModalClose();
    },
    [folders, post]
  );

  const editFolders = useCallback((folderName, parent) => {
    setFolders(
      folders.map((folder) =>
        folder.itemId === edit
          ? {
              name: folderName,
              parentId: parent,
              itemId: edit,
              visitCount: 0,
              tags: null,
            }
          : folder
      )
    );
    //서버용
    put.folders = [
      ...put.folders,
      {
        name: folderName,
        parentId: parent,
        itemId: edit,
        visitCount: 0,
      },
    ];
  });

  const makeBookmarks = useCallback(
    (bookmarkName, url, comment, folderId) => {
      setBookmarks([
        ...bookmarks,
        {
          itemId: itemId.current,
          name: bookmarkName,
          url: url,
          comment: comment,
          parentId: folderId,
          tags: null,
          visitCount: 0,
        },
      ]);
      //서버용
      post.bookmarks = [
        ...post.bookmarks,
        {
          itemId: itemId.current,
          name: bookmarkName,
          url: url,
          comment: comment,
          parentId: folderId,
        },
      ];
      ++itemId.current;
      modalClose();
    },
    [bookmarks, post]
  );

  const editBookmarks = useCallback(
    (bookmarkName, url, comment, parentId) => {
      setBookmarks(
        bookmarks.map((bookmark) =>
          bookmark.itemId === edit
            ? {
                itemId: edit,
                parentId: parentId,
                name: bookmarkName,
                url: url,
                comment: comment,
              }
            : bookmark
        )
      );
      //서버용
      put.bookmarks = [
        ...put.bookmarks,
        {
          itemId: edit,
          parentId: parentId,
          name: bookmarkName,
          url: url,
          comment: comment,
          visitCount: 0,
        },
      ];
      modalClose();
    },
    [bookmarks, put, edit]
  );

  const deleteBookmarks = useCallback(
    (itemId) => {
      setBookmarks(bookmarks.filter((bookmark) => bookmark.itemId !== itemId));
      //서버용
      del.bookmarkIdList = [...del.bookmarkIdList, itemId];
    },

    [bookmarks, del]
  );

  const deleteFolders = useCallback(
    (itemId) => {
      setFolders(folders.filter((folder) => folder.itemId !== itemId));
      del.folderIdList = [...del.folderIdList, itemId];
      setSelectFolder(0);
    },
    [folders, del]
  );

  const folderSelect = useCallback(
    (folderId) => {
      setSelectFolder(folderId);

      //폴더 선택시 해당 폴더의 북마크 가져오기
      GetData(folderId).then((res) => {
        setBookmarks([
          ...res.data.data.bookmarks, //서버에 저장된 북마크와
          ...post.bookmarks.filter((b) => b.parentId === folderId), //서버에 저장되기전 북마크
        ]);
      });
    },

    [post, selectFolder]
  );

  window.addEventListener("beforeunload", axios); //브라우저 종료전 서버통신

  // 폴더 모달 열기
  const folderModalOpen = () => {
    setFolderModal(true);
  };

  //폴더 수정 모달 열기
  const editFolderModalOpen = (folderId) => {
    setEdit(folderId);
    setEditFolder(folders.filter((f) => f.itemId === folderId));
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
      (folder) => folder.itemId === folderId
    );
    if (folderId === 0) return;
    else
      return selectFolderData.parentId == 0
        ? ` / ${selectFolderData.name}`
        : ` /
        ${
          folders.find((folder) => folder.itemId === selectFolderData.parentId)
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

          {/* <div className="nav">
            <Link to="/">My</Link>
            <Link to="/">Logout</Link>
          </div> */}
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
                <button onClick={() => deleteFolders(selectFolder)}>
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
        selectFolder={selectFolder}
      />
    </>
  );
};

export default Main;
