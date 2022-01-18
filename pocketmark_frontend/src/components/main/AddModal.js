import React, { useState } from "react";
import Select from "react-select";
import "./AddModal.css";

const AddModal = ({ makeBookmarks, open, modalClose, folders }) => {
  const [data, setData] = useState({
    name: "",
    url: "",
    comment: "",
  });
  const onChange = (e) => {
    setData({ ...data, [e.target.name]: e.target.value });
  };
  const [select, setSelect] = useState(null);

  const options = folders.map((folder) => {
    const option = {};
    option.value = folder.folderId;
    option.label = folder.name;
    return option;
  });

  const onMake = (e) => {
    e.preventDefault();
    makeBookmarks(data.name, data.url, data.comment, select.value);
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

          <div className="buttonContainer">
            <button onClick={onCancel}>취소</button>
            <button onClick={onMake}>만들기</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddModal;
