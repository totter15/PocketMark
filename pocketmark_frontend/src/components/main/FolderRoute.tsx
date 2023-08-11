import React from 'react';
import { FiEdit3 } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';
import './FolderRoute.css';
import useEdit from '../../hooks/useEdit';

const FolderRoute = ({
	route,
	folders,
	selectFolderId,
	editFolderModalOpen,
	deleteFolders,
}: any) => {
	const selectFolder = folders.find((f: any) => f.itemId === selectFolderId);
	const { editFolderHandler } = useEdit();

	return (
		<div className='route'>
			<div>
				내 폴더 {route}
				<div className='folderTags'>
					{selectFolder &&
						selectFolder.tags &&
						selectFolder.tags.map((t: any) => (
							<div key={`${t.name}`} className='tag'>
								#{t.name}
							</div>
						))}
				</div>
			</div>
			{selectFolderId !== 0 && (
				<div className='icon'>
					<button
						onClick={() => {
							editFolderHandler(selectFolder);
							editFolderModalOpen();
						}}
					>
						<FiEdit3 />
					</button>
					<button onClick={() => deleteFolders(selectFolderId)}>
						<RiDeleteBin6Line />
					</button>
				</div>
			)}
		</div>
	);
};

export default FolderRoute;
