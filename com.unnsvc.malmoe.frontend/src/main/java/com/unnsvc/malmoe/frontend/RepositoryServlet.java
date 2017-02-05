
package com.unnsvc.malmoe.frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.MalmoeConstants;
import com.unnsvc.malmoe.common.exceptions.AccessException;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.exceptions.NotFoundMalmoeException;
import com.unnsvc.malmoe.common.requests.RequestResolver;
import com.unnsvc.malmoe.common.retrieval.ArtifactRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.FileRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.ModelRetrievalResult;
import com.unnsvc.malmoe.repository.IdentityManager;
import com.unnsvc.malmoe.repository.RepositoryManager;
import com.unnsvc.malmoe.repository.config.MalmoeConfigurationParser;
import com.unnsvc.rhena.common.RhenaConstants;

/**
 * 
 * @TODO sanitize request as we will be passing it into the filesystem
 * @author noname
 *
 */
public class RepositoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
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
			log.info("Configuring Malmoe with malmoe.home at " + malmoeHome);
		} catch (MalmoeException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String contextRelativePath = req.getRequestURI().substring(req.getServletContext().getContextPath().length());
		log.debug("Request path " + contextRelativePath);

		// sanitise
		if (contextRelativePath.contains("..")) {
			sendError(contextRelativePath, req, resp, HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (contextRelativePath.startsWith(MalmoeConstants.BASE_REPOSITORY_URI)) {
			String relativePath = contextRelativePath.substring(MalmoeConstants.BASE_REPOSITORY_URI.length());

			try {
				/**
				 * Now authentication before sending the request into the
				 * repository manager
				 */
				IUser user = requestToUser(req);

				/**
				 * Now pattern matching so we can extract relevant information
				 */
				RequestResolver requestResolver = new RequestResolver(repositoryManager.getRepositoryNames(), user);
				IResolvedRequest resolvedRequest = requestResolver.resolveRequest(relativePath);
				log.debug("Request resolved to " + resolvedRequest);

				/**
				 * Now respond with appropriate HTTP response from repository
				 * manager response
				 */

				IRetrievalResult result = repositoryManager.serveRequest(resolvedRequest);

				if (result instanceof ModelRetrievalResult) {

					resp.setContentType("application/xml");
					serveContent(resp, (ModelRetrievalResult) result);
				} else if (result instanceof ArtifactRetrievalResult) {

					FileRetrievalResult fileResult = (FileRetrievalResult) result;
					if (fileResult.getFile().getName().equals(RhenaConstants.ARTIFACTS_DESCRIPTOR_FILENAME)) {
						resp.setContentType("application/xml");
					} else {
						resp.setContentType("application/octet-stream");
					}
					serveContent(resp, fileResult);
				}

			} catch (AccessException ae) {

				log.debug(ae.getMessage(), ae);
				sendError(contextRelativePath, req, resp, HttpServletResponse.SC_FORBIDDEN);
			} catch (NotFoundMalmoeException nfe) {

				log.debug(nfe.getMessage(), nfe);
				sendError(contextRelativePath, req, resp, HttpServletResponse.SC_NOT_FOUND);
			} catch (MalmoeException me) {

				log.debug(me.getMessage(), me);
				sendError(contextRelativePath, req, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {

			sendError(contextRelativePath, req, resp, HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void serveContent(HttpServletResponse resp, FileRetrievalResult result) throws IOException {

		OutputStream out = resp.getOutputStream();
		try (InputStream is = new FileInputStream(result.getFile())) {

			int buff = -1;
			while ((buff = is.read()) != -1) {

				out.write(buff);
			}
		}
	}

	private void sendError(String requestPath, HttpServletRequest req, HttpServletResponse resp, int status) throws IOException {

		log.info("Returning error to: " + req.getRemoteAddr() + " satus: " + status + " requestPath: " + requestPath);

		resp.sendError(status);
	}

	private IUser requestToUser(HttpServletRequest req) throws AccessException {

		String username = req.getHeader(MalmoeHttpConstants.HEADER_USERNAME);
		String password = req.getHeader(MalmoeHttpConstants.HEADER_PASSWORD);
		try {
			IUser user = identityManager.authenticate(username, password);
			return user;
		} catch (AccessException ae) {
			log.debug(ae.getMessage() + ", trying anonymous user instead");
			IUser anonymous = identityManager.authenticate(MalmoeConstants.ANONYMOUS_USER, null);
			return anonymous;
		}
	}
}
