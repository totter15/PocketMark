import React, { useCallback, useEffect, useState } from 'react';
import Select from 'react-select';
import CreatableSelect from 'react-select/creatable';
import './AddFolderModal.css';

const AddFolderModal = ({
	makeFolder,
	open,
	folderModalClose,
	folders,
	edit,
	editFolders,
	editFolder,
}: any) => {
	const [name, setName] = useState('');
	const [select, setSelect] = useState({ label: '내 폴더', value: 0 });
	const [options, setOptions] = useState<any>([]);
	const [tag, setTag] = useState({
		inputValue: '',
		value: [],
	});

	const getOptions = useCallback(() => {
		let options = [{ label: '내 폴더', value: 0 }];
		folders.forEach((folder: any) => {
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

	// 폴더 수정시 원래이름을 기본값으로
	useEffect(() => {
		if (editFolder) {
			setName(editFolder[0].name);
			setSelect(options.find((o: any) => o.value === editFolder[0].parentId));
			editFolder[0].tags &&
				setTag({
					inputValue: '',
					value: editFolder[0].tags.map((b: any) => ({
						label: b.name,
						value: b.name,
					})),
				});
		}
	}, [editFolder]);

	const onMake = (e: any) => {
		e.preventDefault();
		!edit
			? makeFolder(name, select ? select.value : 0, tag.value) //폴더 만들기
			: editFolders(name, select.value, tag.value); //폴더 수정하기
		setName('');
		setSelect({ label: '내 폴더', value: 0 });
		setTag({ inputValue: '', value: [] });
		folderModalClose();
	};

	const onCancel = (e: any) => {
		e.preventDefault();
		setName('');
		setSelect({ label: '내 폴더', value: 0 });
		setTag({ inputValue: '', value: [] });

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
						<input value={name} onChange={(e) => setName(e.target.value)} />
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
					<div className='buttonContainer'>
						<button onClick={onCancel}>취소</button>
						<button onClick={onMake}>{!edit ? '만들기' : '수정하기'}</button>
					</div>
				</form>
			</div>
		</div>
	);
};

export default AddFolderModal;
