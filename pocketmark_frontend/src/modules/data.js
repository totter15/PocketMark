import { handleActions } from "redux-actions";
import * as api from "../lib/Axios";
import createRequestThunk from "../lib/createRequestThunk";

const GET_DATA = "data/GET_DATA";
const GET_DATA_SUCCESS = "data/GET_DATA_SUCCESS";

const PUT_DATA = "data/PUT_DATA";
const PUT_DATA_SUCCESS = "data/PUT_DATA_SUCCESS";

const POST_DATA = "data/POST_DATA";
const POST_DATA_SUCCESS = "data/POST_DATA_SUCCESS";

const DELETE_DATA = "data/DELETE_DATA";
const DELETE_DATA_SUCCESS = "data/DELETE_DATA_SUCCESS";

export const getData = createRequestThunk(GET_DATA, api.PostData);
export const putData = createRequestThunk(PUT_DATA, api.PutData);
export const postData = createRequestThunk(POST_DATA, api.PostData);
export const deleteData = createRequestThunk(DELETE_DATA, api.DeleteData);

const initialState = {
  folders: [],
  bookmarks: [],
};

const data = handleActions(
  {
    [GET_DATA_SUCCESS]: (state, action) => ({
      ...state,
      folders: action.payload.data.folders,
      bookmarks: action.payload.data.bookmarks,
    }),
    [PUT_DATA_SUCCESS]: (state) => ({
      ...state,
    }),
    [POST_DATA_SUCCESS]: (state) => ({
      ...state,
    }),
    [DELETE_DATA_SUCCESS]: (state) => ({
      ...state,
    }),
  },
  initialState
);

export default data;
