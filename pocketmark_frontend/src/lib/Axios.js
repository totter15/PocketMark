import axios from "axios";

const headers = {
  "Content-Type": "application/json",
};

const Post = async (get, data) => {
  try {
    const response = await axios.post(
      `http://localhost:9090/api/v1/${get}`,
      JSON.stringify(data),
      {
        headers: headers,
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
export { Post };
