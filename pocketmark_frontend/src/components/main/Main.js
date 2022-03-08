import React, { useState, useRef, useEffect, useCallback } from "react";
import { IoAddCircleOutline } from "react-icons/io5";
import AddFolderModal from "./AddFolderModal";
import AddModal from "./AddModal";
import FolderList from "./FolderList";
import BookmarkList from "./BookmarkList";
import FolderRoute from "./FolderRoute";
import { useNavigate, useLocation } from "react-router-dom";
import { Link } from "react-router-dom";

import "./Main.css";
import {
  DeleteData,
  PostData,
  PutData,
  GetData,
  GetAllFolders,
  PostTag,
  DelTag,
} from "../../lib/Axios";
import { getCookis, removeCookie, setCookie } from "../../lib/cookie";

const Main = () => {
  const [search, setSearch] = useState("");

  //folders/bookmarks
  const [folders, setFolders] = useState([]); //전체 폴더
  const [bookmarks, setBookmarks] = useState([]); //선택된 폴더의 북마크
  const selectFolderId = useRef(0); //선택된 폴더 아이디

  //modal
  const [folderModal, setFolderModal] = useState(false); //폴더 모달
  const [addModal, setAddModal] = useState(false); //북마크 모달

  const [editBookmark, setEditBookmark] = useState(null);
  const [editFolder, setEditFolder] = useState(null);
  const [edit, setEdit] = useState(null); //수정하는 아이템 아이디

  const [refresh, setRefresh] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();

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
    postTag: {
      tags: [],
    },
    delTag: {
      tags: [],
    },
  });

  const { post, put, del, postTag, delTag } = server.current;

  window.onbeforeunload = function (event) {
    axios(server.current, selectFolderId);
    event.preventDefault();
    return "";
    //서버통신하는 동안 시간벌기용...
  };

  useEffect(() => {
    GetData(0).then((res) => setBookmarks(res.data.data.bookmarks));
    GetAllFolders().then((res) => {
      setFolders(res.data.data.folders.slice(1));
    });
  }, []);

  useEffect(() => {
    setInterval(() => axios(server.current, selectFolderId), 1000 * 60 * 5); //5분
  }, []);

  const axios = useCallback((server, selectFolderId) => {
    const { post, put, del, postTag, delTag } = server;
    PostData(post) //data만들기
      .then(() => {
        if (put.folders.length > 0 || put.bookmarks.length > 0) PutData(put); //data수정
      })
      .then(() => {
        if (del.folderIdList.length > 0 || del.bookmarkIdList.length > 0)
          DeleteData(del); //data삭제
      })
      .then(() => postTag.tags.length > 0 && PostTag(postTag)) //태그 만들기
      .then(() => delTag.tags.length > 0 && DelTag(delTag)) //태그 삭제
      .then(() => GetData(selectFolderId.current)) //현재 선택된 폴더데이터 가져오기
      .then((res) => {
        setBookmarks(res.data.data.bookmarks); //현재 선택된 폴더의 북마크 가져오기
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
        server.postTag = {
          tags: [],
        };
        server.delTag = {
          tags: [],
        };
      })
      .then(() => {
        setCookie("lastId", itemId.current - 1);
      })
      .then(() =>
        GetAllFolders().then((res) => {
          setFolders(res.data.data.folders.slice(1));
        })
      )
      .catch((e) => {});
  }, []);

  //폴더 만들기
  const makeFolder = useCallback(
    (folderName, parent, tags) => {
      let tag = [];
      tags.forEach((t) => tag.push({ itemId: itemId.current, name: t.value }));
      setFolders([
        ...folders,
        {
          itemId: itemId.current,
          parentId: parent,
          name: folderName,
          visitCount: 0,
          tags: tag,
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
      postTag.tags = [...postTag.tags, ...tag];
      ++itemId.current;
      folderModalClose();
    },
    [folders, post]
  );

  //폴더 수정하기
  const editFolders = useCallback((folderName, parent, tags) => {
    const initTag = editFolder[0].tags
      ? editFolder[0].tags.map((item) => ({
          itemId: edit,
          name: item.name,
        }))
      : []; //기존 태그
    let tag = []; //새로운 태그
    tags.forEach((t) => tag.push({ itemId: edit, name: t.value }));

    let post = tag.filter((tag) => !initTag.some((t) => t.name === tag.name));
    let del = initTag.filter(
      (inittag) => !tag.some((t) => t.name === inittag.name)
    );

    // console.group("tag");
    // console.log(initTag, "초기태그");
    // console.log(tag, "새로운태그");
    // console.log(post, "post");
    // console.log(del, "delete");
    // console.groupEnd();

    setFolders(
      folders.map((folder) =>
        folder.itemId === edit
          ? {
              name: folderName,
              parentId: parent,
              itemId: edit,
              visitCount: 0,
              tags: tag,
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
    postTag.tags = [...postTag.tags, ...post];
    delTag.tags = [...delTag.tags, ...del];
  });

  //폴더 삭제하기
  const deleteFolders = useCallback(
    (itemId) => {
      setFolders(folders.filter((folder) => folder.itemId !== itemId));
      del.folderIdList = [...del.folderIdList, itemId];
      selectFolderId.current = 0;
    },
    [folders, del]
  );

  //북마크 만들기
  const makeBookmarks = useCallback(
    (bookmarkName, url, comment, folderId, tags) => {
      let tag = [];
      tags.forEach((t) => tag.push({ itemId: itemId.current, name: t.value }));
      setBookmarks([
        ...bookmarks,
        {
          itemId: itemId.current,
          name: bookmarkName,
          url: url,
          comment: comment,
          parentId: folderId,
          tags: tag,
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
      postTag.tags = [...postTag.tags, ...tag];
      ++itemId.current;
      modalClose();
    },
    [bookmarks, post]
  );

  //북마크 수정하기
  const editBookmarks = useCallback(
    (bookmarkName, url, comment, parentId, tags) => {
      const initTag = editBookmark[0].tags
        ? editBookmark[0].tags.map((item) => ({
            itemId: edit,
            name: item.name,
          }))
        : []; //기존 태그
      let tag = []; //새로운 태그
      tags.forEach((t) => tag.push({ itemId: edit, name: t.value }));

      let post = tag.filter((tag) => !initTag.some((t) => t.name === tag.name));
      let del = initTag.filter(
        (inittag) => !tag.some((t) => t.name === inittag.name)
      );

      // console.group("tag");
      // console.log(initTag, "초기태그");
      // console.log(tag, "새로운태그");
      // console.log(post, "post");
      // console.log(del, "delete");
      // console.groupEnd();
      //태그 확인용

      setBookmarks(
        bookmarks.map((bookmark) =>
          bookmark.itemId === edit
            ? {
                itemId: edit,
                parentId: parentId,
                name: bookmarkName,
                url: url,
                comment: comment,
                tags: tag,
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
      postTag.tags = [...postTag.tags, ...post];
      delTag.tags = [...delTag.tags, ...del];

      modalClose();
    },
    [bookmarks, put, edit, postTag, delTag]
  );

  //북마크 삭제하기
  const deleteBookmarks = useCallback(
    (itemId) => {
      setBookmarks(bookmarks.filter((bookmark) => bookmark.itemId !== itemId));
      //서버용
      del.bookmarkIdList = [...del.bookmarkIdList, itemId];
    },

    [bookmarks, del]
  );

  const folderSelect = useCallback(
    (folderId) => {
      let initBookmarks = post.bookmarks;
      selectFolderId.current = folderId;
      //useRef사용시 객체주소가 불러와져 다른변수에 할당한다해도 주소의 객체도 변경됨
      //https://flyingsquirrel.medium.com/react-%EC%BD%94%EB%93%9C-%EA%B9%8C%EB%B3%B4%EA%B8%B0-useref%EB%8A%94-dom%EC%97%90-%EC%A0%91%EA%B7%BC%ED%95%A0-%EB%95%8C-%EB%BF%90%EB%A7%8C-%EC%95%84%EB%8B%88%EB%9D%BC-%EB%8B%A4%EC%96%91%ED%95%98%EA%B2%8C-%EC%9D%91%EC%9A%A9%ED%95%A0-%EC%88%98-%EC%9E%88%EC%96%B4%EC%9A%94-f0359ad23f3b

      //폴더 선택시 해당 폴더의 북마크 가져오기
      GetData(folderId).then((res) => {
        let bookmarks = [
          ...res.data.data.bookmarks,
          ...initBookmarks.filter((b) => b.parentId === folderId),
        ];

        const bookmarkHasTag = bookmarks.map((b) => {
          let findTag = postTag.tags.filter((t) => t.itemId === b.itemId);
          let tags = b.tags;
          findTag.length > 0 &&
            (tags = b.tags ? b.tags.concat(findTag) : findTag);
          //itemId에 맞는 태그 넣기

          let deleteTag = delTag.tags.filter((t) => t.itemId === b.itemId);
          deleteTag &&
            (tags = tags.filter(
              (t) => !deleteTag.find((d) => d.name === t.name)
            ));
          //delTag에 들어간 tag 지우기
          return { ...b, tags: tags };
        });
        //기존 북마크 + 새로만든 북마크

        const edited = bookmarkHasTag.map((bookmark) => {
          const editedBookmark =
            put.bookmarks.findLast &&
            put.bookmarks.findLast((b) => b.itemId === bookmark.itemId);
          //가장 마지막으로 수정된 버전 찾기

          return editedBookmark
            ? Object.assign(bookmark, editedBookmark) //수정된 북마크가 있다면 수정
            : bookmark; //없다면 기존
        });

        const deleted = edited.filter(
          (b) => !del.bookmarkIdList.some((d) => d === b.itemId)
        );
        //삭제리스트에 있다면 삭제
        setBookmarks(deleted);
      });
    },
    [post]
  );

  // 폴더 모달 열기
  const folderModalOpen = () => {
    setFolderModal(true);
  };

  //폴더 수정 모달 열기
  const editFolderModalOpen = (itemId) => {
    setEdit(itemId);
    setEditFolder(folders.filter((f) => f.itemId === itemId));
    setFolderModal(true);
  };

  // 폴더 모달 닫기
  const folderModalClose = () => {
    setFolderModal(false);
    setEdit(null);
    setEditFolder(null);
  };

  //북마크 모달 열기
  const modalOpen = () => {
    setAddModal(true);
  };

  // 북마크 수정 모달 열기
  const editModalOpen = (itemId) => {
    setEdit(itemId);
    setEditBookmark(bookmarks.filter((b) => b.itemId === itemId));
    setAddModal(true);
  };

  // 북마크 모달 닫기
  const modalClose = () => {
    setAddModal(false);
    setEdit(null);
    setEditBookmark(null);
  };

  const onLogout = () => {
    removeCookie("autoLogin");

    setTimeout(() => {
      navigate("/");
    }, 500);
  };

  //폴더 경로 표시
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

          <div className="nav">
            <div>{/* <Link to="/mypage">My</Link> */}</div>
            <div onClick={onLogout} className="logout">
              Logout
            </div>
          </div>
        </header>

        <main>
          {/* 폴더 네비게이션 */}
          <FolderList
            folders={folders}
            folderModalOpen={folderModalOpen}
            folderSelect={folderSelect}
            selectFolder={selectFolderId.current}
          />

          <section className="bookmark">
            <FolderRoute
              route={getRoute(selectFolderId.current)}
              folders={folders}
              selectFolderId={selectFolderId.current}
              editFolderModalOpen={editFolderModalOpen}
              deleteFolders={deleteFolders}
            />

            {/* 북마크 리스트 */}
            <BookmarkList
              bookmarks={bookmarks}
              editModalOpen={editModalOpen}
              deleteBookmarks={deleteBookmarks}
              selectFolder={selectFolderId.current}
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
        selectFolder={selectFolderId.current}
      />
    </>
  );
};

export default Main;
