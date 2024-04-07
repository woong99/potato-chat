import {createSlice} from "@reduxjs/toolkit";

export const stompClientSlice = createSlice({
    name: "stompClient",
    initialState: {
        stompClient: null
    },
    reducers: {
        setStompClient: (state, action) => {
            state.stompClient = action.payload;
        },
        clearStompClient: (state) => {
            state.stompClient = null;
        }
    }
});

export const {setStompClient, clearStompClient} = stompClientSlice.actions;

export const selectStompClient = (state) => state.stompClient.stompClient;

export default stompClientSlice.reducer;
