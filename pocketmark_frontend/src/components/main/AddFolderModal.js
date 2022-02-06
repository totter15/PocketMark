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
    let options = [{ label: "내 폴더", value: 0 }];
    folders.forEach((folder) => {
      folder.parentId === 0 && //부모 폴더만 리스트에 뜨게
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

  //폴더 수정시 원래이름을 기본값으로
  useEffect(() => {
    editFolder && setName(editFolder[0].name);
    setSelect(options.find((o) => o.value === editFolder[0].parentId));
  }, [editFolder]);

  const onMake = (e) => {
    e.preventDefault();
    !edit
      ? makeFolder(name, select ? select.value : 0) //폴더 만들기
      : editFolders(name, select.value); //폴더 수정하기
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
