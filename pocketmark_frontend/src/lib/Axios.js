import axios from "axios";
import { getCookis } from "./cookie";

const headers = {
  "Content-Type": "application/json",
};

const testUrl = "http://localhost:9090";
const awsUrl = "http://back.pocketmark.site:9090";

axios.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    if (error.request.status === 403) {
      const res = Post("refresh-token", {
        refreshToken: getCookis("refreshToken"),
      })
        .then((res) => {
          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${res.data.data.accessToken}`;
        })
        .then(() => axios(error.config));
      return res;
    }
    return Promise.reject(error);
  }
);

const Post = async (post, data) => {
  try {
    const response = await axios.post(
      `${awsUrl}/api/v1/${post}`,
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
  } catch (e) {}
};

const PostData = async (data) => {
  try {
    const response = await axios.post(`${awsUrl}/api/v1/data`, data, {
      headers: headers,
    });
    return response;
  } catch (e) {}
};

const PutData = async (data) => {
  try {
    const response = await axios.put(`${awsUrl}/api/v1/data`, data, {
      headers: headers,
    });
    return response;
  } catch (e) {}
};

const DeleteData = async (data) => {
  try {
    const response = await axios.put(`${awsUrl}/api/v1/data/delete`, data, {
      headers: headers,
    });
    return response;
  } catch (e) {}
};

const GetData = async (folderId) => {
  try {
    const response = await axios.get(
      `${awsUrl}/api/v1/data?folder-id=${folderId}&size=100&sort=itemId`,
      {
        headers: headers,
      }
    );
    return response;
  } catch (e) {}
};

const GetAllFolders = async () => {
  try {
    const response = await axios.get(`${awsUrl}/api/v1/folder`, {
      headers: headers,
    });
    return response;
  } catch (e) {}
};

const PostTag = async (data) => {
  try {
    const response = await axios.post(`${awsUrl}/api/v1/tag`, data, {
      headers: headers,
    });
    return response;
  } catch (e) {}
};

const DelTag = async (data) => {
  try {
    const response = await axios.put(`${awsUrl}/api/v1/tag/delete`, data, {
      headers: headers,
    });
    return response;
  } catch (e) {}
};

export {
  Post,
  PostData,
  PutData,
  DeleteData,
  GetData,
  GetAllFolders,
  PostTag,
  DelTag,
};
