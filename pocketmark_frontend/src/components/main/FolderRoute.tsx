import React from 'react';
import { FiEdit3 } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';
import './FolderRoute.css';
import useEdit from '../../hooks/useEdit';
import useFolder from '../../hooks/useFolder';
import { useQueryClient } from 'react-query';
import { ResponseFolderType } from '../../interfaces/data';
import useCurrentFolder from '../../hooks/useCurrentFolder';
import { initialState } from '../../slices/currentFolder';

const FolderRoute = ({ editFolderModalOpen }: any) => {
	const queryClient = useQueryClient();
	const data = queryClient.getQueryData('folder') as ResponseFolderType;
	const folders = data?.data.folders ?? [];

	const { currentFolder, selectCurrentFolder } = useCurrentFolder();
	const { editFolderHandler } = useEdit();
	const { deleteFolder } = useFolder();

	const route = () => {
		const route: string[] = ['Root'];
		const parentFolder = folders.find(
			(folder) => folder.itemId === currentFolder.parentId
		);

		if (currentFolder.itemId !== 0) {
			parentFolder &&
				parentFolder.itemId !== 0 &&
				route.push(parentFolder.name);
			route.push(currentFolder.name);
		}

		return route.join(' / ');
	};

	return (
		<div className='route'>
			<div>
				{route()}
				<div className='folderTags'>
					{/* {selectFolder &&
						selectFolder.tags &&
						selectFolder.tags.map((t: any) => (
							<div key={`${t.name}`} className='tag'>
								#{t.name}
							</div>
						))} */}
				</div>
			</div>
			{currentFolder.itemId !== 0 && (
				<div className='icon'>
					<button
						onClick={() => {
							editFolderHandler(currentFolder);
							editFolderModalOpen();
						}}
					>
						<FiEdit3 />
					</button>
					<button
						onClick={() => {
							deleteFolder.mutate(currentFolder.itemId);
							selectCurrentFolder(initialState);
						}}
					>
						<RiDeleteBin6Line />
					</button>
				</div>
			)}
		</div>
	);
};

export default FolderRoute;
