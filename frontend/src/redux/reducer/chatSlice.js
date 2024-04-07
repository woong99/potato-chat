import {createSlice} from "@reduxjs/toolkit";

export const chatSlice = createSlice({
    name: "chat",
    initialState: {
        receiverId: null,
        messageList: [],
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
        }
    }
});

export const {
    setReceiverId,
    clearReceiverId,
    setMessageList,
    addMessage,
    clearMessageList
} = chatSlice.actions;

export const selectReceiverId = (state) => state.chat.receiverId;

export const selectMessageList = (state) => state.chat.messageList;

export default chatSlice.reducer;
