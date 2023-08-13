import { TagResponseType } from '../interfaces/tag';
import client from './client';

export async function createTag(tags: TagResponseType) {
	const { data } = await client.post('api/v1/tag', tags);
	console.log(data);
	return data;
}

export async function deleteTag(tags: TagResponseType) {
	const { data } = await client.put('api/v1/tag/delete', tags);
	console.log(data);

	return data;
}
