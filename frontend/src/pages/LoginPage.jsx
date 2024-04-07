import React, {useState} from 'react';
import axios from "axios";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";

const LoginPage = () => {
    const [userId, setUserId] = useState(''); // 아이디
    const [password, setPassword] = useState(''); // 비밀번호
    const [cookies, setCookie] = useCookies(['accessToken']); // accessToken
    const navigate = useNavigate();

    const login = async () => {
        try {
            const res = await axios.post(
                "http://localhost:8080/api/member/login", {
                    userId,
                    password
                })
            setCookie('accessToken', res.data.accessToken,
                {path: '/', expires: new Date(res.data.expireDate)});
            navigate("/");
        } catch (error) {
            if (error?.response?.status === 401) {
                alert("아이디 혹은 비밀번호를 확인해주세요.");
            } else {
                alert("서버 오류가 발생했습니다.");
            }
        }
    }

    return (
        <div
            className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
                    Potato Chat
                </h2>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form className="space-y-6" action="#" method="POST">
                    <div>
                        <label htmlFor="email"
                               className="block text-sm font-medium leading-6 text-gray-900">
                            아이디
                        </label>
                        <div className="mt-2">
                            <input
                                type="text"
                                onChange={e => setUserId(e.target.value)}
                                className="px-3 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                            />
                        </div>
                    </div>

                    <div>
                        <div className="flex items-center justify-between">
                            <label htmlFor="password"
                                   className="block text-sm font-medium leading-6 text-gray-900">
                                비밀번호
                            </label>
                        </div>
                        <div className="mt-2">
                            <input
                                type="password"
                                onChange={e => setPassword(e.target.value)}
                                className="px-3 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                            />
                        </div>
                    </div>

                    <div>
                        <button
                            type="button"
                            onClick={login}
                            className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                        >
                            Sign in
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;