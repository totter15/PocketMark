import React, { useState } from "react";
import { Put } from "../lib/Axios";
import { putData } from "../modules/data";
import "./Mypage.css";

const Mypage = () => {
  const [prePassword, setPrePassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [newPasswordCheck, setNewPasswordCheck] = useState("");

  const onSubmit = (e) => {
    e.preventDefault();
    const data = {
      nowPw: prePassword,
      newPw: newPassword,
      conPw: newPasswordCheck,
    };
    Put("changePassword", data).then((res) => {
      console.log(res);
    });
  };

  return (
    <div className="mypage">
      <h1>Pocket mark</h1>
      <section>
        <h4>비밀번호 변경</h4>
        <h6>비밀번호는 10자리 이상으로 입력해주세요</h6>
        <div>
          <label>현재 비밀번호를 입력해주세요</label>
          <input
            value={prePassword}
            onChange={(e) => setPrePassword(e.target.value)}
          />
        </div>

        <div>
          <label>새 비밀번호를 입력해주세요</label>
          <input
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </div>

        <div>
          <label>새 비밀번호를 한번 더 입력해주세요</label>
          <input
            value={newPasswordCheck}
            onChange={(e) => setNewPasswordCheck(e.target.value)}
          />
        </div>

        <button onClick={onSubmit}>변경하기</button>
      </section>
    </div>
  );
};

export default Mypage;
