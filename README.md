# PocketMark 리팩토링

<img width="1508" alt="스크린샷 2023-07-27 오후 2 53 25" src="https://github.com/PocketMark/PocketMark/assets/71440070/203ebc13-1c9f-45c6-97cc-6f5b158ff641">

<br/>

**데모 영상**
<img width="1508" alt="스크린샷 2023-07-27 오후 2 53 25" src="https://github.com/PocketMark/PocketMark/assets/71440070/20830eea-1bfc-45b8-9da0-df50663f2485">

2021.12 - 2022.03 에 개발했던 Pocket Mark 사이트의 프론트엔드 부분을 리팩토링 했습니다.
[리팩토링 전 레퍼지토리](https://github.com/PocketMark/PocketMark/tree/7182da96af67ec20c146df5278bf6760d0e281b8)

## Pocket Mark

기술의 발전에 따라, 웹상의 정보가 너무나 방대해지면서 좋은 질의 정보를 찾아 내는데 적지 않은 시간이 걸리게 되었습니다. 양질의 정보를 폴더별로 아카이빙하고, 태그로 분류해 정보를 정리할 수 있는 웹 사이트를 목표로 개발하였습니다.

배포 링크 : http://pocketmark.site (현재는 서버가 내려간 상태입니다.)

## 실행 방법

**서버 실행**

> cd pocketmark_backend

> Spring boot 실행

**리액트 실행**

> cd .. && cd pocketmark_frontend

> npm install

> npm start

## 리팩토링 진행 내용

### 01. 폴더 구조 수정

기존 폴더구조에서 page와 component를 분리하고 lib로 묶여있던 api관련 파일들을 따로 폴더로 만들어주었습니다.

<pre>
기존의 폴더구조
.
├── App.css
├── App.js
├── index.css
├── index.js
├── components/
├── lib/
└── modules/
</pre>

<pre>
수정한 폴더 구조
.
├── App.css
├── App.tsx
├── index.css
├── index.tsx
├── utils/        
├── apis/        
├── components/   
├── hooks/        
├── interfaces/
├── pages/
└── slices/
</pre>

### 02. TypeScript 도입

기존에 Javascript로 작성된 파일들에 TypeScript를 도입해 안정적인 코드를 작성하고자 했습니다.

### 03. React Query, Redux를 이용한 상태 관리

서버로 가져오는 데이터의 경우 **React Query**를 이용한 상태관리를 해주었고, 클라이언트 상에서 관리되는 상태의 경우 **Redux Toolkit**을 이용해 상태관리를 해주었습니다.

기존에 5분 간격으로 데이터를 업데이트 하는 로직을 사용했었는데 서버로 데이터를 보내기 전의 시간에 무슨일이 일어날지 예측하기 힘들고, 데이터 유실을 할 가능성이 있기에 위험하다고 판단하였고, 이번에 리팩토링을 할때 위의 로직을 제거하고 React Query를 이용해 즉시 데이터를 업데이트 하는 방식으로 변경해 주었습니다.

### 04. Custom Hook을 이용해 로직 분리

기존에 Main 컴포넌트에만 모든 로직들을 관리하고 Main의 자식 컴포넌트들은 UI만 관여하게 하면서 Presentational - Container 패턴과 유사하게 개발을 진행했었습니다. 당시에 다른 상태관리 툴등을 사용하지 않아 Main에서 로직을 내려주게 되면서, props drilling이 많이 발생하는 문제가있었습니다.

이번 리팩토링을 통해 Main에서 대부분의 로직들을 customHook으로 만들어 로직을 분리하고자 시도했고, Redux Toolkit, React Query등을 사용하면서 전역으로 상태를 관리해 props drilling을 줄여 유지보수하기 용이하게 만들어 주었습니다.

#### 04-01

북마크 업데이트, 폴더 업데이트, 태그 업데이트등을 useMutation을 이용해 서버 데이터를 업데이트 해주었고 이와 관련있는 queryData들은 invalidateQuery를 이용해 값들을 업데이트 시켜주었습니다. 각각 업데이트하는 로직들은 `useFolder` `useFolderData` `useTag`등의 hook으로 만들어 관리해 주었습니다.

#### 04-02

기존 프로젝트를 봤을때 폴더 수정, 북마크 수정하기 위한 데이터가 여러 컴포넌트에서 사용되는 걸 확인하였고 이를 redux로 상태를 관리하게 만들어주어 useEdit이라는 hook으로 만들고 수정여부, 수정할 데이터등을 관리해 주었습니다.

또한, 현재 선택한 폴더 데이터 또한 props drilling으로 하위 컴포넌트에서 까지 쓰이는걸 확인 후 이를 redux로 상태를 관리하게해 불필요한 props drilling을 줄이고 전역에서 사용가능하게 만들었습니다.

### 05. 성능 최적화

## 팀원

- 천진아 : 프론트엔드 총괄
- 유희선 : 백엔드
- 우재영 : 백엔드
