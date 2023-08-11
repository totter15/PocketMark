import { useRef, useCallback, useState } from 'react';
import { BookmarkType, FolderType } from '../interfaces/data';
import {
	createData,
	deleteData,
	editData,
	getAllFolder,
	getFolderData,
} from '../apis/datas';
import { createTag, deleteTag } from '../apis/tags';

function useServerRequestSave() {
	const [folders, setFolders] = useState<FolderType[]>([]);
	const [bookmarks, setBookmarks] = useState<BookmarkType[]>([]);

	const postData = {
		folders: [],
		bookmarks: [],
	};
	const putData = {
		folders: [],
		bookmarks: [],
	};
	const delData = {
		folderIdList: [],
		bookmarkIdList: [],
	};
	const postTagData = {
		tags: [],
	};
	const delTagData = {
		tags: [],
	};

	async function getData(folderId: number) {
		const { data: folderData } = await getAllFolder();
		const { data: bookmarkData } = await getFolderData(folderId);

		setFolders(folderData.folders);
		setBookmarks(bookmarkData.bookmarks);
	}

	async function post() {
		if (!!postData.folders.length || !!postData.bookmarks.length) {
			await createData(postData);
		}
	}

	async function put() {
		if (!!putData.folders.length || !!putData.bookmarks.length) {
			await editData(putData);
		}
	}

	async function del() {
		if (!!delData.folderIdList.length || !!delData.bookmarkIdList.length) {
			await deleteData(delData);
		}
	}

	async function postTag() {
		if (!!postTagData.tags.length) {
			await createTag(postTagData);
		}
	}

	async function delTag() {
		if (!!delTagData.tags.length) {
			await deleteTag(delTagData);
		}
	}

	function reset() {
		postData.folders = [];
		postData.bookmarks = [];

		putData.folders = [];
		putData.folders = [];

		delData.folderIdList = [];
		delData.bookmarkIdList = [];

		postTagData.tags = [];
		delTagData.tags = [];
	}

	async function serverRequest(folderId: number = 0) {
		try {
			await post();
			await put();
			await del();
			await postTag();
			await delTag();

			await getData(folderId);
		} catch (e) {
			console.log(e);
		} finally {
			reset();
		}
	}

	return { folders, bookmarks, serverRequest };
}

export default useServerRequestSave;
