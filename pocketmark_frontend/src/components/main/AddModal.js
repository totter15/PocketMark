import React, { useEffect, useState } from "react";
import Select from "react-select";
import "./AddModal.css";

const AddModal = ({
  makeBookmarks,
  open,
  modalClose,
  folders,
  edit,
  editBookmarks,
  editBookmark,
}) => {
  const [data, setData] = useState({
    name: "",
    url: "",
    comment: "",
  });
  const onChange = (e) => {
    setData({ ...data, [e.target.name]: e.target.value });
  };
  const [select, setSelect] = useState({ label: "내 폴더", value: 0 });

  const options = folders.map((folder) => {
    const option = {};
    option.value = folder.folderId;
    option.label = folder.name;
    return option;
  });

  useEffect(() => {
    if (edit) setData(editBookmark[0]);
  }, [edit]);

  const onMake = (e) => {
    e.preventDefault();
    !edit
      ? makeBookmarks(data.name, data.url, data.comment, select.value)
      : editBookmarks(data.name, data.url, data.comment, select.value);
    setData({
      name: "",
      url: "",
      comment: "",
    });
    modalClose();
  };

  const onCancel = (e) => {
    e.preventDefault();
    setData({
      name: "",
      url: "",
      comment: "",
    });
    modalClose();
  };

  return (
    <div className={open ? "addModal open" : "addModal"}>
      <div className="background"></div>
      <div className="components">
        <form>
          <div>
            <label>이름</label>
            <input value={data.name} name="name" onChange={onChange} />
          </div>
          <div>
            <label>코멘트</label>
            <input value={data.comment} name="comment" onChange={onChange} />
          </div>
          <div>
            <label>url</label>
            <input value={data.url} name="url" onChange={onChange} />
          </div>

          <Select
            onChange={(option) => setSelect(option)}
            options={options}
            value={select}
          />
        </form>
        <div className="buttonContainer">
          <button onClick={onCancel}>취소</button>
          <button onClick={onMake}>{edit ? "수정하기" : `만들기`}</button>
        </div>
      </div>
    </div>
  );
};

export default AddModal;
