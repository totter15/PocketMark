import {
	DeleteDataType,
	FolderDataType,
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
		`api/v1/data?folder-id=${folderId}&size=100&sort=itemId`
	);
	return data;
}

export async function getAllFolder(): Promise<ResponseFolderType> {
	const { data } = await client.get('api/v1/folder');
	return data;
}

export async function editData(datas: FolderDataType) {
	const { data } = await client.put('api/v1/data', datas);
	return data;
}

export async function createData(datas: FolderDataType) {
	const { data } = await client.post('api/v1/data', datas);
	return data;
}

export async function deleteData(datas: DeleteDataType) {
	const { data } = await client.put('api/v1/data/delete', datas);
	return data;
}
