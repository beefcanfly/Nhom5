package com.trungtam.utils;

import com.trungtam.model.SessionUser;

public class SessionManager {

    // biến static = dùng chung toàn project
    private static SessionUser session;

    // lưu session sau login
    public static void setSession(SessionUser s) {
        session = s;
    }

    // lấy session ở mọi nơi
    public static SessionUser getSession() {
        return session;
    }

    // logout (optional nhưng nên có)
    public static void clear() {
        session = null;
    }
}