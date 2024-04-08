import React, {useEffect, useState} from 'react';
import {AiOutlineCaretUp} from "react-icons/ai";
import {FaPlus} from "react-icons/fa";

import axios from "axios";
import {useCookies} from "react-cookie";
import FriendListDialog from "../components/FriendsModal";
import {useDispatch, useSelector} from "react-redux";
import {selectStompClient} from "../redux/reducer/stompClientSlice";
import {v4 as uuid} from "uuid";
import {IoPerson} from "react-icons/io5";
import {fetchUserInfo, selectUser} from "../redux/reducer/userSlice";
import WebSocketComponent from "../components/WebSocketComponent";
import {
    addMessage,
    clearMessageList,
    selectMessageList,
    selectReceiverId,
    setMessageList,
    setReceiverId
} from "../redux/reducer/chatSlice";

const MainPage = () => {
    const dispatch = useDispatch();
    const [members, setMembers] = useState([]); // 사용자 목록
    const [cookies] = useCookies(['accessToken']); // accessToken
    const [isFriendsModalOpen, setIsFriendsModalOpen] = useState(false); // 친구 목록 모달 on/off
    const [nowChatRoomId, setNowChatRoomId] = useState(''); // 현재 채팅방 ID
    const [nowChatRoomName, setNowChatRoomName] = useState(''); // 현재 채팅방 이름
    const [message, setMessage] = useState(''); // 메시지
    const [chatRoomList, setChatRoomList] = useState([]); // 채팅방 목록
    const stompClient = useSelector(selectStompClient);
    const userInfo = useSelector(selectUser); // 사용자 정보
    const receiverId = useSelector(selectReceiverId); // 수신자 ID
    const messageList = useSelector(selectMessageList); // 메시지 목록

    useEffect(() => {
        if (!userInfo) {
            dispatch(fetchUserInfo())
        }

        fetchChatRoomList().then(res => setChatRoomList(res));
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

    // 친구 목록 모달 오픈
    const openFriendsModal = async () => {
        const members = await fetchMembers();
        if (members.length === 0) {
            alert("등록된 친구가 없습니다.");
        } else {
            setMembers(members.map(member => {
                member.visible = true;
                return member;
            }));
            setIsFriendsModalOpen(true);
        }
    }

    // 메시지 전송
    const sendMessage = () => {
        if (!nowChatRoomId) {
            alert("채팅방을 선택해주세요.");
            return;
        }

        if (!message) {
            alert("메시지를 입력해주세요.");
            return;
        }

        const data = {
            message,
            receiverId: receiverId,
            senderId: userInfo.userId,
            nickname: userInfo.nickname,
            chatCommand: messageList.length === 0 ? 'FIRST_CHAT'
                : 'SEND_MESSAGE'
        }

        stompClient.publish({
            destination: `/publish/${nowChatRoomId}`,
            headers: {Authorization: `Bearer ${cookies['accessToken']}`},
            body: JSON.stringify(data),
        });
        setMessage('');
    }

    // 엔터키로 메시지 전송
    const activeEnter = (e, func) => {
        if (e.key === 'Enter') {
            func()
        }
    }

    // 채팅방 목록 조회
    const fetchChatRoomList = async () => {
        try {
            const res = await axios.get(
                'http://localhost:8080/api/chat-room/list', {
                    headers: {Authorization: `Bearer ${cookies['accessToken']}`}
                });
            return res.data;
        } catch (err) {
            console.error(err);
        }
    }

    // 채팅방 클릭 시 채팅 내역 조회
    const onClickChatRoom = (chatRoomId, receiverId, chatRoomName) => {
        setNowChatRoomId(chatRoomId);
        setNowChatRoomName(chatRoomName);
        dispatch(setReceiverId(receiverId));
        fetchChatList(chatRoomId).then(res => {
            dispatch(setMessageList(res));
        })
    }

    // 채팅 내역 조회
    const fetchChatList = async (chatRoomId) => {
        try {
            dispatch(clearMessageList())
            stompClient.subscribe(`/topic/${chatRoomId}`, (res) => {
                const message = JSON.parse(res.body);
                dispatch(addMessage(message));
            });

            const res = await axios.get(
                `http://localhost:8080/api/chat/list/${chatRoomId}`, {
                    headers: {Authorization: `Bearer ${cookies['accessToken']}`}
                });
            return res.data;
        } catch (err) {
            console.error(err);
        }
    }

    return (
        <div
            className="h-screen overflow-hidden flex items-center justify-center"
            style={{background: '#edf2f7'}}>
            <div className="container mx-auto shadow-lg rounded-lg h-full">
                {/* Header */}
                <div
                    className="px-5 py-5 flex justify-between items-center bg-white border-b-2"
                    style={{height: '10%'}}>
                    <div className="font-semibold text-2xl">Potato Chat</div>
                    <div onClick={openFriendsModal}
                         className="h-12 w-12 p-2 bg-yellow-500 rounded-full text-white font-semibold flex items-center justify-center cursor-pointer">
                        <FaPlus/>
                    </div>
                </div>
                {/* Chatting */}
                <div className="flex flex-row justify-between bg-white"
                     style={{height: '90%'}}>
                    {/* Chat list */}
                    <div
                        className="flex flex-col w-2/5 border-r-2 overflow-y-auto relative">
                        {/* search component */}
                        <div className="border-b-2 py-4 px-2">
                            <input
                                type="text"
                                placeholder="search chatting"
                                className="py-2 px-2 border-2 border-gray-200 rounded-2xl w-full"
                            />
                        </div>
                        {/* user list */}
                        <WebSocketComponent/>
                        <div className="h-4/6">
                            {chatRoomList?.map(chatRoom => (
                                <div key={chatRoom.chatRoomId}
                                     className={`flex flex-row py-4 px-2 justify-center items-center border-b-2 ${nowChatRoomId
                                     === chatRoom.chatRoomId && 'bg-blue-100'}`}
                                     onClick={() => onClickChatRoom(
                                         chatRoom.chatRoomId,
                                         chatRoom.receiverId,
                                         chatRoom.name)}>
                                    <div
                                        className="bg-amber-300 rounded h-8 p-1 me-6">
                                        <IoPerson size={24}
                                                  color={"#ffffff"}/>
                                    </div>
                                    <div className="w-full">
                                        <div
                                            className="text-lg font-semibold">{chatRoom.name}
                                        </div>
                                        <span className="text-gray-500">Pick me at 9:00 Am</span>
                                    </div>
                                </div>
                            ))}
                        </div>
                        {/* more user list items */}
                    </div>
                    {/* end chat list */}
                    {/* message */}
                    <div
                        className="w-full px-5 flex flex-col justify-between">
                        {nowChatRoomName &&
                            <div
                                className="mt-2 border-b text-2xl pb-2 flex items-center">
                                <div
                                    className="bg-amber-300 rounded h-8 p-1 me-2">
                                    <IoPerson size={24}
                                              color={"#ffffff"}/>
                                </div>
                                {nowChatRoomName}
                            </div>
                        }
                        <div className="flex flex-col overflow-y-scroll"
                             style={{height: '80%'}}>
                            {messageList?.map((message) => (
                                message.member.userId === userInfo.userId ?
                                    (<div className="flex justify-end mb-4"
                                          key={uuid()}>
                                        <div
                                            className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white"
                                        >
                                            {message.message}
                                        </div>
                                        <div
                                            className="bg-amber-300 rounded h-8 p-1 me-2">
                                            <IoPerson size={24}
                                                      color={"#ffffff"}/>
                                        </div>
                                    </div>) :
                                    (<div className="flex justify-start mb-4"
                                          key={uuid()}>
                                            <div
                                                className="bg-amber-300 rounded h-8 p-1 me-2">
                                                <IoPerson size={24}
                                                          color={"#ffffff"}/>
                                            </div>
                                            <div
                                                className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white"
                                            >
                                                {message.message}
                                            </div>
                                        </div>
                                    )
                            ))}
                            {/*<div className="flex justify-end mb-4">*/}
                            {/*    <div*/}
                            {/*        className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white"*/}
                            {/*    >*/}
                            {/*        Welcome to group everyone !*/}
                            {/*    </div>*/}
                            {/*    <img*/}
                            {/*        src="https://source.unsplash.com/vpOeXr5wmR4/600x600"*/}
                            {/*        className="object-cover h-8 w-8 rounded-full"*/}
                            {/*        alt=""*/}
                            {/*    />*/}
                            {/*</div>*/}
                            {/*<div className="flex justify-start mb-4">*/}
                            {/*    <img*/}
                            {/*        src="https://source.unsplash.com/vpOeXr5wmR4/600x600"*/}
                            {/*        className="object-cover h-8 w-8 rounded-full"*/}
                            {/*        alt=""*/}
                            {/*    />*/}
                            {/*    <div*/}
                            {/*        className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white"*/}
                            {/*    >*/}
                            {/*        Lorem ipsum dolor sit amet consectetur*/}
                            {/*        adipisicing elit. Quaerat*/}
                            {/*        at praesentium, aut ullam delectus odio*/}
                            {/*        error sit rem. Architecto*/}
                            {/*        nulla doloribus laborum illo rem enim dolor*/}
                            {/*        odio saepe,*/}
                            {/*        consequatur quas?*/}
                            {/*    </div>*/}
                            {/*</div>*/}
                            {/*<div className="flex justify-end mb-4">*/}
                            {/*    <div>*/}
                            {/*        <div*/}
                            {/*            className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white"*/}
                            {/*        >*/}
                            {/*            Lorem ipsum dolor, sit amet consectetur*/}
                            {/*            adipisicing elit.*/}
                            {/*            Magnam, repudiandae.*/}
                            {/*        </div>*/}

                            {/*        <div*/}
                            {/*            className="mt-4 mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white"*/}
                            {/*        >*/}
                            {/*            Lorem ipsum dolor sit amet consectetur*/}
                            {/*            adipisicing elit.*/}
                            {/*            Debitis, reiciendis!*/}
                            {/*        </div>*/}
                            {/*    </div>*/}
                            {/*    <img*/}
                            {/*        src="https://source.unsplash.com/vpOeXr5wmR4/600x600"*/}
                            {/*        className="object-cover h-8 w-8 rounded-full"*/}
                            {/*        alt=""*/}
                            {/*    />*/}
                            {/*</div>*/}
                            {/*<div className="flex justify-start mb-4">*/}
                            {/*    <img*/}
                            {/*        src="https://source.unsplash.com/vpOeXr5wmR4/600x600"*/}
                            {/*        className="object-cover h-8 w-8 rounded-full"*/}
                            {/*        alt=""*/}
                            {/*    />*/}
                            {/*    <div*/}
                            {/*        className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white"*/}
                            {/*    >*/}
                            {/*        happy holiday guys!*/}
                            {/*    </div>*/}
                            {/*</div>*/}
                            {/* messages contents */}
                        </div>
                        {/* 메세지 입력 창 S */}
                        <div className="py-5 flex">
                            <input
                                className="w-full bg-gray-300 py-5 px-3 rounded-xl mr-3"
                                type="text"
                                value={message}
                                onChange={(e) => setMessage(e.target.value)}
                                onKeyUp={(message && nowChatRoomId) ? (e) => {
                                    activeEnter(e, sendMessage)
                                } : undefined}
                                placeholder="type your message here..."
                            />
                            <button type="button"
                                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-5 rounded"
                                    onClick={(message && nowChatRoomId)
                                        ? (() => sendMessage())
                                        : undefined}>
                                <AiOutlineCaretUp size="24"/>
                            </button>
                        </div>
                        {/* 메세지 입력 창 E */}
                    </div>
                    {/* end message */}
                </div>
            </div>

            {/* 친구 목록 모달 S */}
            <FriendListDialog isOpen={isFriendsModalOpen}
                              setIsOpen={setIsFriendsModalOpen}
                              members={members}
                              setMembers={setMembers}
                              setChatRoomId={setNowChatRoomId}
                              fetchChatList={fetchChatList}/>
            {/* 친구 목록 모달 S */}
        </div>
    );
};

export default MainPage;