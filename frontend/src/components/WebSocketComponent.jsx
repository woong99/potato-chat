import {useEffect, useRef} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {useCookies} from "react-cookie";

const WebSocketComponent = () => {
    const stompClient = useRef(null);
    const [cookies] = useCookies(['accessToken']);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        stompClient.current = Stomp.over(socket);
        stompClient.current.connect({
            Authorization: `Bearer ${cookies['accessToken']}`
        }, (frame) => {
            console.log("Connected: " + frame);

        });

        return () => {
            if (stompClient.current) {
                stompClient.current.disconnect();
            }
        }
    }, []);
}

export default WebSocketComponent;