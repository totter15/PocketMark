import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../slices';
import { FolderType } from '../interfaces/data';
import { select } from '../slices/currentFolder';

function useCurrentFolder() {
	const currentFolder = useSelector((state: RootState) => state.currentFolder);

	const dispatch = useDispatch();

	function selectCurrentFolder(folder: FolderType) {
		dispatch(select(folder));
	}

	return { currentFolder, selectCurrentFolder };
}

export default useCurrentFolder;
