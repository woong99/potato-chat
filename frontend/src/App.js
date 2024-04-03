import Router from './route/Router';
import './App.css';
import {CookiesProvider} from "react-cookie";

function App() {
    return (
        <CookiesProvider>
            <Router/>
        </CookiesProvider>
    );
}

export default App;
