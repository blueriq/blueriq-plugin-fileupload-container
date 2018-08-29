package com.aquima.plugin.fileupload;

import com.aquima.interactions.foundation.GUID;
import com.aquima.interactions.foundation.exception.AppException;
import com.aquima.interactions.foundation.exception.SysException;
import com.aquima.interactions.foundation.logging.LogFactory;
import com.aquima.interactions.foundation.logging.Logger;
import com.aquima.interactions.foundation.types.EntityValue;
import com.aquima.interactions.portal.IActionContext;
import com.aquima.interactions.portal.IActionHandler;
import com.aquima.interactions.portal.IActionResult;
import com.aquima.interactions.portal.IPortalContext;
import com.aquima.interactions.portal.IPortalSession;
import com.aquima.interactions.portal.PortalEvent;
import com.aquima.interactions.portal.PortalException;
import com.aquima.interactions.profile.ValueReference;
import com.aquima.plugin.fileupload.actionhandler.EmptyActionResult;
import com.aquima.plugin.fileupload.actionhandler.ResetAttributeValueActionHandler;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple implementation of the {@link IFileUploadHandler} in which file information (filename, content-type) and its
 * content (in base64) is saved directly into the attribute. This can be used for quick demo solutions, but should not
 * be used in a production environment.
 *
 * @author d.roest
 * @since 8.1
 */
public class BasicFileUploadHandler implements IFileUploadHandler {

  private static final Logger LOG = LogFactory.getLogger(BasicFileUploadHandler.class);

  @Override
  public void handleUploads(IPortalSession session, MultipartHttpServletRequest request) {
    Iterator<String> fileNames = request.getFileNames();
    List<AttributeAttachment> attachments = new ArrayList<AttributeAttachment>();
    while (fileNames.hasNext()) {
      String attrRef = fileNames.next();
      MultipartFile file = request.getFile(attrRef);
      if (file.isEmpty()) { // AQR-2224
        continue;
      }
      attachments.add(new AttributeAttachment(toValueReference(attrRef), file));
    }

    session.executeInlineAction(new SaveFilesInAttributesHandler(attachments), null);
    recompose(session);

  }

  @Override
  public void handleDownload(IPortalSession session, HttpServletResponse response, String attributeFullName) {
    ValueReference attrRef = toValueReference(attributeFullName);

    final StringResult result = (StringResult) session.executeInlineAction(new GetAttributeValueHandler(attrRef), null);
    final String attributeValue = result.getValue();
    final String[] data = attributeValue.split("\\|");
    final String filename = data[0];
    final String contentType = data[1];
    final String contentBase64 = data[2];

    try {
      output(response, filename, contentType, contentBase64);
    } catch (IOException e) {
      throw new SysException(e);
    }
  }

  @Override
  public void handleDelete(IPortalSession session, String attributeFullName) {
    ValueReference attrRef = toValueReference(attributeFullName);

    session.executeInlineAction(new ResetAttributeValueActionHandler(attrRef), null);
    recompose(session);
  }

  @Override
  public String getFilename(IPortalContext context, ValueReference attrRef) {
    try {
      String rawData = context.getProfile().getInstance(attrRef).getValue(attrRef.getAttributeName()).stringValue();
      final String fileName = rawData.split("\\|")[0];
      return fileName;
    } catch (Exception e) {
      // entity.attribute apparently unknown, return null
      return null;
    }
  }

  private void output(HttpServletResponse response, final String filename, final String contentType,
      final String contentBase64) throws IOException {
    response.reset();
    response.setContentType(contentType);
    response.setDateHeader("Expires", 0);
    response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

    ServletOutputStream out = response.getOutputStream();
    out.write(Base64.decodeBase64(contentBase64));
    out.close();
  }

  private void recompose(IPortalSession session) throws AppException {
    if (session.hasChildPortalSession()) {
      session.getChildPortalSession().handleEvent(new PortalEvent());
    } else {
      session.handleEvent(new PortalEvent());
    }
  }

  private static ValueReference toValueReference(String valueReferenceString) {
    String[] parts = valueReferenceString.split("\\|");
    ValueReference attrRef = new ValueReference(new EntityValue(parts[0], GUID.valueOf(parts[1]), parts[2]), parts[3]);

    return attrRef;
  }

  private static String toAttributeValue(MultipartFile file) throws IOException {
    String contentBase64 = Base64.encodeBase64String(file.getBytes());
    return file.getOriginalFilename() + "|" + file.getContentType() + "|" + contentBase64;
  }

  private class GetAttributeValueHandler implements IActionHandler {

    private final ValueReference attributeReference;

    public GetAttributeValueHandler(ValueReference attributeReference) {
      this.attributeReference = attributeReference;
    }

    @Override
    public IActionResult handle(IActionContext context) throws PortalException, Exception {
      String stringValue = context.getProfile().getInstance(attributeReference)
          .getValue(attributeReference.getAttributeName()).stringValue();
      return new StringResult(stringValue);
    }

    @Override
    public boolean isReadOnly() {
      return true;
    }
  }

  private class SaveFilesInAttributesHandler implements IActionHandler {
    private final List<AttributeAttachment> attachments;

    public SaveFilesInAttributesHandler(List<AttributeAttachment> attachments) {
      super();
      this.attachments = attachments;
    }

    @Override
    public IActionResult handle(IActionContext context) throws PortalException, Exception {
      for (AttributeAttachment attachment : attachments) {
        ValueReference attrRef = attachment.getValueReference();

        LOG.info("Saving " + attrRef);
        context.getProfile().getInstance(attrRef).setValue(attrRef.getAttributeName(),
            BasicFileUploadHandler.toAttributeValue(attachment.getFile()));
      }
      return new EmptyActionResult();
    }

    @Override
    public boolean isReadOnly() {
      return false;
    }
  }

  private class AttributeAttachment {
    private final ValueReference attributeReference;
    private final MultipartFile file;

    public AttributeAttachment(ValueReference attributeReference, MultipartFile file) {
      super();
      this.attributeReference = attributeReference;
      this.file = file;
    }

    public ValueReference getValueReference() {
      return attributeReference;
    }

    public MultipartFile getFile() {
      return file;
    }
  }

  private class StringResult implements IActionResult {
    private static final long serialVersionUID = -7371017460088913619L;
    private final String value;

    public StringResult(String value) {
      super();
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
