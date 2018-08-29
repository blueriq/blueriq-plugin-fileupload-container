package com.aquima.plugin.fileupload.controller;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Mandatory methods for file upload controllers.
 * 
 * @author hj.van.veenendaal
 * @since 8.1
 */
public interface IFileUploadController {

  /**
   * Checks a regular {@link HttpServletRequest} for fileuploads.
   * 
   * @param request the {@link HttpServletRequest}
   * @return the {@link ModalAndView} to render
   */
  ModelAndView handle(HttpServletRequest request) throws Exception;

  /**
   * Checks a {@link MultipartHttpServletRequest} for fileuploads.
   * 
   * @param request the {@link MultipartHttpServletRequest}
   * @param sessionId the sessionID of the session to upload files to
   * @return the {@link ModalAndView} to render
   */
  ModelAndView upload(MultipartHttpServletRequest request, String sessionId) throws Exception;

  /**
   * Sets the response to a file response, containing the file contained in the specified attribute.
   * 
   * @param request the {@link HttpServletRequest}
   * @param response the {@link HttpServletResponse}
   * @param attributeName the name of the attribute holding the file
   * @param sessionId the sessionId of the session from which to read the attribute value
   */
  void download(HttpServletRequest request, HttpServletResponse response, String attributeName, String sessionId)
      throws Exception;

  /**
   * Deletes the file contained in the specified attribute.
   * 
   * @param request request the {@link HttpServletRequest}
   * @param attributeName the name of the attribute holding the file
   * @param sessionId the sessionId of the session from which to read the attribute value
   * @return the {@link ModalAndView} to render
   */
  ModelAndView delete(HttpServletRequest request, String attributeName, String sessionId) throws Exception;
}
