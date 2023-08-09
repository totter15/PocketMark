import React from 'react';
import { FiEdit3 } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';
import './BookmarkListItem.css';
import useEdit from '../../hooks/useEdit';
import useFolderData from '../../hooks/useFolderData';

const BookmarkListItem = ({ bookmark, editModalOpen }: any) => {
	const { editBookmarkHandler } = useEdit();
	const { deleteFolderData } = useFolderData(bookmark.parentId);

	function editHandler() {
		editBookmarkHandler(bookmark);
		editModalOpen();
	}

	function deleteHandler() {
		deleteFolderData.mutate({
			folderIdList: [],
			bookmarkIdList: [bookmark.itemId],
		});
	}

	return (
		<div className='bookmarkListItem'>
			<div className='edit' onClick={deleteHandler}>
				<RiDeleteBin6Line
					style={{
						position: 'absolute',
						right: 20,
						color: 'lightgray',
						width: '18px',
						height: '18px',
					}}
				/>
			</div>
			<div className='edit' onClick={editHandler}>
				<FiEdit3
					style={{
						position: 'absolute',
						right: 50,
						color: 'lightgray',
						width: '18px',
						height: '18px',
					}}
				/>
			</div>

			<a href={bookmark.url} target={'_blank'} rel='noreferrer'>
				<div className='name'>{bookmark.name}</div>
				<div className='comment'>{bookmark.comment}</div>

				<div className='url'>{bookmark.url}</div>
			</a>

			<div style={{ flexDirection: 'row', display: 'flex' }}>
				{bookmark.tags &&
					bookmark.tags.map((tag: any) => (
						<div className='tag' key={tag.name} style={{ marginRight: 8 }}>
							#{tag.name}
						</div>
					))}
			</div>
		</div>
	);
};

export default BookmarkListItem;
