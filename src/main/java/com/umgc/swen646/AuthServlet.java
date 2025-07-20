package com.umgc.swen646;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;


/**
 * Servlet for user registration and authentication.
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/api/auth/register", "/api/auth/login", "/api/auth/mfa-verify"})
public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String path = req.getServletPath();
        JSONObject jsonRequest = getJsonRequest(req);
        JSONObject jsonResponse = new JSONObject();

        if ("/api/auth/register".equals(path)) {
            handleRegister(jsonRequest, jsonResponse, resp);
        } else if ("/api/auth/login".equals(path)) {
            handleLogin(jsonRequest, jsonResponse, resp);
        } else if ("/api/auth/mfa-verify".equals(path)) {
            handleMfaVerify(jsonRequest, jsonResponse, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            jsonResponse.put("error", "Endpoint not found");
            resp.getWriter().write(jsonResponse.toString());
        }
    }

    private void handleRegister(JSONObject jsonRequest, JSONObject jsonResponse, HttpServletResponse resp) throws IOException {
        String username = jsonRequest.optString("username");
        String password = jsonRequest.optString("password");
        String mfaSecret = jsonRequest.optString("mfaSecret", null);
        if (username.isEmpty() || password.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("error", "Username and password are required");
        } else if (userDAO.findByUsername(username) != null) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            jsonResponse.put("error", "Username already exists");
        } else {
            if (mfaSecret == null || mfaSecret.isEmpty()) {
                mfaSecret = MFAUtil.generateSecret();
            }
            boolean created = userDAO.createUser(username, password, mfaSecret);
            if (created) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                jsonResponse.put("success", true);
                jsonResponse.put("mfaSecret", mfaSecret);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.put("error", "Failed to create user");
            }
        }
        resp.getWriter().write(jsonResponse.toString());
    }

    private void handleLogin(JSONObject jsonRequest, JSONObject jsonResponse, HttpServletResponse resp) throws IOException {
        String username = jsonRequest.optString("username");
        String password = jsonRequest.optString("password");
        if (username.isEmpty() || password.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("error", "Username and password are required");
        } else {
            User user = userDAO.verifyUser(username, password);
            if (user != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("success", true);
                jsonResponse.put("userId", user.getId());
                jsonResponse.put("mfaSecret", user.getMfaSecret());
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("error", "Invalid credentials");
            }
        }
        resp.getWriter().write(jsonResponse.toString());
    }

    private void handleMfaVerify(JSONObject jsonRequest, JSONObject jsonResponse, HttpServletResponse resp) throws IOException {
        String username = jsonRequest.optString("username");
        String code = jsonRequest.optString("code");
        if (username.isEmpty() || code.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Username and code are required");
        } else {
            User user = userDAO.findByUsername(username);
            if (user == null || user.getMfaSecret() == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("success", false);
                jsonResponse.put("error", "User or MFA secret not found");
            } else {
                boolean valid = MFAUtil.verifyCode(user.getMfaSecret(), code);
                if (valid) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    jsonResponse.put("success", true);
                } else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Invalid code");
                }
            }
        }
        resp.getWriter().write(jsonResponse.toString());
    }

    private JSONObject getJsonRequest(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return new JSONObject(sb.toString());
    }
} 