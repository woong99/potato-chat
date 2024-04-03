import {BrowserRouter, Route, Routes} from "react-router-dom";
import LoginPage from "../pages/LoginPage";
import MainPage from "../pages/MainPage";
import PrivateRoutes from "./PrivateRoutes";

const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage/>}/>

                <Route element={<PrivateRoutes/>}>
                    <Route path="/" element={<MainPage/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default Router;