import { useMutation, useQueryClient } from 'react-query';
import { createData, deleteData, editData, getFolderData } from '../apis/datas';
import { DeleteDataType, FolderDataType } from '../interfaces/data';

function useFolderData(folderId: number) {
	const queryClient = useQueryClient();

	function onSuccess() {
		queryClient.invalidateQueries(['folderData', folderId]);
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
