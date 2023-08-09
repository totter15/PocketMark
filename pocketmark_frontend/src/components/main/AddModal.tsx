import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import CreatableSelect from 'react-select/creatable';
import { ActionMeta, OnChangeValue } from 'react-select';

import './AddModal.css';
import useFolderData from '../../hooks/useFolderData';
import useEdit from '../../hooks/useEdit';

const AddModal = ({ open, modalClose, folders, selectFolder, lastId }: any) => {
	const { editData, isEditBookmark, editDoneHandler } = useEdit();
	const { addFolderData, editFolderData } = useFolderData(selectFolder);

	const [data, setData] = useState({
		name: '',
		url: '',
		comment: '',
	});

	function makeBookmarks() {
		const { name, url, comment } = data ?? {};

		addFolderData.mutate({
			bookmarks: [
				{
					comment,
					itemId: lastId.current,
					name,
					parentId: selectFolder,
					url,
				},
			],
			folders: [],
		});
		lastId = lastId++;
	}

	function editBookmarks() {
		const { name, url, comment } = data ?? {};
		editFolderData.mutate({
			bookmarks: [
				{
					comment,
					itemId: editData.itemId,
					name,
					parentId: selectFolder,
					url,
					visitCount: 0,
				},
			],
			folders: [],
		});
	}

	const onMake = (e: any) => {
		e.preventDefault();

		isEditBookmark ? editBookmarks() : makeBookmarks();
		setData({
			name: '',
			url: '',
			comment: '',
		});
		setTag({ inputValue: '', value: [] });
		editDoneHandler();
		modalClose();
	};

	//tag
	const onChange = (e: any) => {
		setData({ ...data, [e.target.name]: e.target.value });
	};
	const [select, setSelect] = useState({ label: '내 폴더', value: 0 });
	const [tag, setTag] = useState({
		inputValue: '',
		value: [],
	});

	//옵션 생성
	const options = folders.map((folder: any) => {
		const option: any = {};
		option.value = folder.itemId;
		option.label = folder.name;
		return option;
	});

	useEffect(() => {
		if (isEditBookmark && editData) {
			console.log(editData, 'd');
			const { name, url, comment } = editData ?? {};
			setData({ name, url, comment });

			editData?.tags &&
				setTag({
					...tag,
					value: editData?.tags.map((b: any) => ({
						label: b.name,
						value: b.name,
					})),
				});
		}
		selectFolder &&
			setSelect(options.find((o: any) => o.value === selectFolder));
	}, [editData, isEditBookmark]);

	const onCancel = (e: any) => {
		e.preventDefault();
		setData({
			name: '',
			url: '',
			comment: '',
		});
		setTag({ inputValue: '', value: [] });
		editDoneHandler();
		modalClose();
	};

	const createOption = (label: any) => ({
		label,
		value: label,
	});

	const handleChange = (value: any) => {
		// console.group("Value Changed");
		// console.log(value);
		// console.groupEnd();
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
		<div className={open ? 'addModal open' : 'addModal'}>
			<div className='background'></div>
			<div className='components'>
				<form>
					<div>
						<label>이름</label>
						<input value={data.name} name='name' onChange={onChange} />
					</div>
					<div>
						<label>코멘트</label>
						<input value={data.comment} name='comment' onChange={onChange} />
					</div>
					<div>
						<label>url</label>
						<input value={data.url} name='url' onChange={onChange} />
					</div>

					<Select
						onChange={(option: any) => setSelect(option)}
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
						placeholder='태그 입력'
						value={tag.value}
					/>
				</form>
				<div className='buttonContainer'>
					<button onClick={onCancel}>취소</button>
					<button onClick={onMake}>
						{isEditBookmark ? '수정하기' : `만들기`}
					</button>
				</div>
			</div>
		</div>
	);
};

export default AddModal;
