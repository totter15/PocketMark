import React from 'react';
import FolderListItem from './FolderListItem';
import useCurrentFolder from '../../hooks/useCurrentFolder';
import { FolderType } from '../../interfaces/data';

const FolderChildList = ({ childFolder }: { childFolder: FolderType[] }) => {
	const { currentFolder, selectCurrentFolder } = useCurrentFolder();

	return childFolder.map((folder: any) => (
		<FolderListItem
			key={folder.itemId}
			id={folder.itemId}
			name={folder.name}
			folderSelect={() => selectCurrentFolder(folder)}
			isSelect={folder.itemId === currentFolder.itemId}
			child
		/>
	));
};

export default FolderChildList;
