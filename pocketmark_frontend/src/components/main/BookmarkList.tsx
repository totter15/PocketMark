import React from 'react';
import BookmarkListItem from './BookmarkListItem';
import './BookmarkList.css';

const BookmarkList = ({ bookmarks, editModalOpen }: any) => {
	return (
		<div className='bookmarkList'>
			<div className='contents'>
				{bookmarks &&
					bookmarks.map((bookmark: any, index: any) => (
						<BookmarkListItem
							key={`${index}_${bookmark.name}`}
							bookmark={bookmark}
							editModalOpen={editModalOpen}
						/>
					))}
			</div>
		</div>
	);
};

export default BookmarkList;
