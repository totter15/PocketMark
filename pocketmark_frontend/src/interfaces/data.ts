export interface ResponseDataType {
	data: DataType;
	errorCode: number;
	message: string;
	success: boolean;
}

export interface ResponseFolderType {
	data: {
		folders: FolderType[];
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
	url: string;
	tags?: TagType[];
	visitCount?: number;
}

export interface TagType {
	name: string;
}

export interface FolderType {
	itemId: number;
	name: string;
	parentId: number | null;
	tags?: TagType[];
	visitCount?: number;
}

export interface FolderDataType {
	bookmarks: BookmarkType[];
	folders: FolderType[];
}

export interface DeleteDataType {
	bookmarkIdList: number[];
	folderIdList: number[];
}
