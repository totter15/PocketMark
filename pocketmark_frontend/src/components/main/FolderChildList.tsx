import React from 'react';
import FolderListItem from './FolderListItem';

const FolderChildList = ({ childFolder, selectFolder, folderSelect }: any) => {
	return childFolder.map((folder: any) => (
		<FolderListItem
			key={folder.itemId}
			id={folder.itemId}
			name={folder.name}
			folderSelect={folderSelect}
			select={folder.itemId === selectFolder}
			child
		/>
	));
};

export default FolderChildList;
