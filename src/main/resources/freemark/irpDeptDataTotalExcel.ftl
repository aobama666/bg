<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>EPRI-17Y2</Author>
  <LastAuthor>flying</LastAuthor>
  <Created>2017-09-11T02:56:20Z</Created>
  <LastSaved>2018-01-22T16:05:07Z</LastSaved>
  <Version>14.00</Version>
 </DocumentProperties>
 <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
  <AllowPNG/>
 </OfficeDocumentSettings>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>7575</WindowHeight>
  <WindowWidth>19395</WindowWidth>
  <WindowTopX>600</WindowTopX>
  <WindowTopY>165</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Center"/>
   <Borders/>
   <Font ss:FontName="宋体" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="s62">
   <Font ss:FontName="华文仿宋" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
  </Style>
  <Style ss:ID="s63">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center" ss:WrapText="1"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="华文仿宋" x:CharSet="134" ss:Size="11" ss:Color="#000000"
    ss:Bold="1"/>
   <Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s64">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center" ss:WrapText="1"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="华文仿宋" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
   <Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s65">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Font ss:FontName="华文仿宋" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
  </Style>
  <Style ss:ID="s67">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center" ss:WrapText="1"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="华文仿宋" x:CharSet="134" ss:Size="11" ss:Color="#000000"
    ss:Bold="1"/>
   <Interior ss:Color="#FFFFFF" ss:Pattern="Solid"/>
  </Style>
 </Styles>
 <Worksheet ss:Name="知识产权各月完成值统计">
  <Table ss:ExpandedColumnCount="5" ss:ExpandedRowCount="14" x:FullColumns="1"
   x:FullRows="1" ss:StyleID="s62" ss:DefaultColumnWidth="54"
   ss:DefaultRowHeight="16.5">
   <Column ss:StyleID="s65" ss:AutoFitWidth="0"/>
   <Column ss:StyleID="s65" ss:AutoFitWidth="0" ss:Width="125.25"/>
   <Column ss:StyleID="s65" ss:AutoFitWidth="0" ss:Width="162.75"/>
   <Column ss:StyleID="s65" ss:AutoFitWidth="0" ss:Width="147"/>
   <Column ss:StyleID="s65" ss:AutoFitWidth="0" ss:Width="210"/>
   <Row ss:AutoFitHeight="0" ss:Height="39">
    <Cell ss:StyleID="s63"><Data ss:Type="String">月度</Data></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">当月申请专利（项）</Data></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">年度累计申请专利（项）</Data></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">当月申请发明专利(项)</Data></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">年度累计申请发明专利(项)</Data></Cell>
   </Row>
<#if SHOW01=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">1月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT101}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT201}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT101}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT201}</Data></Cell>
   </Row></#if>
   <#if SHOW02=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">2月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT102}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT202}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT102}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT202}</Data></Cell>
   </Row></#if>
   <#if SHOW03=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">3月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT103}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT203}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT103}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT203}</Data></Cell>
   </Row></#if>
   <#if SHOW04=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">4月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT104}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT204}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT104}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT204}</Data></Cell>
   </Row></#if>
   <#if SHOW05=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">5月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT105}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT205}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT105}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT205}</Data></Cell>
   </Row></#if>
   <#if SHOW06=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">6月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT106}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT206}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT106}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT206}</Data></Cell>
   </Row></#if>
   <#if SHOW07=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">7月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT107}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT207}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT107}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT207}</Data></Cell>
   </Row></#if>
   <#if SHOW08=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">8月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT108}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT208}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT108}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT208}</Data></Cell>
   </Row></#if>
   <#if SHOW09=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">9月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT109}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT209}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT109}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT209}</Data></Cell>
   </Row></#if>
   <#if SHOW10=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">10月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT110}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT210}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT110}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT210}</Data></Cell>
   </Row></#if>
   <#if SHOW11=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">11月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT111}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT211}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT111}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT211}</Data></Cell>
   </Row></#if>
   <#if SHOW12=="1"><Row ss:AutoFitHeight="0" ss:Height="15.9375">
    <Cell ss:StyleID="s64"><Data ss:Type="String">12月</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT112}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${PT212}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT112}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${VT212}</Data></Cell>
   </Row></#if>   
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>9</PaperSizeIndex>
    <HorizontalResolution>600</HorizontalResolution>
    <VerticalResolution>600</VerticalResolution>
   </Print>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveCol>1</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>
