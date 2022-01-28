import React, { useState } from "react";
import Select from "react-select";
import "./AddFolderModal.css";

const AddFolderModal = ({ makeFolder, open, folderModalClose, folders }) => {
  const [name, setName] = useState("");
  const [select, setSelect] = useState(null);

  const options = folders.map((folder) => {
    const option = {};
    option.value = folder.folderId;
    option.label = folder.name;
    return option;
  });

  const onMake = (e) => {
    e.preventDefault();
    makeFolder(name, select ? select.value : 0, select ? 2 : 1);
    setName("");
    folderModalClose();
  };

  const onCancel = (e) => {
    e.preventDefault();
    setName("");
    folderModalClose();
  };

  return (
    <div className={open ? "open addFolderModal" : "addFolderModal"}>
      <div className="background"></div>
      <div className="components">
        <form>
          <div>
            <label>폴더 이름</label>
            <input value={name} onChange={(e) => setName(e.target.value)} />
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

export default AddFolderModal;
