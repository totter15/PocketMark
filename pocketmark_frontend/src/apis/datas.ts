import {
	BookmarkType,
	FolderType,
	ResponseDataType,
	ResponseFolderType,
} from '../interfaces/data';
import client from './client';

export async function getAllData(): Promise<ResponseDataType> {
	const { data } = await client.get('api/v1/data-all');
	return data;
}

export async function getFolderData(
	folderId: number
): Promise<ResponseDataType> {
	const { data } = await client.get(
		`api/v1/data?folder-id=${folderId}&pageSize=100`
	);
	return data;
}

export async function getAllForder(): Promise<ResponseFolderType> {
	const { data } = await client.get('api/v1/folder');
	return data;
}

export async function editData(datas: {
	bookmarks: BookmarkType;
	folders: FolderType;
}) {
	const { data } = await client.put('api/v1/data', datas);
	return data;
}

export async function postData(datas: {
	bookmarks: BookmarkType;
	folders: FolderType;
}) {
	const { data } = await client.post('api/v1/data', datas);
	return data;
}

export async function deleteData(datas: {
	bookmarkIdList: number[];
	folderIdList: number[];
}) {
	const { data } = await client.put('api/v1/data/delete', datas);
	return data;
}
