import { createSlice } from '@reduxjs/toolkit';

export interface EditStateType {
	isEditBookmark: boolean;
	isEditFolder: boolean;
	editData: any;
}

const initialState: EditStateType = {
	isEditBookmark: false,
	isEditFolder: false,
	editData: null,
};

export const editSlice = createSlice({
	name: 'editData',
	initialState,
	reducers: {
		editBookmark: (state, { payload }) => {
			state.isEditBookmark = true;
			state.editData = payload;
		},
		editFolder: (state, { payload }) => {
			state.isEditFolder = true;
			state.editData = payload;
		},
		editDone: (state) => {
			state.isEditFolder = false;
			state.isEditBookmark = false;
			state.editData = null;
		},
	},
});

export const { editBookmark, editFolder, editDone } = editSlice.actions;
export default editSlice.reducer;
