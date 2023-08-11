import React from 'react';
import Select from 'react-select';
import CreatableSelect from 'react-select/creatable';
import useBookmarkModalData from '../../hooks/useBookmarkModalData';
import useBookmarkModalFolder from '../../hooks/useBookmarkModalFolder';
import useBookmarkModalTag from '../../hooks/useBookmarkModalTag';
import useEdit from '../../hooks/useEdit';
import useFolderData from '../../hooks/useFolderData';
import './AddModal.css';

const AddModal = ({
	open,
	modalClose,
	selectFolder,
	itemId,
	handleId,
}: any) => {
	const { isEditBookmark, editData, editDoneHandler } = useEdit();
	const { editFolderData, addFolderData } = useFolderData(selectFolder);

	const { formData, formDataHandler, resetFormData } = useBookmarkModalData();
	const { folderSelect, folderOptions, folderSelectHandler } =
		useBookmarkModalFolder(selectFolder);
	const { tag, handleChange, handleInputChange, handleKeyDown, resetTag } =
		useBookmarkModalTag();

	function reset() {
		resetFormData();
		resetTag();
		editDoneHandler();
		modalClose();
	}

	const onMake = (e: any) => {
		e.preventDefault();
		const { name, url, comment } = formData ?? {};
		const addData = {
			comment,
			itemId: isEditBookmark ? editData.itemId : itemId,
			name,
			parentId: folderSelect.value,
			url,
			visitCount: 0,
		};

		if (isEditBookmark) {
			editFolderData.mutate({ bookmarks: [addData], folders: [] });
		}
		if (!isEditBookmark) {
			addFolderData.mutate({ bookmarks: [addData], folders: [] });
			handleId();
		}

		reset();
	};

	const onCancel = (e: any) => {
		e.preventDefault();
		reset();
	};

	return (
		<div className={open ? 'addModal open' : 'addModal'}>
			<div className='background'></div>
			<div className='components'>
				<form>
					<div>
						<label>이름</label>
						<input
							value={formData.name}
							name='name'
							onChange={formDataHandler}
						/>
					</div>
					<div>
						<label>코멘트</label>
						<input
							value={formData.comment}
							name='comment'
							onChange={formDataHandler}
						/>
					</div>
					<div>
						<label>url</label>
						<input value={formData.url} name='url' onChange={formDataHandler} />
					</div>

					<Select
						onChange={folderSelectHandler}
						options={folderOptions}
						value={folderSelect}
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
