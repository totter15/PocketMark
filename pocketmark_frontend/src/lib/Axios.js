import axios from "axios";
import { getCookis } from "./cookie";

const headers = {
  "Content-Type": "application/json",
  Authorization: `Bearer ${getCookis("myToken")}`,
};

const Post = async (post, data) => {
  try {
    const response = await axios.post(
      `http://localhost:9090/api/v1/${post}`,
      JSON.stringify(data),
      {
        headers: {
          "Content-Type": "application/json",
        },
        validateStatus: function (status) {
          // 상태 코드가 500 이상일 경우 거부. 나머지(500보다 작은)는 허용.
          return status < 500;
        },
      }
    );
    return response;
  } catch (e) {
    return e;
  }
};

const PostData = async (data) => {
  try {
    const response = await axios.post(
      `http://localhost:9090/api/v1/data`,
      data,
      { headers: headers }
    );
    return response;
  } catch (e) {
    return e;
  }
};

const PutData = async (data) => {
  try {
    const response = await axios.put(
      `http://localhost:9090/api/v1/data`,
      data,
      { headers: headers }
    );
    return response;
  } catch (e) {
    return e;
  }
};

const DeleteData = async (data) => {
  try {
    const response = await axios.put(
      `http://localhost:9090/api/v1/data/delete`,
      data,
      { headers: headers }
    );
    return response;
  } catch (e) {
    return e;
  }
};

const GetData = async (folderId) => {
  try {
    const response = await axios.get(
      `http://localhost:9090/api/v1/data?folder-id=${folderId}&size=100&sort=name`,
      {
        headers: headers,
      }
    );
    return response;
  } catch (e) {
    return e;
  }
};

const GetAllFolders = async () => {
  try {
    const response = await axios.get("http://localhost:9090/api/v1/folder", {
      headers: headers,
    });
    return response;
  } catch (e) {
    return e;
  }
};

export { Post, PostData, PutData, DeleteData, GetData, GetAllFolders };
