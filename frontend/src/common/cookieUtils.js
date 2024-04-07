export const getCookie = (name) => {
    const cookies = document.cookie.split('; ');
    const cookie = cookies.find((cookie) => cookie.startsWith(name));
    return cookie ? cookie.split('=')[1] : null;
}