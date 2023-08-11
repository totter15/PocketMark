import { useMutation, useQueryClient } from 'react-query';
import { createData, deleteData, editData } from '../apis/datas';
import { FolderType } from '../interfaces/data';

function useFolder() {
	const queryClient = useQueryClient();

	function onSuccess() {
		queryClient.invalidateQueries('folder');
	}

	const addFolder = useMutation(
		(folder: FolderType) => createData({ folders: [folder], bookmarks: [] }),
		{
			onSuccess,
		}
	);
	const editFolder = useMutation(
		(folder: FolderType) => editData({ folders: [folder], bookmarks: [] }),
		{
			onSuccess,
		}
	);
	const deleteFolder = useMutation(
		(folderId: number) =>
			deleteData({ folderIdList: [folderId], bookmarkIdList: [] }),
		{
			onSuccess,
		}
	);

	return { addFolder, editFolder, deleteFolder };
}

export default useFolder;
