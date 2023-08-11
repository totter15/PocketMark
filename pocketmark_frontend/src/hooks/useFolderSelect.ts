import { useState } from 'react';
import { FolderSelectType } from '../components/main/FolderSelect';

function useFolderSelect() {
	const initialSelectFolder = {
		label: 'Root',
		value: 0,
	};
	const [selectFolder, setSelectFolder] =
		useState<FolderSelectType>(initialSelectFolder);

	function selectHandler(option: FolderSelectType) {
		setSelectFolder(option);
	}

	return { selectFolder, selectHandler, initialSelectFolder };
}

export default useFolderSelect;
