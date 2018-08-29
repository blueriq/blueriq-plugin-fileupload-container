package com.aquima.plugin.fileupload;

import com.aquima.interactions.portal.IPortalContext;
import com.aquima.interactions.portal.IPortalSession;
import com.aquima.interactions.profile.ValueReference;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * Handler class for managing uploaded files, e.g. Uploading, Downloading and deleting. The information about the file
 * is saved in an attribute.
 *
 * @author d.roest
 * @since 8.1
 */
public interface IFileUploadHandler {

  /**
   * Checks the multipart request for uploaded files and handles the uploaded files.
   * <p>
   * This method is by default called from {@link RunController} when it detects there is a multipart
   *
   * @param session the current PortalSession
   * @param request the multipart request
   */
  void handleUploads(IPortalSession session, MultipartHttpServletRequest request);

  /**
   * Outputs the file to the browser.
   *
   * @param session the current PortalSession
   * @param response the response to write the file to
   * @param attributeFullName the full name of the attribute in which the file information is saved
   */
  void handleDownload(IPortalSession session, HttpServletResponse response, String attributeFullName);

  /**
   * Deletes (or hides) the uploaded file.
   *
   * @param session the current PortalSession
   * @param attributeFullName the full name of the attribute in which the file information is saved
   */
  void handleDelete(IPortalSession session, String attributeFullName);

  /**
   * Returns the original filename of the uploaded file.
   * <p>
   * The purpose of this method is that the container can display the name of the attribute.
   *
   * @param context the current context
   * @param attributeReference the {@link ValueReference} of the attribute containing the file
   * @return the original filename of the uploaded file
   */
  String getFilename(IPortalContext context, ValueReference attributeReference);

}
