/*
 * Copyright 2013 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stormpath.shiro.web.servlet;

import com.stormpath.shiro.web.controller.CustomDataController;
import com.stormpath.shiro.web.model.CustomDataBean;
import com.stormpath.shiro.web.model.CustomDataFieldBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Endpoint responding to custom data requests from the UI.
 */
public class CustomDataServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CustomDataServlet.class);

    private static final String SERVLET_URI = "/account/customData";

    /**
     * Retrieval of custom data is handled here relying on the {@link com.stormpath.shiro.web.controller.CustomDataController} to get it.
     *
     * @param req	an {@link HttpServletRequest} object that contains the request the client has made
     *			of the servlet
     *
     * @param resp	an {@link HttpServletResponse} object that contains the response the servlet sends
     *			to the client
     *
     * @exception IOException	if an input or output error is detected when the servlet handles
     *				the GET request
     *
     * @exception ServletException	if the request for the GET could not be handled
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if(uri.startsWith(SERVLET_URI)) {
            String[] splittedUri = StringUtils.split(uri, '/');
            if(splittedUri.length == 3) {
                try {
                    Map account = SecurityUtils.getSubject().getPrincipals().oneByType(java.util.Map.class);
                    String accountHref = account.get("href").toString();
                    String accountId = accountHref.substring(accountHref.lastIndexOf("/") + 1);
                    CustomDataBean customDataBean = CustomDataController.getInstance().getCustomData(accountId);

                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account/customData.jsp");
                    req.setAttribute("customDataFields", customDataBean.getCustomDataFields());
                    req.setAttribute("accountId", accountId);
                    dispatcher.forward(req,resp);
                } catch (Exception ex) {
                    logger.warn(ex.getMessage());
                    throw new ServletException(ex);
                }
            } else {
                throw new ServletException("This servlet cannot handle this GET request: " + uri);
            }
        } else {
            throw new ServletException("This servlet cannot handle this GET request: " + uri);
        }
    }

    /**
     * Insertion of custom data fields is handled here relying on the {@link CustomDataController} to actually do it. The
     * updated custom data field value will be returned in the response so the UI can display it.
     *
     * @param req	an {@link HttpServletRequest} object that contains the request the client has made
     *			of the servlet
     *
     * @param resp	an {@link HttpServletResponse} object that contains the field that has just been added/updated and
     *              is sent to the client
     *
     * @exception IOException	if an input or output error is detected when the servlet handles
     *				the POST request
     *
     * @exception ServletException	if the request for the POST could not be handled
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] splittedUri = StringUtils.split(uri, '/');
        if(uri.startsWith(SERVLET_URI) && splittedUri.length == 4) {
            try {
                String accounId = splittedUri[3];
                String key = req.getParameter("key");
                String value = req.getParameter("value");
                CustomDataFieldBean field = CustomDataController.getInstance().addCustomDataField(accounId, key, value);
                try {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().write(field.getValue().toString());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
                throw new ServletException(ex);
            }
        } else {
            throw new ServletException("This servlet cannot handle this POST request: " + uri);
        }
    }

    /**
     * Deletion of custom data fields is handled here relying on the {@link CustomDataController} to actually do it.
     *
     * @param req	an {@link HttpServletRequest} object that contains the request the client has made
     *			of the servlet
     *
     * @param resp	an {@link HttpServletResponse} object that contains the response the servlet sends
     *			to the client
     *
     * @exception IOException	if an input or output error is detected when the servlet handles
     *				the DELETE request
     *
     * @exception ServletException	if the request for the DELETE could not be handled
     */
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] splittedUri = StringUtils.split(uri, '/');
        if(uri.startsWith(SERVLET_URI) && splittedUri.length == 5) {
            try {
                String accounId = splittedUri[3];
                String customDataFieldKey = splittedUri[4];
                CustomDataController.getInstance().deleteCustomDataField(accounId, customDataFieldKey);
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
                throw new ServletException(ex);
            }
        } else {
            throw new ServletException("This servlet cannot handle this DELETE request: " + uri);
        }
    }

}
