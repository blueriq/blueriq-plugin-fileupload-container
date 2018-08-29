package com.aquima.plugin.fileupload.config;

import com.aquima.plugin.fileupload.BasicFileUploadHandler;
import com.aquima.plugin.fileupload.IFileUploadHandler;
import com.aquima.web.boot.annotation.RootConfiguration;
import com.aquima.web.boot.annotation.ServerConfiguration;
import com.aquima.web.ui.xslt.IDynamicXsltLoader;

import com.blueriq.component.api.plugin.IPluginMetadata;
import com.blueriq.component.api.plugin.PluginMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * Main configuration for the FileUpload Plugin.
 * 
 * @author hj.van.veenendaal
 * @since 8.1
 */
public class FileUploadConfig {

  @ServerConfiguration(basePackages = "com.aquima.plugin.fileupload.controller")
  public static class FileUploadServerConfig {
    // for component scanning
  }


  @RootConfiguration(basePackages = "com.aquima.plugin.fileupload.container")
  public static class FileUploadRootConfig {
    @Autowired
    private IDynamicXsltLoader dynamicXsltLoader;

    @PostConstruct
    public void loadComposerXslt() {
      this.dynamicXsltLoader.registerXslt("UI/xslt/fileupload/button-fileupload.xsl");
    }

    @Bean
    public IFileUploadHandler basicFileUploadHandler() {
      return new BasicFileUploadHandler();
    }
  }

}
