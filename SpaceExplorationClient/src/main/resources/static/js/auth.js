function logout() {
    const userId = sessionStorage.getItem('currentUserId');
    if (userId) {
        sessionStorage.removeItem(`user_${userId}_missions`);
    }
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('currentUserId');
    console.log('Logout: user session cleared');
}