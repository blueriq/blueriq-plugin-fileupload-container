package com.aquima.plugin.fileupload.actionhandler;

import com.aquima.interactions.portal.IActionContext;
import com.aquima.interactions.portal.IActionHandler;
import com.aquima.interactions.portal.IActionResult;
import com.aquima.interactions.portal.PortalException;
import com.aquima.interactions.profile.ValueReference;

/**
 * Action handler for resetting the value an attribute.
 * 
 * @author d.roest
 * @since 8.1
 */
public class ResetAttributeValueActionHandler implements IActionHandler {

  private final ValueReference attributeReference;

  /**
   * Resets the value of the specified attribute.
   * 
   * @param attributeReference the {@link ValueReference} of the attribute to reset
   */
  public ResetAttributeValueActionHandler(ValueReference attributeReference) {
    super();
    this.attributeReference = attributeReference;
  }

  @Override
  public IActionResult handle(IActionContext context) throws PortalException, Exception {
    context.getProfile().getInstance(this.attributeReference).setValue(this.attributeReference.getAttributeName(),
        null);
    return new EmptyActionResult();
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

}
