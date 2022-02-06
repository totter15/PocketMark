import React, { useCallback, useEffect, useState } from "react";
import Select from "react-select";
import "./AddFolderModal.css";

const AddFolderModal = ({
  makeFolder,
  open,
  folderModalClose,
  folders,
  edit,
  editFolders,
  editFolder,
}) => {
  const [name, setName] = useState("");
  const [select, setSelect] = useState({ label: "내 폴더", value: 0 });
  const [options, setOptions] = useState([]);

  const getOptions = useCallback(() => {
    let options = [];
    folders.forEach((folder) => {
      options.push({
        value: folder.itemId,
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
    !edit
      ? makeFolder(name, select ? select.value : 0)
      : editFolders(name, select.value);
    setName("");
    setSelect({ label: "내 폴더", value: 0 });
    folderModalClose();
  };

  const onCancel = (e) => {
    e.preventDefault();
    setName("");
    setSelect({ label: "내 폴더", value: 0 });
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
            <button onClick={onMake}>{!edit ? "만들기" : "수정하기"}</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddFolderModal;
