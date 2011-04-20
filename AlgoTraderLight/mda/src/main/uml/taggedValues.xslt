<?xml version="1.0" encoding="UTF-8"?>
<?altova_samplexml algoTrader.xmi?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:UML="omg.org/UML/1.4">
	<xsl:key name="class-index" match="UML:Class" use="@xmi.id"/>
	<xsl:template match="/">	
		<html>
			<body>
				<p><b>Tagged Values</b></p>
				<table border="1">
					<tr>
						<th>Parent</th>				
						<th>Name</th>
						<th>Value</th>
					</tr>
					<xsl:for-each select="//UML:TaggedValue">
						<tr>
							<td>
								<nobr>
									<xsl:choose>
										<xsl:when test="ancestor::UML:Operation">
											<xsl:value-of select="string-join(ancestor::UML:Package/@name,'.')"/>.<xsl:value-of select="ancestor::UML:Class/@name"/>.<xsl:value-of select="ancestor::UML:Operation/@name"/>()
										</xsl:when>
										<xsl:when test="ancestor::UML:Attribute">
											<xsl:value-of select="string-join(ancestor::UML:Package/@name,'.')"/>.<xsl:value-of select="ancestor::UML:Class/@name"/>.<xsl:value-of select="ancestor::UML:Attribute/@name"/>
										</xsl:when>
										<xsl:when test="ancestor::UML:AssociationEnd">
											<xsl:value-of select="string-join(ancestor::UML:Package/@name,'.')"/>.<xsl:value-of select="key('class-index',ancestor::UML:Association.connection/UML:AssociationEnd[1]/@participant)/@name"/><br/> 
											<xsl:value-of select="string-join(ancestor::UML:Package/@name,'.')"/>.<xsl:value-of select="key('class-index',ancestor::UML:Association.connection/UML:AssociationEnd[2]/@participant)/@name"/><br/>
											(<xsl:value-of select="ancestor::UML:AssociationEnd/@name"/>)
										</xsl:when>		
										<xsl:otherwise>
											<xsl:value-of select="string-join(ancestor::UML:Package/@name,'.')"/>.<xsl:value-of select="ancestor::UML:Class/@name"/>
										</xsl:otherwise>
									</xsl:choose>
								</nobr>
							</td>
							<td>
								<xsl:value-of select="@name"/>
							</td>	
							<td>
								<pre>
									<xsl:value-of select="UML:TaggedValue.dataValue"/>
									<xsl:value-of select="UML:TaggedValue.referenceValue/UML:ModelElement/XMI.extension/referentPath/@xmi.value"/>									
								</pre>
							</td>
						</tr>
					</xsl:for-each>
				</table>			
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
