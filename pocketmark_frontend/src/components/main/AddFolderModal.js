import React, { useCallback, useEffect, useState } from "react";
import Select from "react-select";
import "./AddFolderModal.css";

const AddFolderModal = ({ makeFolder, open, folderModalClose, folders }) => {
  const [name, setName] = useState("");
  const [select, setSelect] = useState({ label: "내 폴더", value: 0 });
  const [options, setOptions] = useState([{}]);

  const getOptions = useCallback(() => {
    let options = [];
    folders.forEach((folder) => {
      if (folder.depth != 2)
        //depth 1단까지 옵션보이기
        options.push({
          value: folder.folderId,
          label: folder.name,
        });
    });
    setOptions(options);
  }, [folders]);

  useEffect(() => {
    getOptions();
  }, [folders]);

  const onMake = (e) => {
    e.preventDefault();
    makeFolder(name, select ? select.value : 0, select.value === 0 ? 1 : 2);
    setName("");
    setSelect(null);
    folderModalClose();
  };

  const onCancel = (e) => {
    e.preventDefault();
    setName("");
    setSelect(null);
    folderModalClose();
  };

  return (
    <div className={open ? "open addFolderModal" : "addFolderModal"}>
      <div className="background"></div>
      <div className="components">
        <form>
          <div className="folderName">
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
