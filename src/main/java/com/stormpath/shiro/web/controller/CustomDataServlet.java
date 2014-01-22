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

package com.stormpath.shiro.web.controller;

import com.stormpath.shiro.web.model.CustomDataBean;
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
     * Retrieval of custom data is handled here relying on the {@link StormpathService} to get it.
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
            try {
                Map account = org.apache.shiro.SecurityUtils.getSubject().getPrincipals().oneByType(java.util.Map.class);
                String accountHref = account.get("href").toString();
                String accountId = accountHref.substring(accountHref.lastIndexOf("/") + 1);
                CustomDataBean customDataBean = StormpathService.getInstance().getCustomData(accountId);

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
    }

    /**
     * Insertion of custom data fields is handled here relying on the {@link StormpathService} to actually do it.
     *
     * @param req	an {@link HttpServletRequest} object that contains the request the client has made
     *			of the servlet
     *
     * @param resp	an {@link HttpServletResponse} object that contains the response the servlet sends
     *			to the client
     *
     * @exception IOException	if an input or output error is detected when the servlet handles
     *				the POST request
     *
     * @exception ServletException	if the request for the POST could not be handled
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if(uri.startsWith(SERVLET_URI)) {
            try {
                String accounId = uri.substring(uri.lastIndexOf("/") + 1);
                String key = req.getParameter("key");
                String value = req.getParameter("value");
                StormpathService.getInstance().addCustomDataField(accounId, key, value);
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
                throw new ServletException(ex);
            }
        } else {
            throw new ServletException("This servlet cannot handle this POST request: " + uri);
        }
    }

    /**
     * Deletion of custom data fields is handled here relying on the {@link StormpathService} to actually do it.
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
        if(req.getRequestURI().startsWith(SERVLET_URI)) {
            try {
                String accounId = uri.substring(SERVLET_URI.length() + 1, uri.lastIndexOf("/"));
                String customDataFieldKey = uri.substring(uri.lastIndexOf("/") + 1);
                StormpathService.getInstance().deleteCustomDataField(accounId, customDataFieldKey);
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
                throw new ServletException(ex);
            }
        } else {
            throw new ServletException("This servlet cannot handle this DELETE request: " + uri);
        }
    }

}
