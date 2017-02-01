
package com.unnsvc.malmoe.frontend;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.MalmoeConstants;
import com.unnsvc.malmoe.common.exceptions.AccessException;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.IdentityManager;
import com.unnsvc.malmoe.repository.RepositoryManager;
import com.unnsvc.malmoe.repository.config.MalmoeConfigurationParser;
import com.unnsvc.malmoe.repository.identity.AnonymousUser;
import com.unnsvc.malmoe.repository.retrieval.ArtifactRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.ExecutionsRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.ModelRetrievalResult;

public class RepositoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IMalmoeConfiguration malmoeConfig;
	private IIdentityManager identityManager;
	private IRepositoryManager repositoryManager;

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		try {
			String malmoeHomeProperty = System.getProperty(MalmoeConstants.SYSPROP_MALMOE_HOME);
			File malmoeHome = new File(malmoeHomeProperty);
			if (!malmoeHome.isDirectory()) {
				throw new ServletException(MalmoeConstants.SYSPROP_MALMOE_HOME + " system property not set.");
			}

			malmoeConfig = new MalmoeConfigurationParser(malmoeHome);
			identityManager = new IdentityManager(malmoeConfig.getIdentityConfig());
			repositoryManager = new RepositoryManager(malmoeHome, identityManager, malmoeConfig.getRepositoriesConfig());
		} catch (MalmoeException e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String requestPath = req.getRequestURI();
		if (requestPath.startsWith(MalmoeConstants.BASE_REPOSITORY_URI)) {
			String relativePath = requestPath.substring(MalmoeConstants.BASE_REPOSITORY_URI.length());



			try {
				/**
				 * Now pattern matching so we can extract relevant information
				 */
				RequestResolver requestResolver = new RequestResolver(repositoryManager.getRepositoryNames());
				IResolvedRequest resolvedRequest = requestResolver.resolveRequest(relativePath);
				
				/**
				 * Now authentication before sending the request into the
				 * repository manager
				 */
				IUser user = requestToUser(req);
				IRetrievalRequest request = null;
				/**
				 * Now respond with appropriate HTTP response from repository
				 * manager response
				 */

				IRetrievalResult result = repositoryManager.serveRequest(request);

				if (result instanceof ModelRetrievalResult) {

					throw new UnsupportedOperationException("Not implemented");
				} else if (result instanceof ExecutionsRetrievalResult) {

					throw new UnsupportedOperationException("Not implemented");
				} else if (result instanceof ArtifactRetrievalResult) {

					throw new UnsupportedOperationException("Not implemented");
				}

			} catch (AccessException ae) {

				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (MalmoeException me) {

				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}

		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	private IUser requestToUser(HttpServletRequest req) throws AccessException {

		String username = req.getHeader(MalmoeHttpConstants.HEADER_USERNAME);
		String password = req.getHeader(MalmoeHttpConstants.HEADER_PASSWORD);
		if (username != null && password != null) {
			IUser user = identityManager.authenticate(username, password);
			return user;
		}
		return new AnonymousUser();
	}
}
