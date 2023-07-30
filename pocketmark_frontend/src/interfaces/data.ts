export interface ResponseDataType {
	data: DataType;
	errorCode: number;
	message: string;
	success: boolean;
}

export interface ResponseFolderType {
	data: {
		folders: FolderType;
	};
	errorCode: number;
	message: string;
	success: boolean;
}

export interface DataType {
	bookmarks: BookmarkType[];
	folders: FolderType[];
	targetId: number;
}

export interface BookmarkType {
	comment: string;
	itemId: number;
	name: string;
	parentId: number;
	tags?: TagType[];
	url: string;
	visitCount?: number;
}

export interface TagType {
	name: string;
}

export interface FolderType {
	itemId: number;
	name: string;
	parentId: number;
	tags?: TagType[];
	visitCount?: number;
}
