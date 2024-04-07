import {useEffect, useRef} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {useCookies} from "react-cookie";
import {useDispatch} from "react-redux";
import {
    clearStompClient,
    setStompClient
} from "../redux/reducer/stompClientSlice";

const WebSocketComponent = () => {
    const dispatch = useDispatch();
    const stompClientRef = useRef(null);
    const [cookies] = useCookies(['accessToken']);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        stompClientRef.current = Stomp.over(socket);
        stompClientRef.current.connect({
            Authorization: `Bearer ${cookies['accessToken']}`
        }, (frame) => {
            console.log("Connected: " + frame);
            dispatch(setStompClient(stompClientRef.current))
        });

        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.disconnect();
                dispatch(clearStompClient());
            }
        }
    }, []);
}

export default WebSocketComponent;