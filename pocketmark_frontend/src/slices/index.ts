import { configureStore } from '@reduxjs/toolkit';
import editDataReducer from './editData';
import currentFolderReducer from './currentFolder';

export const store = configureStore({
	reducer: {
		editData: editDataReducer,
		currentFolder: currentFolderReducer,
	},
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
