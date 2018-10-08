<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>PL/SQL Developer</Author>
  <LastAuthor>flying</LastAuthor>
  <Created>2017-12-05T09:30:29Z</Created>
  <LastSaved>2018-01-21T12:56:36Z</LastSaved>
  <Company>Allround Automations</Company>
  <Version>14.00</Version>
 </DocumentProperties>
 <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
  <AllowPNG/>
 </OfficeDocumentSettings>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>9000</WindowHeight>
  <WindowWidth>20175</WindowWidth>
  <WindowTopX>750</WindowTopX>
  <WindowTopY>585</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Bottom"/>
   <Borders/>
   <Font ss:FontName="宋体" x:CharSet="134" ss:Size="9" ss:Color="#000000"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="s63">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center" ss:WrapText="1"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Courier New" x:Family="Modern" ss:Size="11"
    ss:Color="#000000" ss:Bold="1"/>
   <Interior ss:Color="#FFFF00" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s64">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
  </Style>
 </Styles>
 <Worksheet ss:Name="论文">
  <Names>
   <NamedRange ss:Name="_FilterDatabase" ss:RefersTo="=论文!R1C1:R1C16" ss:Hidden="1"/>
  </Names>
  <Table ss:ExpandedColumnCount="16" ss:ExpandedRowCount="${(dataList?size)+2}" x:FullColumns="1"
   x:FullRows="1" ss:DefaultColumnWidth="42" ss:DefaultRowHeight="11.25">
   <Column ss:AutoFitWidth="0" ss:Width="47.25"/>
   <Column ss:AutoFitWidth="0" ss:Width="225" ss:Span="1"/>
   <Column ss:Index="4" ss:AutoFitWidth="0" ss:Width="195"/>
   <Column ss:AutoFitWidth="0" ss:Width="225"/>
   <Column ss:AutoFitWidth="0" ss:Width="141"/>
   <Column ss:AutoFitWidth="0" ss:Width="138"/>
   <Column ss:AutoFitWidth="0" ss:Width="117.75"/>
   <Column ss:AutoFitWidth="0" ss:Width="111.75"/>
   <Column ss:AutoFitWidth="0" ss:Width="134.25" ss:Span="1"/>
   <Column ss:Index="12" ss:AutoFitWidth="0" ss:Width="120.75"/>
   <Column ss:AutoFitWidth="0" ss:Width="60.75"/>
   <Column ss:AutoFitWidth="0" ss:Width="53.25"/>
   <Column ss:AutoFitWidth="0" ss:Width="65.25"/>
   <Column ss:AutoFitWidth="0" ss:Width="53.25"/>
   <Row ss:AutoFitHeight="0" ss:Height="29.25">
    <Cell ss:StyleID="s63"><Data ss:Type="String">序号</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">论文名称</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">作者姓名</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">第一作者单位</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">刊物名称</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">刊号</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">刊期</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">刊物级别</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">第一作者单位与上报单位的关系</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">技术领域(一级)</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">技术领域(二级)</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">技术领域(三级)</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">上报年份</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">上报季度</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">系统内排名</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
    <Cell ss:StyleID="s63"><Data ss:Type="String">备注</Data><NamedCell
      ss:Name="_FilterDatabase"/></Cell>
   </Row>
   <#list dataList as v>
   <Row ss:AutoFitHeight="0">
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["PAPER_ID"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["PAPER_NAME"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["AUTH_NAME"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["AUTH_ORG"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["PERIODICL_NAME"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["PERIODICL_CODE"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["PERIODICL_PERIOD"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["PERIODICL_LEVEL"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["AUTH_ORGREL"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["TECH_FIELD"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["SECOND_TECHFIELD"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["THREED_TECHFIELD"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["SEND_YEAR"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["SEND_QUARTER"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["SYSTEM_ORDER"]}</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">${v["PAPER_REMARK"]}</Data></Cell>
   </Row>
   </#list>
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <Unsynced/>
   <Selected/>
   <FreezePanes/>
   <FrozenNoSplit/>
   <SplitHorizontal>1</SplitHorizontal>
   <TopRowBottomPane>1</TopRowBottomPane>
   <ActivePane>2</ActivePane>
   <Panes>
    <Pane>
     <Number>3</Number>
    </Pane>
    <Pane>
     <Number>2</Number>
     <ActiveRow>0</ActiveRow>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>
