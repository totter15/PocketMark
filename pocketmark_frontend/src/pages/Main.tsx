import React, { useState, useRef, useEffect, useCallback } from 'react';
import { IoAddCircleOutline } from 'react-icons/io5';
import AddFolderModal from '../components/main/AddFolderModal';
import AddModal from '../components/main/AddModal';
import FolderList from '../components/main/FolderList';
import BookmarkList from '../components/main/BookmarkList';
import FolderRoute from '../components/main/FolderRoute';
import { useNavigate, useLocation } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { useQuery } from 'react-query';
import './Main.css';

import { getCookis, removeCookie, setCookie } from '../lib/cookie';
import { getAllFolder, getFolderData } from '../apis/datas';

const Main = () => {
	const [selectFolderId, setSelectFolderId] = useState(0); //선택된 폴더 아이디

	const { data } = useQuery('folder', () => getAllFolder());
	const { data: folderData } = useQuery(['folderData', selectFolderId], () =>
		getFolderData(selectFolderId)
	);

	const folders = data?.data?.folders ?? [];
	const bookmarks = folderData?.data?.bookmarks ?? [];

	//modal
	const [folderModal, setFolderModal] = useState(false); //폴더 모달
	const [addModal, setAddModal] = useState(false); //북마크 모달

	const [editFolder, setEditFolder] = useState(null);

	const [refresh, setRefresh] = useState(false);

	const navigate = useNavigate();
	const location = useLocation();

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
	const editFolderModalOpen = (itemId: any) => {
		// setEditFolder(folders.filter((f) => f.itemId === itemId));
		setFolderModal(true);
	};

	// 폴더 모달 닫기
	const folderModalClose = () => {
		setFolderModal(false);
		setEditFolder(null);
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

	//폴더 경로 표시
	const getRoute = (folderId: any) => {
		// const selectFolderData = folders?.find(
		// 	(folder) => folder.itemId === folderId
		// );
		// if (folderId === 0) return;
		// else
		// 	return selectFolderData?.parentId == 0
		// 		? ` / ${selectFolderData.name}`
		// 		: ` /
		// ${
		// 			folders?.find((folder:any) => folder?.itemId === selectFolderData.parentId)
		// 				.name
		// 		}
		//   / ${selectFolderData.name}`;
		return '';
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
					<FolderList
						folders={folders}
						folderModalOpen={folderModalOpen}
						folderSelect={(folderId: any) => setSelectFolderId(folderId)}
						selectFolder={selectFolderId}
					/>

					<section className='bookmark'>
						<FolderRoute
							route={getRoute(selectFolderId)}
							folders={folders}
							selectFolderId={selectFolderId}
							editFolderModalOpen={editFolderModalOpen}
						/>

						{/* 북마크 리스트 */}
						<BookmarkList
							bookmarks={bookmarks}
							editModalOpen={editModalOpen}
							selectFolder={selectFolderId}
						/>
					</section>
				</main>
			</div>

			{/* 폴더 추가/수정 */}
			<AddFolderModal
				folderModalClose={folderModalClose}
				open={folderModal}
				folders={folders}
				editFolder={editFolder}
			/>

			{/* 북마크 추가/수정 */}
			<AddModal
				modalClose={modalClose}
				open={addModal}
				itemId={itemId.current}
				handleId={handleId}
				selectFolder={selectFolderId}
			/>
		</>
	);
};

export default Main;
