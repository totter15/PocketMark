# PocketMark 리팩토링

<img width="1508" alt="스크린샷 2023-07-27 오후 2 53 25" src="https://github.com/PocketMark/PocketMark/assets/71440070/203ebc13-1c9f-45c6-97cc-6f5b158ff641">

<br/>

**데모 영상**
<img width="1508" alt="스크린샷 2023-07-27 오후 2 53 25" src="https://github.com/PocketMark/PocketMark/assets/71440070/8d521f28-f2b3-40b0-b078-5ea6781f33da">

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

### 05. 성능 개선

LightHouse를 이용해 성능을 측정하고 이를 개선 하는 과정을 거쳤습니다.

**성능 개선 전**
<img width="795" alt="스크린샷 2023-08-18 오후 6 51 28" src="https://github.com/TOO-IT/Tooit-frontend/assets/71440070/a469514f-12d4-4a3c-ad8d-5873bd5882e5">

**성능 개선 후**
<img width="776" alt="스크린샷 2023-08-18 오후 10 47 22" src="https://github.com/TOO-IT/Tooit-frontend/assets/71440070/11d17f71-08d0-4eb4-940c-f4d6bef2d15e">

#### 05-01 코드 분할

```javascript
const Login = lazy(() => import('./pages/Login'));
const Regist = lazy(() => import('./pages/Regist'));
const RegistOk = lazy(() => import('./pages/RegistOk'));
const Main = lazy(() => import('./pages/Main'));

function App() {
	const queryClient = new QueryClient();

	return (
		<Suspense fallback={<div>로딩중...</div>}>
			<Provider store={store}>
				<QueryClientProvider client={queryClient}>
					<Routes>
						<Route path='/' element={<Login />} />
						<Route path='/regist' element={<Regist />} />
						<Route path='/registOk' element={<RegistOk />} />
						<Route path='/main' element={<Main />} />
					</Routes>
				</QueryClientProvider>
			</Provider>
		</Suspense>
	);
}
```

리액트의 Suspense와 lazy 함수를 이용해 페이지별로 동적 import를 활용히여 코드를 분할하였습니다. 이를 통해 기존에 bundle.js를 가져오는데 138.28ms에서 67.51ms로 더 빠르게 파일을 로드할 수 있었습니다.

<img width="578" alt="스크린샷 2023-08-18 오후 10 53 48" src="https://github.com/TOO-IT/Tooit-frontend/assets/71440070/709aa793-adee-4964-977b-5147f9d0f6a5">

<img width="757" alt="스크린샷 2023-08-18 오후 10 54 16" src="https://github.com/TOO-IT/Tooit-frontend/assets/71440070/608f0548-795e-490b-9758-496a1565748b">

#### 05-02 Reduce unused Javascript 개선

<img width="687" alt="스크린샷 2023-08-18 오후 10 38 21" src="https://github.com/TOO-IT/Tooit-frontend/assets/71440070/cef991a2-ff6d-4e12-9082-1fe0b3e51da3">

다음으로 Reduce unused JavaScript라는 메세지를 확인후 react-icons에서 불필요하게 많은 파일이 로드되고 있는걸 확인했습니다. react-icons를 사용한 코드를 확인하니 아래와 같이 한 종류의 아이콘이 아니라 다양한 종류의 아이콘을 불러오는 것을 볼수 있었습니다. 이를 'react-icons/fi'의 아이콘만 사용하는 걸로 변경하고 나니 performance부분에서 높은 점수를 얻을 수 있었습니다.

```javascript
import { FiPlus } from 'react-icons/fi';
import { BiBadgeCheck } from 'react-icons/bi';
```

#### 05-03 접근성 개선

기존에 button 안에는 text가 아닌 img가 들어있었는데 이부분 때문에 텍스트리더가 버튼에 대해 읽을 수가 없어서 lighthouse에서 접근성에 대해 낮은 점수를 받게 되엇습니다. button에 aria-label 속성을 추가해 접근성 점수를 높일 수 있었습니다.
