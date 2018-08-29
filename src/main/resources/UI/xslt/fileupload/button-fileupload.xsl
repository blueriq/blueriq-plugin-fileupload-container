<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
	<xsl:param name="singleFileUploadLink" select="'../Fileupload.html'"/>
	<xsl:param name="singleFileUploadLinkAdditionalArgs"/>

	<xsl:template match="button[@format='file_upload']">
		<input type="file">
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
		</input>
		<br />
		<input type="submit" class="aq-button">
			<xsl:attribute name="value"><xsl:value-of select="@caption"/></xsl:attribute>
			<xsl:attribute name="onclick">
				this.form.action = '<xsl:value-of select="$singleFileUploadLink"/>?<xsl:value-of select="$idPrefix"/>sessionId=<xsl:value-of select="$sessionId"/><xsl:value-of select="$singleFileUploadLinkAdditionalArgs"/>';
				this.form.enctype = 'multipart/form-data';
			</xsl:attribute>
			<xsl:if test="@disabled='true'">
				<xsl:attribute name="disabled">disabled</xsl:attribute>
				<xsl:attribute name="class">disabled</xsl:attribute>
			</xsl:if>
		</input>
	</xsl:template>
	
	<xsl:template match="button[@format='file_download']">
		<input type="button" class="aq-button">
			<xsl:attribute name="onclick">
				location.href = '<xsl:value-of select="$singleFileUploadLink"/>?<xsl:value-of select="$idPrefix"/>download=<xsl:value-of select="@name"/>&amp;<xsl:value-of select="$idPrefix"/>sessionId=<xsl:value-of select="$sessionId"/><xsl:value-of select="$singleFileUploadLinkAdditionalArgs"/>';
			</xsl:attribute>
			<xsl:attribute name="value">Download <xsl:value-of select="@caption"/></xsl:attribute>
			<xsl:if test="@disabled='true'">
				<xsl:attribute name="disabled">disabled</xsl:attribute>
				<xsl:attribute name="class">disabled</xsl:attribute>
			</xsl:if>
		</input>
	</xsl:template>
	
	<xsl:template match="button[@format='file_delete']">
		<input type="button" class="aq-button">
			<xsl:attribute name="onclick">
				location.href = '<xsl:value-of select="$singleFileUploadLink"/>?<xsl:value-of select="$idPrefix"/>delete=<xsl:value-of select="@name"/>&amp;<xsl:value-of select="$idPrefix"/>sessionId=<xsl:value-of select="$sessionId"/><xsl:value-of select="$singleFileUploadLinkAdditionalArgs"/>';
			</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="@caption"/></xsl:attribute>
			<xsl:if test="@disabled='true'">
				<xsl:attribute name="disabled">disabled</xsl:attribute>
				<xsl:attribute name="class">disabled</xsl:attribute>
			</xsl:if>
		</input>
	</xsl:template>
</xsl:stylesheet>