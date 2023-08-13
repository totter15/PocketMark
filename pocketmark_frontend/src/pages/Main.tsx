import React, { useState, useRef } from 'react';
import { IoAddCircleOutline } from 'react-icons/io5';
import AddFolderModal from '../components/main/AddFolderModal';
import AddModal from '../components/main/AddModal';
import FolderList from '../components/main/FolderList';
import BookmarkList from '../components/main/BookmarkList';
import FolderRoute from '../components/main/FolderRoute';
import { useNavigate } from 'react-router-dom';
import { useQuery } from 'react-query';
import './Main.css';

import { getCookis, removeCookie, setCookie } from '../lib/cookie';
import { getAllFolder, getFolderData } from '../apis/datas';
import useCurrentFolder from '../hooks/useCurrentFolder';

const Main = () => {
	const { currentFolder } = useCurrentFolder();
	const { data: folder } = useQuery('folder', async () => await getAllFolder());
	const { data: folderData } = useQuery(
		['folderData', currentFolder.itemId],
		async () => await getFolderData(currentFolder.itemId)
	);

	const folders = folder?.data?.folders ?? [];
	const bookmarks = folderData?.data?.bookmarks ?? [];

	//modal
	const [folderModal, setFolderModal] = useState(false); //폴더 모달
	const [addModal, setAddModal] = useState(false); //북마크 모달

	const navigate = useNavigate();

	const itemId = useRef(Number(getCookis('lastId')) + 1);
	function handleId() {
		setCookie('itemId', itemId.current + 1);
		itemId.current = itemId.current + 1;
	}

	// 폴더 모달 열기
	const folderModalOpen = () => {
		setFolderModal(true);
	};

	//폴더 수정 모달 열기
	const editFolderModalOpen = () => {
		setFolderModal(true);
	};

	// 폴더 모달 닫기
	const folderModalClose = () => {
		setFolderModal(false);
	};

	//북마크 모달 열기
	const modalOpen = () => {
		setAddModal(true);
	};

	// 북마크 수정 모달 열기
	const editModalOpen = () => {
		setAddModal(true);
	};

	// 북마크 모달 닫기
	const modalClose = () => {
		setAddModal(false);
	};

	const onLogout = () => {
		removeCookie('autoLogin');

		setTimeout(() => {
			navigate('/');
		}, 500);
	};

	return (
		<>
			<div className='main'>
				<header>
					<div className='search'>
						<h2>PocketMark</h2>

						<button onClick={modalOpen}>
							<IoAddCircleOutline />
						</button>
					</div>

					<div className='nav'>
						<div onClick={onLogout} className='logout'>
							Logout
						</div>
					</div>
				</header>

				<main>
					{/* 폴더 네비게이션 */}
					<FolderList folders={folders} folderModalOpen={folderModalOpen} />

					<section className='bookmark'>
						<FolderRoute
							folders={folders}
							selectFolderId={currentFolder.itemId}
							editFolderModalOpen={editFolderModalOpen}
						/>

						{/* 북마크 리스트 */}
						<BookmarkList bookmarks={bookmarks} editModalOpen={editModalOpen} />
					</section>
				</main>
			</div>

			{/* 폴더 추가/수정 */}
			<AddFolderModal
				folderModalClose={folderModalClose}
				open={folderModal}
				itemId={itemId.current}
				handleId={handleId}
			/>

			{/* 북마크 추가/수정 */}
			<AddModal
				modalClose={modalClose}
				open={addModal}
				itemId={itemId.current}
				handleId={handleId}
			/>
		</>
	);
};

export default Main;
