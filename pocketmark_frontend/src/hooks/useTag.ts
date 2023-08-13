import { useMutation, useQueryClient } from 'react-query';
import { createTag, deleteTag } from '../apis/tags';
import { TagType } from '../interfaces/tag';
import useCurrentFolder from './useCurrentFolder';

function useTag() {
	const queryClient = useQueryClient();
	const { currentFolder, selectCurrentFolder } = useCurrentFolder();

	function onSuccess() {
		queryClient.invalidateQueries(['folderData', currentFolder.itemId]);
	}

	function onSuccessFolder() {
		queryClient.invalidateQueries(['folder']);
		//TODO : currentFolder 수정하기
	}

	const addBookmarkTagHandler = useMutation(
		async (tags: TagType[]) => await createTag({ tags }),
		{ onSuccess }
	);
	const deleteBookmarkTagHandler = useMutation(
		async (tags: TagType[]) => await deleteTag({ tags }),
		{ onSuccess }
	);

	const addFolderTagHandler = useMutation(
		async (tags: TagType[]) => await createTag({ tags }),
		{ onSuccess: onSuccessFolder }
	);
	const deleteFolderTagHandler = useMutation(
		async (tags: TagType[]) => await deleteTag({ tags }),
		{ onSuccess: onSuccessFolder }
	);

	return {
		addBookmarkTagHandler,
		deleteBookmarkTagHandler,
		addFolderTagHandler,
		deleteFolderTagHandler,
	};
}

export default useTag;
