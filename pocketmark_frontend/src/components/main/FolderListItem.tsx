import React from 'react';
import './FolderListItem.css';

const FolderListItem = ({ name, child, folderSelect, isSelect }: any) => {
	return (
		<div
			className={isSelect ? 'select folderListItem' : 'folderListItem'}
			onClick={folderSelect}
		>
			{child ? ` - ${name}` : name}
		</div>
	);
};

export default FolderListItem;
