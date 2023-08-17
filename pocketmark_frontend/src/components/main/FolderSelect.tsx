import React, { useEffect } from 'react';
import { useQueryClient } from 'react-query';
import Select from 'react-select';
import { FolderType, ResponseFolderType } from '../../interfaces/data';
import useEdit from '../../hooks/useEdit';

export interface FolderSelectType {
	label: string;
	value: number;
}

function FolderSelect({
	selectHandler,
	select,
}: {
	selectHandler: (select: FolderSelectType) => void;
	select: FolderSelectType | null;
}) {
	const { editData } = useEdit();
	const queryClient = useQueryClient();
	const data = queryClient.getQueryData('folder') as ResponseFolderType;
	const folders = data?.data.folders ?? [];

	const parentFolders = folders.filter(
		(folder: FolderType) => folder.parentId === 0 || folder.parentId === null
	);
	const folderOptions = parentFolders.map((folder: FolderType) => {
		const option: any = {};
		option.value = folder.itemId;
		option.label = folder.name;
		return option;
	});

	useEffect(() => {
		if (editData) {
			const editFolder = folderOptions.find(
				(folder) => folder.value === editData.parentId
			);

			selectHandler(editFolder);
		}
	}, [editData]);

	return (
		<Select
			onChange={(option: FolderSelectType) => selectHandler(option)}
			options={folderOptions}
			value={select}
			defaultValue={folderOptions[0]}
		/>
	);
}

export default FolderSelect;
