<?xml version="1.0" encoding="UTF-8"?>
<?altova_samplexml algoTrader.xmi?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:UML="omg.org/UML/1.4">
	<xsl:template match="/">
		<html>
			<body>
				<p><b>Calculated Fields</b></p>
				<table border="1">
					<tr>
						<th>Class</th>
						<th>Attribute</th>						
						<th>Formula</th>
					</tr>
					<xsl:for-each select="//UML:TaggedValue[@name='@andromda.hibernate.formula']">
						<tr>
							<td>
								<xsl:value-of select="ancestor::UML:Class/@name"/>
							</td>
							<td>
								<xsl:value-of select="ancestor::UML:Attribute/@name"/>
							</td>
							<td>
								<pre>
									<xsl:value-of select="UML:TaggedValue.dataValue"/>
								</pre>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<p></p>
				<p><b>Finder Methods</b></p>
				<table border="1">
					<tr>
						<th>Class</th>
						<th>Operation</th>						
						<th>Formula</th>
					</tr>
					<xsl:for-each select="//UML:TaggedValue[@name='@andromda.hibernate.query']">
						<tr>
							<td>
								<xsl:value-of select="ancestor::UML:Class/@name"/>
							</td>
							<td>
								<xsl:value-of select="ancestor::UML:Operation/@name"/>
							</td>
							<td>
								<pre>
									<xsl:value-of select="UML:TaggedValue.dataValue"/>
								</pre>
							</td>
						</tr>
					</xsl:for-each>
				</table>				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
