import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Login.css";
import { Post } from "../lib/Axios";
import { setCookie } from "../lib/cookie";

const Login = () => {
  const [input, setInput] = useState({
    email: "",
    pw: "",
  });

  const navigate = useNavigate();

  const onChange = (e) => {
    const { name, value } = e.target;
    setInput({
      ...input,
      [name]: value,
    });
  };

  const onSubmit = async (e) => {
    e.preventDefault();

    const data = {
      email: input.email,
      pw: input.pw,
    };

    Post("login", data).then((res) => {
      if (res.data.success && res.data.data.tokenBox.accessToken) {
        setCookie("myToken", res.data.data.tokenBox.accessToken);
        setCookie("refreshToken", res.data.data.tokenBox.refreshToken);
        setCookie("lastId", res.data.data.itemId);
        navigate("/main");
      }
    });
  };

  return (
    <div className="login">
      <div className="title">Pocket Mark</div>
      <form onSubmit={onSubmit}>
        <div className="id">
          <label>아이디</label>
          <input
            name="email"
            type={"email"}
            value={input.email}
            placeholder="아이디를 입력해주세요."
            onChange={onChange}
          />
        </div>
        <div className="password">
          <label>비밀번호</label>
          <input
            name="pw"
            type={"password"}
            value={input.pw}
            placeholder="비밀번호를 입력해주세요."
            onChange={onChange}
          />
        </div>
        <button>로그인</button>
      </form>
      <div className="regist">
        아직 회원이 아니세요?
        <Link className="link" to="/regist">
          회원가입 하러가기
        </Link>
      </div>
    </div>
  );
};

export default Login;
