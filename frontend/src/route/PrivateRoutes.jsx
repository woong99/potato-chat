import React from 'react';
import {useCookies} from "react-cookie";
import {Navigate, Outlet} from "react-router-dom";

const PrivateRoutes = () => {
    const [cookie] = useCookies(['accessToken']);

    return (
        <div>
            {cookie.accessToken ? <Outlet/> : <Navigate to="/login"/>}
        </div>
    );
};

export default PrivateRoutes;