var NUM_CENTYEAR = 30;
// is time input control required by default
var BUL_TIMECOMPONENT = false;
// are year scrolling buttons required by default
var BUL_YEARSCROLL = true;
var cordX=100 ;
var cordY=100 ;

// date format 0 for dd, 1 for mm , 2 for year
var szDateFormat;
var separator;
var calendars = [];
var RE_NUM = /^\-?\d+$/;
var arrCalenderInstances=new Array();
var counterOfInstances=0;


function calendar1(obj_target,X,Y)
{
	this.gen_date = cal_gen_date1;
	this.gen_time = cal_gen_time1;
	this.gen_tsmp = cal_gen_tsmp1;
	this.prs_date = cal_prs_date1;
	this.prs_time = cal_prs_time1;
	this.prs_tsmp = cal_prs_tsmp1;
	this.popup    = cal_popup1;

	// validate input parameters
	if (!obj_target)
		return cal_error("Error calling the calendar: no target control specified");
	if (obj_target.value == null)
		return cal_error("Error calling the calendar: parameter specified is not valid tardet control");
	this.target = obj_target;
	this.time_comp = BUL_TIMECOMPONENT;
	this.year_scroll = BUL_YEARSCROLL;

	// register in global collections
	this.id = calendars.length;
	calendars[this.id] = this;
	cordX = X;
	cordY = Y;
}


function cal_popup1 (str_datetime)
{
	this.dt_current = this.prs_tsmp(str_datetime ? str_datetime : this.target.value);
	if (!this.dt_current) return;

	var obj_calwindow = window.open(
		'calender.htm?datetime=' + this.dt_current.valueOf()+ '&id=' + this.id,
		'Calendar', 'width=190,height=180,status=no,resizable=no,top='+cordY+',left='+cordX+' ,dependent=yes,alwaysRaised=yes'
	);
	obj_calwindow.opener = window;

	//Add in the array of arrCalenderInstances
	arrCalenderInstances[counterOfInstances]=obj_calwindow;
	counterOfInstances+=1;
	obj_calwindow.focus();
}



// timestamp generating function
function cal_gen_tsmp1 (dt_datetime)
{
	return(this.gen_date(dt_datetime) + ' ' + this.gen_time(dt_datetime));
}

// date generating function
function cal_gen_date1 (dt_datetime)
{

	var szTemp = "";
	var arrDateFormat = szDateFormat.split(separator);
	var arrSize = arrDateFormat.length ;

	for(var i=0;i<arrSize;i++)
	{

	   if((arrDateFormat[i]=="dd") || (arrDateFormat[i]=="DD"))
	   {
	     szTemp = szTemp+(dt_datetime.getDate() < 10 ? '0' : '') + dt_datetime.getDate();
	   }
	   if((arrDateFormat[i]=="mm") || (arrDateFormat[i]=="MM"))
	   {
	     szTemp=szTemp+(dt_datetime.getMonth() < 9 ? '0' : '') + (dt_datetime.getMonth() + 1);
	   }
	   if((arrDateFormat[i]=="yyyy") || (arrDateFormat[i]=="YYYY"))
	   {
	     szTemp=szTemp+dt_datetime.getFullYear();
	   }
	   if(i+1<arrSize)
	  	 szTemp = szTemp+separator ;
	}

	return szTemp;

}
// time generating function
function cal_gen_time1 (dt_datetime)
{
	return (
		(dt_datetime.getHours() < 10 ? '0' : '') + dt_datetime.getHours() + ":"
		+ (dt_datetime.getMinutes() < 10 ? '0' : '') + (dt_datetime.getMinutes()) + ":"
		+ (dt_datetime.getSeconds() < 10 ? '0' : '') + (dt_datetime.getSeconds())
	);
}

// timestamp parsing function
function cal_prs_tsmp1 (str_datetime)
{
	// if no parameter specified return current timestamp
	if (!str_datetime)
		return (new Date());

	// if positive integer treat as milliseconds from epoch
	if (RE_NUM.exec(str_datetime))
		return new Date(str_datetime);

	// else treat as date in string format
	var arr_datetime = str_datetime.split(' ');
	return this.prs_time(arr_datetime[1], this.prs_date(arr_datetime[0]));

}

