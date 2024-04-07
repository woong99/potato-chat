import {configureStore} from "@reduxjs/toolkit";
import stompClientReducer from "./reducer/stompClientSlice";
import userReducer from "./reducer/userSlice";
import chatReducer from "./reducer/chatSlice";

export default configureStore({
    reducer: {
        stompClient: stompClientReducer,
        user: userReducer,
        chat: chatReducer
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware({
        serializableCheck: false
    }),
});
