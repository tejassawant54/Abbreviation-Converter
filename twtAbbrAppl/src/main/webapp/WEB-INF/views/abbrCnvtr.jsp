<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Abbr Cnvtr</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/javascript/grid/jquery-1.4.4.min.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/javascript/grid/jquery.tablednd_0_5.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/javascript/grid/grid.locale-en.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/javascript/grid/jquery.jqGrid.min.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/javascript/calender.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/javascript/grid/jquery.center.min.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/javascript/grid/jquery.msg.js" ></script>
		<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/resources/styles/grid/jquery.msg.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/resources/styles/grid/jquery-ui-1.8.1.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/resources/styles/grid/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/resources/styles/grid/ui.multiselect.css" />
		<style>
			.ui-jqgrid tr.jqgrow td { 
				white-space: normal !important;
			}
		</style> 
		<script type="text/javascript">
			window.onload=setGridXML;
			
			function setGridXML(){
				var GridXML	= "${GridXML}";
				if(GridXML!= ""){
					//var xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
					//var xmlDoc=document.implementation.createDocument("","",null); 
					//xmlDoc.loadXML(GridXML);
					//var totRowObj =	xmlDoc.getElementsByTagName("totalrecords")[0];
					/*if(parseInt(totRowObj.text) == 0 ){
						document.getElementById("ErrorDiv").innerHTML = "No Results Found.";
					}
					else{
						document.getElementById("ErrorDiv").innerHTML = "";
					}*/
					
					$("#jQGrid").html("<table id=\"list_external\"  border=\1 style=\"table-layout:fixed;\" width='100%' class=\"scroll\" ></table><div id=\"page_external\" class=\"scroll\"></div>");  
					jQuery("#list_external").tableDnD();
		            jQuery("#list_external").jqGrid(
	            	{
	                	datatype: "xmlstring",                     
						datastr : GridXML,
						xmlReader : {root:"elements", row:"element", page:"requestList>currentpage", total:"requestList>totalpages", records:"requestList>totalrecords", repeatitems:false },					 	
	                    height: 450,
	                    width: 1250,
	                    colNames:['Sr.No.','Abbreviated Text','Non Abbreviated Text'], 
	                    colModel:[
	                        {name:'SrNo',index:'SrNo',sortable:true, align:'center',width:53},
	                        {name:'AbbrText',index:'AbbrText', sortable:true, align:'left',width:475},                           
	                        {name:'NonAbbrText',index:'NonAbbrText',sortable:true, align:'left',width:690}
					    ],
					    shrinkToFit:false,
					    multiselect: false,
	                    paging: true,  
	                    rowNum:15,  
	                    pager: $("#page_external"), 
	                    loadonce:true,
	                    viewrecords: true,
	                    onSelectRow: function(id){
		                   	 var rowData  = jQuery('#list_external').getRowData(id);
		                   	 var AbbrText = rowData.AbbrText;
		                     alert('onSelectRow'+AbbrText);
		                   	 //hourGlass();
		                   	 //document.forms["requestSearchForm"].action="<%=request.getContextPath()%>/sqmrequest/showRequestDetails.htm?reqId="+AbbrText;
							 //document.forms["requestSearchForm"].method = "post";
							 //document.forms["requestSearchForm"].submit();
	                    }
	            	}
	            );
	           }
			}
			function hourGlass(){
			 $.msg({ 
			 	  autoUnblock : false,
			 	  clickUnblock : false,
				  content : '<img src="${pageContext.request.contextPath}/resources/images/hourGlass.gif"/> <font size="2" face="verdana" color="red"><b>Please wait...</b></font>'
			 });
			}
			function fnSearch(){
				hourGlass();
				document.forms["requestSearchForm"].action = "<%=request.getContextPath()%>/abbrCnvtr.ts";
				document.forms["requestSearchForm"].method = "get";
				document.forms["requestSearchForm"].submit();
			}
			function fnReset(){
				document.forms["requestSearchForm"].action = "<%=request.getContextPath()%>/abbrCnvtr.ts";
				document.forms["requestSearchForm"].method = "get";
				document.forms["requestSearchForm"].submit();
			}	
			
		</script>
	</head>
	
	<body bgcolor="FAFAF3">
		<form name="requestSearchForm" id="requestSearchForm" method="post">
			<ul id="primary-nav" class="nav-bar">
					<div style=" position: absolute; top:10px; left: 10px;"><a href="<%=request.getContextPath()%>/" >Home</a></div>
					<div style=" position: absolute; top:10px; left: 60px;"><a href="<%=request.getContextPath()%>/wekaStats" >WekaStats</a></div>
			</ul>
			<div class="mainContainer">
				<div class="mainContainerUpperSection">
					
					<div align="center">
						<span class="textHeading"><h1>@bbr Cnvtr</h1></span>
					</div>
					<div style="width: 960px; margin-top: 2px; position: relative; ">
							<div class="positionCenter" style="width: 30px;"></div>
							<div align="center"><form:errors path="*" cssClass="errorMessage"/></div>
							<div class="positionLeft clearLeftSpace" style="width: 15px;"></div>
					<div id="class1ComponentContainer" class="positionLeft">
						<table class="table" border="0px" align="center">
							<tbody>
								<tr>
									<td><label><span
											id="lblclass1cat_MasterAccountName" >Search Text</span> </label> <input
										title="Enter Search Query "
										style=" width: 200px; font-family: Verdana; font-size: 11px;"
										type="text" name="searchText" id="stext" value="" />
									</td>
								</tr>
								<tr>
									<td style="position: absolute; top: 0px; left: 630px;"><span>
											<button onclick="fnReset();" id="clear"	class="largeButtonBlueBG">
												<span class="whiteText">Reset</span>
												</button>
									</span></td>
									<td style="position: absolute; top: 0px; left: 700px"><span>
											<button onclick="fnSearch();" id="save"
												class="largeButtonBlueBG">
												<span class="whiteText">Search</span>
											</button>
									</span></td>
									<!-- <td style="position: absolute; top: 0px; left: 850px"><span>
											<button onclick="fnWekaStats();" id="save"
												class="largeButtonBlueBG">
												<span class="whiteText">WekaStats</span>
											</button>
									</span></td> -->
								</tr>
							</tbody>
						</table>
					</div>

					<div align="center">
								<span class="textHeading" style="position: absolute; top: 50px; left: 550px"></span>
							</div>
							<div id="ErrorDiv" align="center" class="error"></div>
							<div id="jQGrid" align="center"></div>
							<br/>
					</div>
				</div>
			</div>
		</form>
		<!-- <div align="center">
								<span class="textHeading">Search Result</span>
							</div>
							<div id="ErrorDiv" align="center" class="error"></div>
							<div id="jQGrid" align="center"></div>
							<br/> -->
	</body>
</html>