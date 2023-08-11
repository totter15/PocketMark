import { useEffect, useState } from 'react';
import { useQuery } from 'react-query';
import { getAllFolder } from '../apis/datas';

function useBookmarkModalFolder(selectFolderId: number) {
	const { data } = useQuery('folders', getAllFolder);
	const folders = data?.data.folders ?? [];

	const [folderSelect, setFolderSelect] = useState({
		label: '내 폴더',
		value: 0,
	});

	function folderSelectHandler(option: any) {
		setFolderSelect(option);
	}

	const folderOptions = folders.map((folder: any) => {
		const option: any = {};
		option.value = folder.itemId;
		option.label = folder.name;
		return option;
	});

	useEffect(() => {
		selectFolderId &&
			setFolderSelect(
				folderOptions.find((o: any) => o.value === selectFolderId)
			);
	}, []);

	return { folderSelect, folderOptions, folderSelectHandler };
}

export default useBookmarkModalFolder;
