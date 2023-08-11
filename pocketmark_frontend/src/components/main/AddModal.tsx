import React, { useEffect } from 'react';
import CreatableSelect from 'react-select/creatable';
import useBookmarkModalData from '../../hooks/useBookmarkModalData';
import useBookmarkModalTag from '../../hooks/useBookmarkModalTag';
import useEdit from '../../hooks/useEdit';
import useFolderData from '../../hooks/useFolderData';
import './AddModal.css';
import FolderSelect from './FolderSelect';
import useFolderSelect from '../../hooks/useFolderSelect';
import useCurrentFolder from '../../hooks/useCurrentFolder';

const AddModal = ({ open, modalClose, itemId, handleId }: any) => {
	const { currentFolder } = useCurrentFolder();
	const { isEditBookmark, editData, editDoneHandler } = useEdit();
	const { editFolderData, addFolderData } = useFolderData(currentFolder.itemId);

	const { formData, formDataHandler, resetFormData } = useBookmarkModalData();
	const { selectFolder, selectHandler } = useFolderSelect();
	const { tag, handleChange, handleInputChange, handleKeyDown, resetTag } =
		useBookmarkModalTag();

	useEffect(() => {
		if (open) {
			resetFormData();
			resetTag();
			selectHandler({ label: currentFolder.name, value: currentFolder.itemId });
		}
	}, [open, currentFolder]);

	const onMake = (e: any) => {
		e.preventDefault();
		const { name, url, comment } = formData ?? {};
		const addData = {
			comment,
			itemId: isEditBookmark ? editData.itemId : itemId,
			name,
			parentId: selectFolder.value,
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

		editDoneHandler();
		modalClose();
	};

	const onCancel = (e: any) => {
		e.preventDefault();
		editDoneHandler();
		modalClose();
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

					<FolderSelect select={selectFolder} selectHandler={selectHandler} />
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
