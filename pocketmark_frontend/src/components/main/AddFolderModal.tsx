import React, { ChangeEvent, useEffect, useState } from 'react';
import CreatableSelect from 'react-select/creatable';
import './AddFolderModal.css';
import useFolder from '../../hooks/useFolder';
import useEdit from '../../hooks/useEdit';
import { editDone } from '../../slices/editData';
import FolderSelect from './FolderSelect';
import useFolderSelect from '../../hooks/useFolderSelect';

const AddFolderModal = ({ open, folderModalClose, itemId, handleId }: any) => {
	const { addFolder, editFolder } = useFolder();
	const { isEditFolder, editData } = useEdit();
	const { selectFolder, selectHandler } = useFolderSelect();

	const [name, setName] = useState('');
	const [tag, setTag] = useState({
		inputValue: '',
		value: [],
	});

	function onChangeName(e: ChangeEvent<HTMLInputElement>) {
		setName(e.target.value);
	}

	useEffect(() => {
		if (open) {
			selectHandler({ label: 'Root', value: 0 });
			setName('');
			setTag({ inputValue: '', value: [] });
		}
	}, [open]);

	// 폴더 수정시 원래이름을 기본값으로
	useEffect(() => {
		if (isEditFolder && editData) {
			setName(editData.name);
			// editData.tags &&
			// 	setTag({
			// 		inputValue: '',
			// 		value: editData.tags.map((b: any) => ({
			// 			label: b.name,
			// 			value: b.name,
			// 		})),
			// 	});
		}
	}, [editData]);

	const onMake = (e: any) => {
		e.preventDefault();

		const folderData = {
			name,
			itemId: isEditFolder ? editData.itemId : itemId,
			parentId: selectFolder ? selectFolder.value : 0,
		};

		if (isEditFolder) {
			editFolder.mutate(folderData);
		}
		if (!isEditFolder) {
			addFolder.mutate(folderData);
			handleId();
		}
		editDone();
		folderModalClose();
	};

	const onCancel = (e: any) => {
		e.preventDefault();
		editDone();
		folderModalClose();
	};

	const createOption = (label: any) => {
		return {
			label,
			value: label,
		};
	};

	const handleChange = (value: any) => {
		setTag({ ...tag, value: value });
	};
	const handleInputChange = (inputValue: any) => {
		setTag({ ...tag, inputValue: inputValue });
	};

	const handleKeyDown = (event: any) => {
		const { inputValue, value } = tag;
		if (!inputValue) return;
		switch (event.key) {
			case 'Enter':
			case 'Tab':
				!event.nativeEvent.isComposing &&
					// setTag({
					// 	inputValue: '',
					// 	value: [...value, createOption(inputValue)],
					// });
					event.preventDefault();
		}
	};

	return (
		<div className={open ? 'open addFolderModal' : 'addFolderModal'}>
			<div className='background'></div>
			<div className='components'>
				<form>
					<div className='folderName'>
						<label>폴더 이름</label>
						<input value={name} onChange={onChangeName} />
					</div>
					<FolderSelect selectHandler={selectHandler} select={selectFolder} />
					<CreatableSelect
						inputValue={tag.inputValue}
						isClearable
						isMulti
						menuIsOpen={false}
						onChange={handleChange}
						onInputChange={handleInputChange}
						onKeyDown={handleKeyDown}
						placeholder='태그 입력'
						value={tag.value}
					/>
					<div className='buttonContainer'>
						<button onClick={onCancel}>취소</button>
						<button onClick={onMake}>
							{!isEditFolder ? '만들기' : '수정하기'}
						</button>
					</div>
				</form>
			</div>
		</div>
	);
};

export default AddFolderModal;