// date parsing function
function cal_prs_date1 (str_date)
{

	var errorExist = false ;
	var arr_date = str_date.split(separator);
	var arr_dateFormat = szDateFormat.split(separator);
	var d=0;
	var m=0;
	var y=0;
	for(var i=0;i<3;i++)
	{
	   if((arr_dateFormat[i]=="dd") || (arr_dateFormat[i]=="DD"))
	   	d = i;
	   if((arr_dateFormat[i]=="mm") || (arr_dateFormat[i]=="MM"))
	   	m = i;
	   if((arr_dateFormat[i]=="yyyy") || (arr_dateFormat[i]=="YYYY"))
	   	y = i;
	}
	if (arr_date.length != 3)
	{
		errorExist= true ;
		//return cal_error ("Invalid date format: '" + str_date + "'.\nFormat accepted is dd-mm-yyyy.");
	}
	if (!arr_date[d])
	{
		errorExist= true ;
		//return cal_error ("Invalid date format: '" + str_date + "'.\nNo day of month value can be found.");
	}
	if (!RE_NUM.exec(arr_date[d]))
	{
		errorExist= true ;
		//return cal_error ("Invalid day of month value: '" + arr_date[0] + "'.\nAllowed values are unsigned integers.");
	}
	if (!arr_date[m])
	{
		errorExist= true ;
		//return cal_error ("Invalid date format: '" + str_date + "'.\nNo month value can be found.");
	}
	if (!RE_NUM.exec(arr_date[m]))
	{
		errorExist= true ;
		//return cal_error ("Invalid month value: '" + arr_date[1] + "'.\nAllowed values are unsigned integers.");
	}
	if (!arr_date[y])
	{
		errorExist= true ;
		//return cal_error ("Invalid date format: '" + str_date + "'.\nNo year value can be found.");
	}
	if (!RE_NUM.exec(arr_date[y]))
	{
		errorExist= true ;
		//return cal_error ("Invalid year value: '" + arr_date[2] + "'.\nAllowed values are unsigned integers.");
	}
	var dt_date = new Date();

	if(errorExist)
	{
	    return dt_date ;
	}

	dt_date.setDate(1);
	if (arr_date[m] < 1 || arr_date[m] > 12) return cal_error ("Invalid month value: '" + arr_date[m] + "'.\nAllowed range is 01-12.");
	dt_date.setMonth(arr_date[m]-1);

	if (arr_date[y] < 100) arr_date[y] = Number(arr_date[y]) + (arr_date[y] < NUM_CENTYEAR ? 2000 : 1900);
	dt_date.setFullYear(arr_date[y]);

	var dt_numdays = new Date(arr_date[y], arr_date[m], 0);
	dt_date.setDate(arr_date[d]);
	if (dt_date.getMonth() != (arr_date[m]-1)) return cal_error ("Invalid day of month value: '" + arr_date[d] + "'.\nAllowed range is 01-"+dt_numdays.getDate()+".");

	return (dt_date)


}

// time parsing function
function cal_prs_time1 (str_time, dt_date)
{
	if (!dt_date) return null;
	var arr_time = String(str_time ? str_time : '').split(':');

	if (!arr_time[0]) dt_date.setHours(0);
	else if (RE_NUM.exec(arr_time[0]))
		if (arr_time[0] < 24) dt_date.setHours(arr_time[0]);
		else return cal_error ("Invalid hours value: '" + arr_time[0] + "'.\nAllowed range is 00-23.");
	else return cal_error ("Invalid hours value: '" + arr_time[0] + "'.\nAllowed values are unsigned integers.");

	if (!arr_time[1]) dt_date.setMinutes(0);
	else if (RE_NUM.exec(arr_time[1]))
		if (arr_time[1] < 60) dt_date.setMinutes(arr_time[1]);
		else return cal_error ("Invalid minutes value: '" + arr_time[1] + "'.\nAllowed range is 00-59.");
	else return cal_error ("Invalid minutes value: '" + arr_time[1] + "'.\nAllowed values are unsigned integers.");

	if (!arr_time[2]) dt_date.setSeconds(0);
	else if (RE_NUM.exec(arr_time[2]))
		if (arr_time[2] < 60) dt_date.setSeconds(arr_time[2]);
		else return cal_error ("Invalid seconds value: '" + arr_time[2] + "'.\nAllowed range is 00-59.");
	else return cal_error ("Invalid seconds value: '" + arr_time[2] + "'.\nAllowed values are unsigned integers.");

	dt_date.setMilliseconds(0);
	return dt_date;
}

function cal_error (str_message)
{
	alert (">"+str_message);
	return null;
}


function popUpCal(target, dateFormat){
	var X = 200 ;
	var Y = 200 ;
	szDateFormat=dateFormat;
	if (szDateFormat==null) szDateFormat="dd/MM/yyyy";
	separator = szDateFormat.substring(2,3);
	if (separator==null) separator="/";

	X = window.event.clientX + window.screenLeft-6 ;
	Y = window.event.clientY + window.screenTop+6;
	try
	 {
	  x1=screen.width
	  y1=screen.height
	  x2=event.screenX
	  y2=event.screenY

	  if((y1-y2)<250)
	     final_Y=y2-220
	  else
	     final_Y=y2
	  if((x1-x2)<200)
	     final_X=x1-200
	  else
	     final_X=x2

			X=final_X
			Y=final_Y+6

	 }
	 catch(e){}
	var cal = new calendar1(target,X,Y);
	cal.popup();
}
