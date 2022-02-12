import React, { useEffect, useState } from "react";
import Select from "react-select";
import CreatableSelect from "react-select/creatable";
import { ActionMeta, OnChangeValue } from "react-select";

import "./AddModal.css";

const AddModal = ({
  makeBookmarks,
  open,
  modalClose,
  folders,
  edit,
  editBookmarks,
  editBookmark,
  selectFolder,
}) => {
  //bookmark
  const [data, setData] = useState({
    name: "",
    url: "",
    comment: "",
  });

  //tag
  const onChange = (e) => {
    setData({ ...data, [e.target.name]: e.target.value });
  };
  const [select, setSelect] = useState({ label: "내 폴더", value: 0 });
  const [tag, setTag] = useState({
    inputValue: "",
    value: [],
  });

  //옵션 생성
  const options = folders.map((folder) => {
    const option = {};
    option.value = folder.itemId;
    option.label = folder.name;
    return option;
  });

  useEffect(() => {
    if (editBookmark) {
      setData(editBookmark[0]);
      editBookmark[0].tags &&
        setTag({
          ...tag,
          value: editBookmark[0].tags.map((b) => ({
            label: b.name,
            value: b.name,
          })),
        });
    }
    selectFolder && setSelect(options.find((o) => o.value === selectFolder));
  }, [selectFolder, editBookmark]);

  const onMake = (e) => {
    e.preventDefault();
    !edit
      ? makeBookmarks(
          data.name,
          data.url,
          data.comment,
          select.value,
          tag.value
        )
      : editBookmarks(
          data.name,
          data.url,
          data.comment,
          select.value,
          tag.value
        );
    setData({
      name: "",
      url: "",
      comment: "",
    });
    setTag({ inputValue: "", value: [] });
    modalClose();
  };

  const onCancel = (e) => {
    e.preventDefault();
    setData({
      name: "",
      url: "",
      comment: "",
    });
    setTag({ inputValue: "", value: [] });

    modalClose();
  };

  const createOption = (label) => ({
    label,
    value: label,
  });

  const handleChange = (value) => {
    // console.group("Value Changed");
    // console.log(value);
    // console.groupEnd();
    setTag({ ...tag, value: value });
  };
  const handleInputChange = (inputValue) => {
    setTag({ ...tag, inputValue: inputValue });
  };

  const handleKeyDown = (event) => {
    const { inputValue, value } = tag;
    if (!inputValue) return;
    switch (event.key) {
      case "Enter":
      case "Tab":
        // console.group("Value Added");
        // console.log(value);
        // console.groupEnd();
        setTag({
          inputValue: "",
          value: [...value, createOption(inputValue)],
        });
        event.preventDefault();
    }
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
          <CreatableSelect
            inputValue={tag.inputValue}
            isClearable
            isMulti
            menuIsOpen={false}
            onChange={handleChange}
            onInputChange={handleInputChange}
            onKeyDown={handleKeyDown}
            placeholder="태그 입력"
            value={tag.value}
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
