package com.aquima.plugin.fileupload.controller;

import com.aquima.interactions.foundation.logging.LogFactory;
import com.aquima.interactions.foundation.logging.Logger;
import com.aquima.interactions.portal.IPortalSession;
import com.aquima.plugin.fileupload.IFileUploadHandler;
import com.aquima.plugin.xslt.ui.XsltController;

import com.blueriq.component.api.IAquimaSessionsMap;
import com.blueriq.component.api.annotation.ServerContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for uploading, downloading and deleting an uploaded file.
 * 
 * @author hj.van.veenendaal
 * @since 8.1
 */
@Controller(value = "DeprecatedFileUploadController")
@RequestMapping({ "/Fileupload.*", "/Fileupload" })
@ServerContext
public class FileUploadController implements IFileUploadController {

  private static final Logger LOG = LogFactory.getLogger(FileUploadController.class);

  private final IFileUploadHandler mFileUploadHandler;
  private final IAquimaSessionsMap sessionManager;
  private final XsltController controller;

  /**
   * Contructs a new controller for uploading files.
   * 
   * @param fileUploadHandler the {@link IFileUploadHandler} to use for uploading, downloading and deleting files
   * @param sessionManager the {@link IAquimaSessionsMap} with the active sessions
   * @param controller the default {@link XsltController} to render the pages
   */
  @Autowired
  public FileUploadController(IFileUploadHandler fileUploadHandler, IAquimaSessionsMap sessionManager,
      XsltController controller) {
    this.mFileUploadHandler = fileUploadHandler;
    this.sessionManager = sessionManager;
    this.controller = controller;
  }

  @Override
  @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
  public ModelAndView handle(HttpServletRequest request) throws Exception {
    if (request instanceof MultipartHttpServletRequest) {
      return this.upload((MultipartHttpServletRequest) request, request.getParameter("sessionId"));
    }
    String sessionId = request.getParameter("sessionId");
    return this.controller.handle(request, sessionId);
  }

  @Override
  public ModelAndView upload(MultipartHttpServletRequest request, String sessionId) throws Exception {
    IPortalSession session = this.sessionManager.getSession(sessionId).getPortalSession();
    try {
      this.mFileUploadHandler.handleUploads(session, request);
    } catch (Exception e) {
      LOG.warning("Files could not be uploaded", e);
    }
    return this.controller.handle(request, sessionId);
  }

  @Override
  @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, params = { "download" })
  public void download(HttpServletRequest request, HttpServletResponse response,
      @RequestParam("download") String attributeName, @RequestParam("sessionId") String sessionId) throws Exception {
    IPortalSession session = this.sessionManager.getSession(sessionId).getPortalSession();
    try {
      this.mFileUploadHandler.handleDownload(session, response, attributeName);
    } catch (Exception e) {
      LOG.warning("Uploaded file '" + attributeName + "' could not be downloaded", e);
    }
  }

  @Override
  @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, params = { "delete" })
  public ModelAndView delete(HttpServletRequest request, @RequestParam("delete") String attributeName,
      @RequestParam("sessionId") String sessionId) throws Exception {
    IPortalSession session = this.sessionManager.getSession(sessionId).getPortalSession();
    try {
      this.mFileUploadHandler.handleDelete(session, attributeName);
    } catch (Exception e) {
      LOG.warning("Uploaded file '" + attributeName + "' could not be deleted", e);
    }
    return this.controller.handle(request, sessionId);
  }
}
