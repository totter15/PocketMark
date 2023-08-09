import React from 'react';
import './FolderListItem.css';

const FolderListItem = ({ name, child, folderSelect, id, select }: any) => {
	return (
		<div
			className={select ? 'select folderListItem' : 'folderListItem'}
			onClick={() => !select && folderSelect(id)}
		>
			{child ? ` - ${name}` : name}
		</div>
	);
};

export default FolderListItem;
