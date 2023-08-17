import React, { ChangeEvent, useEffect, useState } from 'react';
import './AddFolderModal.css';
import useFolder from '../../hooks/useFolder';
import useEdit from '../../hooks/useEdit';
import FolderSelect from './FolderSelect';
import useFolderSelect from '../../hooks/useFolderSelect';
import useCurrentFolder from '../../hooks/useCurrentFolder';
import TagSelect from './TagSelect';
import useTagSelect from '../../hooks/useTagSelect';
import useTag from '../../hooks/useTag';
import { FolderType } from '../../interfaces/data';

const AddFolderModal = ({ open, folderModalClose, itemId, handleId }: any) => {
	const { currentFolder, selectCurrentFolder } = useCurrentFolder();
	const { isEditFolder, editData, editDoneHandler } = useEdit();

	const { addFolder, editFolder } = useFolder();
	const { addFolderTagHandler, deleteFolderTagHandler } = useTag();

	const [name, setName] = useState('');
	const { selectFolder, selectHandler } = useFolderSelect();
	const { tag, tagHandler, resetTag } = useTagSelect();

	function onChangeName(e: ChangeEvent<HTMLInputElement>) {
		setName(e.target.value);
	}

	useEffect(() => {
		if (open) {
			if (!isEditFolder) {
				setName('');
				resetTag();
				selectHandler({
					label: currentFolder.name,
					value: currentFolder.itemId,
				});
			}
			if (isEditFolder) {
				setName(editData.name);
			}
		}
	}, [open, editData, isEditFolder, currentFolder]);

	const onMake = async (e: any) => {
		e.preventDefault();
		const id = isEditFolder ? editData.itemId : itemId;
		const data = {
			name,
			itemId: id,
			parentId: selectFolder ? selectFolder.value : 0,
		};
		const tagData =
			tag?.value?.map((item) => ({ itemId, name: item.value })) ?? [];

		isEditFolder ? editFolderHandler(data) : makeFolderHandler(data);
		selectCurrentFolder({ ...data, tags: tagData });
		folderModalClose();
	};

	async function makeFolderHandler(data: FolderType) {
		const tagData =
			tag?.value?.map((item) => ({ itemId, name: item.value })) ?? [];
		const isTagData = tagData.length > 0;

		await addFolder.mutateAsync(data);
		isTagData && (await addFolderTagHandler.mutateAsync(tagData));
		handleId();
	}

	async function editFolderHandler(data: FolderType) {
		const currentTag = editData?.tags?.flatMap(
			(tag: { name: string }) => tag.name
		);
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

		await editFolder.mutateAsync(data);
		isCreateTag && (await addFolderTagHandler.mutateAsync(createTag));
		isDeleteTag && (await deleteFolderTagHandler.mutateAsync(deleteTag));
		editDoneHandler();
	}

	const onCancel = (e: any) => {
		e.preventDefault();
		editDoneHandler();
		folderModalClose();
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
					<TagSelect tag={tag} handleTag={tagHandler} />
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
