import React, { useEffect } from 'react';
import { useQueryClient } from 'react-query';
import Select from 'react-select';
import { ResponseFolderType } from '../../interfaces/data';

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
	const queryClient = useQueryClient();
	const data = queryClient.getQueryData('folder') as ResponseFolderType;
	const folders = data?.data.folders ?? [];

	const folderOptions = folders.map((folder: any) => {
		const option: any = {};
		option.value = folder.itemId;
		option.label =
			folder.parentId === 0 || null ? `- ${folder.name}` : folder.name;
		return option;
	});

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
