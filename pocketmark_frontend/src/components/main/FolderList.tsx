import React from 'react';
import FolderListItem from './FolderListItem';
import FolderChildList from './FolderChildList';
import { FiPlus } from 'react-icons/fi';
import './FolderList.css';
import useCurrentFolder from '../../hooks/useCurrentFolder';
import { FolderType } from '../../interfaces/data';

const FolderList = ({ folders, folderModalOpen }: any) => {
	const { selectCurrentFolder, currentFolder } = useCurrentFolder();

	return (
		<div className='folderList'>
			<div
				className={
					currentFolder.itemId === 0 ? 'addFolder select' : 'addFolder'
				}
				onClick={() => selectCurrentFolder(folders[0])}
			>
				<div>Root</div>
				<button aria-label='addFolderButton' onClick={folderModalOpen}>
					<FiPlus />
				</button>
			</div>

			{folders.length > 0 &&
				folders.map((folder: FolderType) => {
					if (folder.parentId === 0)
						return (
							<div key={folder.itemId}>
								<FolderListItem
									name={folder.name}
									folderSelect={() => selectCurrentFolder(folder)}
									isSelect={folder.itemId === currentFolder.itemId}
								/>
								<FolderChildList
									childFolder={folders.filter(
										(f: any) => f.parentId === folder.itemId
									)}
								/>
							</div>
						);
				})}
		</div>
	);
};

export default FolderList;
