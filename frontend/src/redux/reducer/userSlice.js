import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {getCookie} from "../../common/cookieUtils";

export const fetchUserInfo = createAsyncThunk("/api/member/info",
    async () => {
        const res = await axios.get("http://localhost:8080/api/member/me",
            {headers: {Authorization: `Bearer ${getCookie('accessToken')}`}});
        return res.data;
    })

export const userSlice = createSlice({
    name: "user",
    initialState: {
        user: null,
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
        .addCase(fetchUserInfo.pending, (state) => {
            state.status = 'loading';
        })
        .addCase(fetchUserInfo.fulfilled, (state, action) => {
            state.status = 'succeeded';
            state.user = action.payload;
        })
        .addCase(fetchUserInfo.rejected, (state, action) => {
            state.status = 'failed';
            state.error = action.error.message;
        });
    },
});

export const selectUser = (state) => state.user.user;

export default userSlice.reducer;
