import {createSlice} from "@reduxjs/toolkit";

export const chatSlice = createSlice({
    name: "chat",
    initialState: {
        receiverId: null,
        messageList: [],
        subscribeList: []
    },
    reducers: {
        setReceiverId: (state, action) => {
            state.receiverId = action.payload;
        },
        clearReceiverId: (state) => {
            state.receiverId = null;
        },
        setMessageList: (state, action) => {
            state.messageList = [...state.messageList, ...action.payload];
        },
        addMessage: (state, action) => {
            state.messageList = [...state.messageList, action.payload];
        },
        clearMessageList: (state) => {
            state.messageList = [];
        },
        addSubscribe: (state, action) => {
            state.subscribeList = [...state.subscribeList, action.payload];
        },
    }
});

export const {
    setReceiverId,
    clearReceiverId,
    setMessageList,
    addMessage,
    clearMessageList,
    addSubscribe
} = chatSlice.actions;

export const selectReceiverId = (state) => state.chat.receiverId;

export const selectMessageList = (state) => state.chat.messageList;

export const selectSubscribeList = (state) => state.chat.subscribeList;

export default chatSlice.reducer;
