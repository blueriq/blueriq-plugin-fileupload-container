package com.aquima.plugin.fileupload.container;

import com.aquima.interactions.composer.model.Button;
import com.aquima.interactions.composer.model.Container;
import com.aquima.interactions.composer.model.Element;
import com.aquima.interactions.composer.model.PresentationStyle;
import com.aquima.interactions.composer.model.definition.ContainerDefinition;
import com.aquima.interactions.foundation.exception.AppException;
import com.aquima.interactions.foundation.text.MultilingualText;
import com.aquima.interactions.metamodel.AttributeReference;
import com.aquima.interactions.metamodel.exception.UnknownEntityException;
import com.aquima.interactions.portal.IContainerContext;
import com.aquima.interactions.portal.IContainerExpander;
import com.aquima.interactions.profile.IEntityInstance;
import com.aquima.interactions.profile.ValueReference;
import com.aquima.interactions.profile.exception.CreateInstanceException;
import com.aquima.interactions.profile.exception.UnknownInstanceException;
import com.aquima.plugin.fileupload.IFileUploadHandler;

import com.blueriq.component.api.annotation.AquimaExpander;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dynamic container in which a file can be uploaded, downloaded and deleted.
 * 
 * @author d.roest
 * @author hj.van.veenendaal
 * @since 8.1
 */
@AquimaExpander("BB_SingleFileUpload")
public class SingleFileUploadContainer implements IContainerExpander {

  private static final String PARAMETER_ATTRIBUTE = "attribute";

  private static final PresentationStyle PRESENTATION_STYLE_UPLOAD = PresentationStyle.valueOf("file_upload");
  private static final PresentationStyle PRESENTATION_STYLE_DOWNLOAD = PresentationStyle.valueOf("file_download");
  private static final PresentationStyle PRESENTATION_STYLE_DELETE = PresentationStyle.valueOf("file_delete");

  private final IFileUploadHandler mFileUploadHandler;

  /**
   * Adds a dynamic container that enables uploading, downloading and deleting of files.
   * 
   * @param fileUploadHandler The {@link IFileUploadHandler} to use for dealing with files
   */
  @Autowired
  public SingleFileUploadContainer(IFileUploadHandler fileUploadHandler) {
    this.mFileUploadHandler = fileUploadHandler;
  }

  @Override
  public Container expand(Container container, ContainerDefinition containerDefinition, IContainerContext context)
      throws Exception {
    final String attributeFullName = context.getParameter(PARAMETER_ATTRIBUTE);
    final String attributeName = new AttributeReference(attributeFullName).getAttributeName();
    final String entityName = new AttributeReference(attributeFullName).getEntityName();

    IEntityInstance instance = context.getProfile().getActiveInstance(entityName);
    ValueReference attrRef = instance.getAttributeState(attributeName).getValueReference();

    // TODO: get texts from the attribute definition?
    String attrRefString = attrRef.stringValue() + '|' + attrRef.getAttributeName();
    final boolean isUploaded = this.isUploaded(context, attrRef);
    if (!isUploaded) {
      final Element element = new Button(attrRefString, new MultilingualText("Upload"));
      element.setPresentationStyle(PRESENTATION_STYLE_UPLOAD);
      container.addElement(element);
    } else {
      final String fileName = this.mFileUploadHandler.getFilename(context, attrRef);

      final Element element = new Button(attrRefString, new MultilingualText(fileName));
      element.setPresentationStyle(PRESENTATION_STYLE_DOWNLOAD);
      container.addElement(element);

      final Element button = new Button(attrRefString, new MultilingualText("Delete"));
      button.setPresentationStyle(PRESENTATION_STYLE_DELETE);
      container.addElement(button);
    }
    return container;
  }

  private boolean isUploaded(IContainerContext context, final ValueReference attrRef)
      throws AppException, UnknownInstanceException, UnknownEntityException, CreateInstanceException {
    return !context.getProfile().getInstance(attrRef).getValue(attrRef.getAttributeName()).isUnknown();
  }
}
