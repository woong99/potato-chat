import React, {useEffect, useState} from 'react';
import {AiOutlineCaretUp} from "react-icons/ai";
import axios from "axios";
import {useCookies} from "react-cookie";

const MainPage = () => {
    const [members, setMembers] = useState([]);
    const [cookies] = useCookies(['accessToken']);

    useEffect(() => {
        fetchMembers().then(data => setMembers(data));
    }, []);

    // 사용자 목록 조회
    const fetchMembers = async () => {
        try {
            const response = await axios.get(
                'http://localhost:8080/api/member/list',
                {headers: {Authorization: `Bearer ${cookies['accessToken']}`}});
            return response.data;
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div
            className="h-screen overflow-hidden flex items-center justify-center"
            style={{background: '#edf2f7'}}>
            <div className="container mx-auto shadow-lg rounded-lg h-full">
                {/* Header */}
                <div
                    className="px-5 py-5 flex justify-between items-center bg-white border-b-2">
                    <div className="font-semibold text-2xl">Potato Chat</div>
                    <div
                        className="h-12 w-12 p-2 bg-yellow-500 rounded-full text-white font-semibold flex items-center justify-center">RA
                    </div>
                </div>
                {/* Chatting */}
                <div className="flex flex-row justify-between bg-white h-full">
                    {/* Chat list */}
                    <div
                        className="flex flex-col w-2/5 border-r-2 overflow-y-auto">
                        {/* search component */}
                        <div className="border-b-2 py-4 px-2">
                            <input
                                type="text"
                                placeholder="search chatting"
                                className="py-2 px-2 border-2 border-gray-200 rounded-2xl w-full"
                            />
                        </div>
                        {/* user list */}
                        {members.map(member => (
                            <div key={member.userId}
                                 className="flex flex-row py-4 px-2 justify-center items-center border-b-2">
                                <div className="w-1/4">
                                    <img
                                        src="https://source.unsplash.com/_7LbC5J-jw4/600x600"
                                        className="object-cover h-12 w-12 rounded-full"
                                        alt=""/>
                                </div>
                                <div className="w-full">
                                    <div
                                        className="text-lg font-semibold">{member.nickname}
                                    </div>
                                    <span className="text-gray-500">Pick me at 9:00 Am</span>
                                </div>
                            </div>
                        ))}
                        {/* more user list items */}
                    </div>
                    {/* end chat list */}
                    {/* message */}
                    <div className="w-full px-5 flex flex-col justify-around">
                        <div className="flex flex-col mt-5">
                            <div className="flex justify-end mb-4">
                                <div
                                    className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white"
                                >
                                    Welcome to group everyone !
                                </div>
                                <img
                                    src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                                    className="object-cover h-8 w-8 rounded-full"
                                    alt=""
                                />
                            </div>
                            <div className="flex justify-start mb-4">
                                <img
                                    src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                                    className="object-cover h-8 w-8 rounded-full"
                                    alt=""
                                />
                                <div
                                    className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white"
                                >
                                    Lorem ipsum dolor sit amet consectetur
                                    adipisicing elit. Quaerat
                                    at praesentium, aut ullam delectus odio
                                    error sit rem. Architecto
                                    nulla doloribus laborum illo rem enim dolor
                                    odio saepe,
                                    consequatur quas?
                                </div>
                            </div>
                            <div className="flex justify-end mb-4">
                                <div>
                                    <div
                                        className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white"
                                    >
                                        Lorem ipsum dolor, sit amet consectetur
                                        adipisicing elit.
                                        Magnam, repudiandae.
                                    </div>

                                    <div
                                        className="mt-4 mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white"
                                    >
                                        Lorem ipsum dolor sit amet consectetur
                                        adipisicing elit.
                                        Debitis, reiciendis!
                                    </div>
                                </div>
                                <img
                                    src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                                    className="object-cover h-8 w-8 rounded-full"
                                    alt=""
                                />
                            </div>
                            <div className="flex justify-start mb-4">
                                <img
                                    src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                                    className="object-cover h-8 w-8 rounded-full"
                                    alt=""
                                />
                                <div
                                    className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white"
                                >
                                    happy holiday guys!
                                </div>
                            </div>
                            {/* messages contents */}
                        </div>
                        <div className="py-5 flex">
                            <input
                                className="w-full bg-gray-300 py-5 px-3 rounded-xl mr-3"
                                type="text"
                                placeholder="type your message here..."
                            />
                            <button type="button"
                                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-5 rounded">
                                <AiOutlineCaretUp size="24"/>

                            </button>
                        </div>
                    </div>
                    {/* end message */}
                </div>
            </div>
        </div>
    );
};

export default MainPage;