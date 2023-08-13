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

const AddFolderModal = ({ open, folderModalClose, itemId, handleId }: any) => {
	const { currentFolder } = useCurrentFolder();
	const { addFolder, editFolder } = useFolder();
	const { isEditFolder, editData, editDoneHandler } = useEdit();
	const { selectFolder, selectHandler } = useFolderSelect();
	const { selectCurrentFolder } = useCurrentFolder();
	const { tag, tagHandler, resetTag } = useTagSelect();
	const { addFolderTagHandler, deleteFolderTagHandler } = useTag();

	const [name, setName] = useState('');

	function onChangeName(e: ChangeEvent<HTMLInputElement>) {
		setName(e.target.value);
	}

	useEffect(() => {
		if (open) {
			if (!isEditFolder) {
				setName('');
				selectHandler({
					label: currentFolder.name,
					value: currentFolder.itemId,
				});
				resetTag();
			}
			if (isEditFolder) {
				setName(editData.name);
			}
		}
	}, [open, editData, isEditFolder, currentFolder]);

	const onMake = async (e: any) => {
		e.preventDefault();
		const id = isEditFolder ? editData.itemId : itemId;
		const folderData = {
			name,
			itemId: id,
			parentId: selectFolder ? selectFolder.value : 0,
		};

		const tagData = () =>
			tag?.value?.map((item) => ({ itemId: id, name: item.value })) ?? [];
		const currentTag = () =>
			editData?.tags?.map((tag: { name: string }) => ({
				itemId: id,
				name: tag.name,
			})) ?? [];

		if (isEditFolder) {
			const deleteTag = currentTag().filter(
				(current: { name: string }) =>
					!tagData().some((tag) => tag.name === current.name)
			);
			const createTag = tagData().filter(
				(tag) =>
					!currentTag().some(
						(current: { name: string }) => tag.name === current.name
					)
			);

			await editFolder.mutateAsync(folderData);
			!!createTag.length && (await addFolderTagHandler.mutateAsync(createTag));
			!!deleteTag.length &&
				(await deleteFolderTagHandler.mutateAsync(deleteTag));
		}
		if (!isEditFolder) {
			await addFolder.mutateAsync(folderData);
			!!tagData().length && (await addFolderTagHandler.mutateAsync(tagData()));

			handleId();
		}

		selectCurrentFolder({ ...folderData, tags: tagData() });
		editDoneHandler();
		folderModalClose();
	};

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
