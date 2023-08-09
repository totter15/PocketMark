import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../slices';
import { editBookmark, editDone, editFolder } from '../slices/editData';
import { BookmarkType, FolderDataType } from '../interfaces/data';

function useEdit() {
	const isEditBookmark = useSelector(
		(state: RootState) => state.editData.isEditBookmark
	);
	const isEditFolder = useSelector(
		(state: RootState) => state.editData.isEditFolder
	);
	const editData = useSelector((state: RootState) => state.editData.editData);

	const dispatch = useDispatch();

	function editBookmarkHandler(editBookmarkData: BookmarkType) {
		dispatch(editBookmark(editBookmarkData));
	}

	function editFolderHandler(editFolderData: FolderDataType) {
		dispatch(editFolder(editFolderData));
	}

	function editDoneHandler() {
		dispatch(editDone());
	}

	return {
		isEditBookmark,
		isEditFolder,
		editData,
		editBookmarkHandler,
		editFolderHandler,
		editDoneHandler,
	};
}

export default useEdit;
