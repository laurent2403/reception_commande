<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">


	<xsl:template match="commande">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"
			font-family="any" font-style="normal" font-weight="normal">
			<fo:layout-master-set>

				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21cm" margin-top="1cm"
					margin-bottom="1cm" margin-left="1cm" margin-right="1cm"
					reference-orientation="90">
					<!-- definition des zones de la page -->
					<fo:region-body region-name="xsl-region-body"
						margin-top="1.5cm" margin-bottom="1.2cm" margin-left="0cm"
						margin-right="0cm" background-color="white" />
					<fo:region-before region-name="xsl-region-before"
						extent="1.5cm" precedence='false' background-color="white" />
					<fo:region-after region-name="xsl-region-after"
						extent="0.5cm" precedence='false' background-color="white" />
					<!--<fo:region-start region-name="xsl-region-start" extent="1cm" background-color="lightgreen" 
						/> <fo:region-end region-name="xsl-region-end" extent="1cm" background-color="lightgreen" 
						/> -->
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence initial-page-number="1"
				master-reference="simpleA4">

				<!-- entete de page -->
				<fo:static-content flow-name="xsl-region-before">
					<fo:block font-size="16pt" text-align="center"
						font-weight="bold">
						Commande
						<xsl:value-of select="numeroCmd" />
					</fo:block>
				</fo:static-content>

				<!-- pied de page -->
				<fo:static-content flow-name="xsl-region-after">
					<fo:block font-size="8pt" text-align="center" font-weight="bold">
						<fo:page-number />
						/
						<fo:page-number-citation ref-id="last-page" />
					</fo:block>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">

					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-number="1" column-width="135mm" />
						<fo:table-column column-number="2" column-width="135mm" />

						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left">
										Client :
										<xsl:value-of select="codeClient" />
										-
										<xsl:value-of select="nomClient" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right">
										<xsl:value-of select="typeRepresClient" />
										:
										<xsl:value-of select="codeRepresClient" />
										-
										<xsl:value-of select="nomRepresClient" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left">
										<xsl:value-of select="AdrLigne1Client" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right">
										<xsl:value-of select="typeChefZoneClient" />
										:
										<xsl:value-of select="codeChefZoneClient" />
										-
										<xsl:value-of select="nomChefZoneClient" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left">
										<xsl:value-of select="AdrLigne2Client" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right">
										Date de création :
										<xsl:value-of select="dateCreationCmd" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left">
										<xsl:value-of select="bpClient" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right">
										Date de synchro :
										<xsl:value-of select="dateSynchroCmd" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left">
										<xsl:value-of select="cpClient" />
										-
										<xsl:value-of select="villeClient" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right">
										Date impression :
										<xsl:value-of select="datePrintCmd" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left">
										<xsl:value-of select="telClient" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>

					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-number="1" column-width="25mm" />
						<fo:table-column column-number="2" column-width="35mm" />
						<fo:table-column column-number="3" column-width="115mm" />
						<fo:table-column column-number="4" column-width="20mm" />
						<fo:table-column column-number="5" column-width="30mm" />
						<fo:table-column column-number="6" column-width="15mm" />
						<fo:table-column column-number="7" column-width="30mm" />

						<fo:table-body>
							<!-- entête de colonne -->
							<fo:table-row text-align="left" background-color="white">
								<fo:table-cell border-style="solid" border-width="0.2mm"
									padding="1mm">
									<fo:block>Référence</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm"
									padding="1mm">
									<fo:block>Gencod</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm"
									padding="1mm">
									<fo:block>Désignation</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm"
									padding="1mm">
									<fo:block>Qté</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm"
									padding="1mm">
									<fo:block>PV. net</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm"
									padding="1mm">
									<fo:block>% rem</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm"
									padding="1mm">
									<fo:block>Total</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- ligne de commande -->
							<xsl:for-each select="listeComDet/comDet">
								<fo:table-row>
									<fo:table-cell border-style="solid" border-width="0.2mm"
										padding="1mm">
										<fo:block text-align="left">
											<xsl:value-of select="reference" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.2mm"
										padding="1mm">
										<fo:block text-align="left">
											<xsl:value-of select="gencod" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.2mm"
										padding="1mm">
										<fo:block text-align="left">
											<xsl:value-of select="designation" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.2mm"
										padding="1mm">
										<fo:block text-align="right">
											<xsl:value-of select="qteCmd" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.2mm"
										padding="1mm">
										<fo:block text-align="right">
											<xsl:value-of select="pvClient" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.2mm"
										padding="1mm">
										<fo:block text-align="right">
											<xsl:value-of select="pcRemiseClient" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.2mm"
										padding="1mm">
										<fo:block text-align="right">
											<xsl:value-of select="totalLigne" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>

					<fo:block text-align="left" space-before="5mm" font-size="14pt"
						font-weight="bold">
						Message :
						<xsl:value-of select="message" />
					</fo:block>

					<fo:table space-before="5mm" table-layout="fixed" width="100%">
						<fo:table-column column-number="1" column-width="135mm" />
						<fo:table-column column-number="2" column-width="135mm" />

						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left">
										Nombre de lignes :
										<xsl:value-of select="nbLigne" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="16pt" text-align="right"
										font-weight="bold">
										Total net :
										<xsl:value-of select="totalNet" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>

					<!-- balise pour indiquer que c'est la derniere page -->
					<fo:block id="last-page">
					</fo:block>

				</fo:flow>

			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>