import { configureStore } from '@reduxjs/toolkit';
import editDataReducer from './editData';

export const store = configureStore({
	reducer: {
		editData: editDataReducer,
	},
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
