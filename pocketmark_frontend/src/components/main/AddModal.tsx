import React, { useEffect } from 'react';
import useBookmarkModalData from '../../hooks/useBookmarkModalData';
import useEdit from '../../hooks/useEdit';
import useFolderData from '../../hooks/useFolderData';
import './AddModal.css';
import FolderSelect from './FolderSelect';
import useFolderSelect from '../../hooks/useFolderSelect';
import useCurrentFolder from '../../hooks/useCurrentFolder';
import TagSelect from './TagSelect';
import useTagSelect from '../../hooks/useTagSelect';
import useTag from '../../hooks/useTag';
import { BookmarkType } from '../../interfaces/data';

const AddModal = ({ open, modalClose, itemId, handleId }: any) => {
	const { currentFolder } = useCurrentFolder();
	const { isEditBookmark, editData, editDoneHandler } = useEdit();

	const { editFolderData, addFolderData } = useFolderData();
	const { addBookmarkTagHandler, deleteBookmarkTagHandler } = useTag();

	const { formData, formDataHandler, resetFormData } = useBookmarkModalData();
	const { selectFolder, selectHandler } = useFolderSelect();
	const { tag, tagHandler, resetTag } = useTagSelect();

	useEffect(() => {
		if (open && !isEditBookmark) {
			resetFormData();
			resetTag();
			selectHandler({
				label: currentFolder.name,
				value: currentFolder.itemId,
			});
		}
	}, [open, isEditBookmark, currentFolder]);

	const onMake = async (e: any) => {
		e.preventDefault();
		const { name, url, comment } = formData ?? {};
		const id = isEditBookmark ? editData.itemId : itemId;
		const data = {
			comment,
			itemId: id,
			name,
			parentId: selectFolder.value,
			url,
			visitCount: 0,
		};

		isEditBookmark ? editBookmarkHandler(data) : makeBookmarkHandler(data);
		modalClose();
	};

	async function makeBookmarkHandler(data: BookmarkType) {
		const tagData =
			tag?.value?.map((item) => ({ itemId, name: item.value })) ?? [];
		const isTagData = tagData.length > 0;

		await addFolderData.mutateAsync({ bookmarks: [data], folders: [] });
		isTagData && (await addBookmarkTagHandler.mutateAsync(tagData));
		handleId();
	}

	async function editBookmarkHandler(data: BookmarkType) {
		const currentTag =
			editData?.tags?.flatMap((tag: { name: string }) => tag.name) ?? [];
		const tagData = tag?.value?.flatMap((tag) => tag.value);

		const deleteTag =
			currentTag
				.filter((current: string) => !tagData.includes(current))
				.map((item: string) => ({ itemId: editData.itemId, name: item })) ?? [];
		const createTag =
			tagData
				.filter((tag: string) => !currentTag.includes(tag))
				.map((item: string) => ({ itemId: editData.itemId, name: item })) ?? [];

		const isDeleteTag = deleteTag.length > 0;
		const isCreateTag = createTag.length > 0;

		await editFolderData.mutateAsync({ bookmarks: [data], folders: [] });
		isCreateTag && (await addBookmarkTagHandler.mutateAsync(createTag));
		isDeleteTag && (await deleteBookmarkTagHandler.mutateAsync(deleteTag));
		editDoneHandler();
	}

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
					<TagSelect tag={tag} handleTag={tagHandler} />
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
