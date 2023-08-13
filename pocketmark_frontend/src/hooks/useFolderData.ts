import { useMutation, useQueryClient } from 'react-query';
import { createData, deleteData, editData } from '../apis/datas';
import { DeleteDataType, FolderDataType } from '../interfaces/data';
import useCurrentFolder from './useCurrentFolder';

function useFolderData() {
	const queryClient = useQueryClient();
	const { currentFolder } = useCurrentFolder();

	function onSuccess() {
		queryClient.invalidateQueries(['folderData', currentFolder.itemId]);
	}

	const addFolderData = useMutation(
		(data: FolderDataType) => createData(data),
		{ onSuccess }
	);
	const editFolderData = useMutation(
		async (data: FolderDataType) => await editData(data),
		{
			onSuccess,
		}
	);
	const deleteFolderData = useMutation(
		(data: DeleteDataType) => deleteData(data),
		{ onSuccess }
	);

	return {
		addFolderData,
		editFolderData,
		deleteFolderData,
	};
}

export default useFolderData;
