package org.example.utils;

import javax.servlet.http.HttpServletRequest;

public class ServletUtility {
    public static String parseRequestPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        if (path == null) {
            return "";
        }
        else if(path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }

        return path;
    }
}
