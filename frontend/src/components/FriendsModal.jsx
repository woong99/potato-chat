import React, {Fragment, useEffect, useState} from 'react';
import {Dialog, Transition} from "@headlessui/react";
import {IoPerson} from "react-icons/io5";
import styled from "styled-components";

const FriendListDialog = ({isOpen, setIsOpen, members, setMembers}) => {
    const [searchWrd, setSearchWrd] = useState(''); // 검색어

    // 검색어에 따라 사용자 목록 필터링
    useEffect(() => {
        members.map(member => {
            member.visible = !!member.nickname.includes(searchWrd);
        })
        setMembers([...members]);
    }, [searchWrd]);

    return (
        <Transition appear show={isOpen} as={Fragment}>
            <Dialog as="div" className="relative z-10" onClose={setIsOpen}>
                <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0"
                    enterTo="opacity-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                >
                    <div className="fixed inset-0 bg-black/25"/>
                </Transition.Child>


                <div className="fixed inset-0 overflow-y-auto">
                    <div
                        className="flex min-h-full items-center justify-center p-4 text-center">
                        <Transition.Child
                            as={Fragment}
                            enter="ease-out duration-300"
                            enterFrom="opacity-0 scale-95"
                            enterTo="opacity-100 scale-100"
                            leave="ease-in duration-200"
                            leaveFrom="opacity-100 scale-100"
                            leaveTo="opacity-0 scale-95"
                        >
                            <Dialog.Panel
                                className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                                <Dialog.Title
                                    as="h3"
                                    className="text-lg font-medium leading-6 text-gray-900"
                                >
                                    대화상대 선택
                                </Dialog.Title>

                                {/* 검색 S */}
                                <div className="pt-3 pb-2">
                                    <input
                                        type="text"
                                        placeholder="이름으로 검색"
                                        className="py-2 px-4 border-2 border-gray-200 rounded-2xl w-full"
                                        onChange={(e) => setSearchWrd(
                                            e.target.value)}
                                    />
                                </div>
                                {/* 검색 E */}

                                {/* 사용자 목록 S */}
                                <div className="mt-2 h-80 overflow-y-scroll">
                                    {members?.map((member) => (
                                        member.visible &&
                                        <div
                                            className="flex gap-x-3 justify-between mt-3 ps-2 pe-4">
                                            <div
                                                className="text-sm leading-6 flex items-center">
                                                <div
                                                    className="bg-amber-300 rounded p-1 me-2">
                                                    <IoPerson size={24}
                                                              color={"#ffffff"}/>
                                                </div>
                                                <label htmlFor="comments"
                                                       className="font-medium text-gray-900 text-xl">
                                                    {member.nickname}
                                                </label>
                                            </div>
                                            <div
                                                className="flex h-6 items-center">
                                                <StyledInput
                                                    id="comments"
                                                    name="comments"
                                                    type="checkbox"
                                                    value={member.userId}
                                                />
                                            </div>
                                        </div>
                                    ))}
                                </div>
                                {/* 사용자 목록 E */}

                                <div className="mt-4 flex justify-end">
                                    <button
                                        type="button"
                                        className="inline-flex justify-center rounded-md border border-transparent bg-gray-300 px-4 py-2 text-sm font-medium text-gray-800 hover:bg-gray-400 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2 me-3"
                                        onClick={() => setIsOpen(false)}
                                    >
                                        취소
                                    </button>
                                    <button
                                        type="button"
                                        className="inline-flex justify-center rounded-md border border-transparent bg-blue-100 px-4 py-2 text-sm font-medium text-blue-900 hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2"
                                    >
                                        확인
                                    </button>
                                </div>
                            </Dialog.Panel>
                        </Transition.Child>
                    </div>
                </div>
            </Dialog>
        </Transition>
    );
};

const StyledInput = styled.input`
  appearance: none;
  border: 1.5px solid #D1D5DB;
  border-radius: 50%;
  width: 1.5rem;
  height: 1.5rem;

  &:checked {
    border-color: transparent;
    background-image: url("data:image/svg+xml,%3csvg viewBox='0 0 16 16' fill='white' xmlns='http://www.w3.org/2000/svg'%3e%3cpath d='M5.707 7.293a1 1 0 0 0-1.414 1.414l2 2a1 1 0 0 0 1.414 0l4-4a1 1 0 0 0-1.414-1.414L7 8.586 5.707 7.293z'/%3e%3c/svg%3e");
    background-size: 100% 100%;
    background-position: 50%;
    background-repeat: no-repeat;
    background-color: limegreen;
  }
`

export default FriendListDialog;